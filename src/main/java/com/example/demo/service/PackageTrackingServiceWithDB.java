package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.example.demo.boundary.History;
import com.example.demo.boundary.Order;
import com.example.demo.boundary.Status;
import com.example.demo.boundary.TrackBoundary;
import com.example.demo.boundary.User;
import com.example.demo.consumers.RestOrderConsumer;
import com.example.demo.consumers.RestUserConsumer;
import com.example.demo.converter.TrackConverter;
import com.example.demo.dal.PackageTrackingDao;
import com.example.demo.data.Track;
import com.example.demo.exceptions.IllegalShoppingCartIdException;
import com.example.demo.exceptions.IncorrectStatusException;
import com.example.demo.exceptions.InvalidApproximatedArrivalDate;
import com.example.demo.exceptions.InvalidEmailException;
import com.example.demo.exceptions.NoSuchOrderException;
import com.example.demo.exceptions.NoSuchUserException;
import com.example.demo.exceptions.TrackNotFoundException;
import com.example.demo.utility.DateUtility;
import com.example.demo.validations.Validators;

@Service
public class PackageTrackingServiceWithDB implements PackageTrackingService {

	private PackageTrackingDao packageTrackingDao;
	private TrackConverter trackConverter;
	private RestUserConsumer restUserConsumer;
	private RestOrderConsumer restOrderConsumer;
	private Validators validators;
	private DateUtility dateUtils;

	@Autowired
	public void setRestOrderConsumer(RestOrderConsumer restOrderConsumer) {
		this.restOrderConsumer = restOrderConsumer;
	}

	@Autowired
	public void setRestUserConsumer(RestUserConsumer restUserConsumer) {
		this.restUserConsumer = restUserConsumer;
	}

	@Autowired
	public void setTrackConverter(TrackConverter trackConverter) {
		this.trackConverter = trackConverter;
	}

	@Autowired
	public void setPackageTrackingDao(PackageTrackingDao packageTrackingDao) {
		this.packageTrackingDao = packageTrackingDao;
	}

	@Autowired
	public void setValidators(Validators validators) {
		this.validators = validators;
	}

	@Autowired
	public void setDateUtils(DateUtility dateUtils) {
		this.dateUtils = dateUtils;
	}

	@Override
	public TrackBoundary create(TrackBoundary trackBoundary) {
		User user = restUserConsumer.fetch(trackBoundary.getUser().getEmail());
		if (!validators.validateUser(user)) {
			throw new NoSuchUserException("No such user.");
		}

		Order order = restOrderConsumer.fetch(trackBoundary.getOrder().getShoppingCartId());
		System.err.println("ORDER:" + order);
		if (!validators.validateOrder(order)) {
			throw new NoSuchOrderException("No such order.");
		}

		if (!validators.validateApproximatedArrivalDate(trackBoundary.getApproximatedArrivalDate(), new Date()))
			throw new InvalidApproximatedArrivalDate("Incorrect approx. arrival date.");

		trackBoundary.setStatus(Status.DEPARTED);
		return this.trackConverter
				.toBoundary(this.packageTrackingDao.save(this.trackConverter.toEntity(trackBoundary)));
	}

	@Override
	public TrackBoundary getSpecificTrack(String trackId) {
		return this.trackConverter.toBoundary(this.packageTrackingDao.findById(trackId)
				.orElseThrow(() -> new TrackNotFoundException("Track with id " + trackId + "wasn't found")));
	}

	@Override
	public void updateTrack(TrackBoundary trackBoundary, String trackId) {
		Track oldTrack = this.packageTrackingDao.findById(trackId).orElseThrow(() -> new RuntimeException());
		History oldInfo = new History(oldTrack.getCreatedTimestamp(), oldTrack.getStatus(), oldTrack.getDescription());
		oldTrack.getHistory().add(oldInfo);

		if (validators.validateDescription(trackBoundary, oldTrack))
			oldTrack.setDescription(trackBoundary.getDescription());

		if (!validators.validateApproximatedArrivalDate(trackBoundary.getApproximatedArrivalDate(),
				oldTrack.getApproximatedArrivalDate(), oldTrack.getCreatedTimestamp()))
			throw new InvalidApproximatedArrivalDate("Incorrect approx. arrival date.");

		oldTrack.setApproximatedArrivalDate(dateUtils.parseDate(trackBoundary.getApproximatedArrivalDate()));

		if (trackBoundary.getStatus() != null) {
			switch (trackBoundary.getStatus()) {
			case ACCEPTED:
				if (oldTrack.getStatus() == Status.LOST || oldTrack.getStatus() == Status.DEPARTED
						|| oldTrack.getStatus() == Status.ARRIVED)
					oldTrack.setStatus(trackBoundary.getStatus());
				break;
			case ARRIVED:
				if (oldTrack.getStatus() == Status.LOST || oldTrack.getStatus() == Status.DEPARTED)
					oldTrack.setStatus(trackBoundary.getStatus());
				break;
			case DEPARTED:
				throw new IncorrectStatusException("Cannot update to this status.");
			case LOST:
				if (oldTrack.getStatus() == Status.ARRIVED || oldTrack.getStatus() == Status.DEPARTED)
					oldTrack.setStatus(trackBoundary.getStatus());
				break;
			default:
				throw new IncorrectStatusException("Failed to update status.");
			}
		}

		oldTrack.setCreatedTimestamp(new Date());
		this.packageTrackingDao.save(oldTrack);
	}

	@Override
	public void deleteAll() {
		this.packageTrackingDao.deleteAll();
	}

	@Override
	public List<TrackBoundary> getTracksByApproximatedArrivalDate(String email, String value, String sortBy,
			String sortOrder, int page, int size) {

		if (!validators.validateUserEmail(email)) {
			throw new InvalidEmailException();
		}

		if (!validators.validateUser(restUserConsumer.fetch(email))) {
			throw new NoSuchUserException("No such user.");
		}

		Direction direction = sortOrder.equals(Direction.ASC.toString()) ? Direction.ASC : Direction.DESC;
		return this.packageTrackingDao
				.findAllByUser_emailAndApproximatedArrivalDateGreaterThanEqual(email, dateUtils.parseDate(value),
						PageRequest.of(page, size, direction, sortBy))
				.stream().map(this.trackConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public List<TrackBoundary> getTracksByCreatedTimestamp(String email, String value, String sortBy, String sortOrder,
			int page, int size) {
		if (!validators.validateUserEmail(email)) {
			throw new InvalidEmailException();
		}

		if (!validators.validateUser(restUserConsumer.fetch(email))) {
			throw new NoSuchUserException("No such user.");
		}

		Direction direction = sortOrder.equals(Direction.ASC.toString()) ? Direction.ASC : Direction.DESC;
		return this.packageTrackingDao
				.findAllByUser_emailAndCreatedTimestampGreaterThanEqual(email, dateUtils.parseDate(value),
						PageRequest.of(page, size, direction, sortBy))
				.stream().map(this.trackConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public List<TrackBoundary> getTracksByStatus(String email, String value, String sortBy, String sortOrder, int page,
			int size) {
		if (!validators.validateUserEmail(email)) {
			throw new InvalidEmailException();
		}

		if (!validators.validateUser(restUserConsumer.fetch(email))) {
			throw new NoSuchUserException("No such user.");
		}
		Direction direction = sortOrder.equals(Direction.ASC.toString()) ? Direction.ASC : Direction.DESC;
		return this.packageTrackingDao
				.findAllByUser_emailAndStatus(email, value, PageRequest.of(page, size, direction, sortBy)).stream()
				.map(this.trackConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public List<TrackBoundary> getTracksByEmail(String email, String sortBy, String sortOrder, int page, int size) {
		if (!validators.validateUserEmail(email)) {
			throw new InvalidEmailException();
		}

		if (!validators.validateUser(restUserConsumer.fetch(email))) {
			throw new NoSuchUserException("No such user.");
		}

		Direction direction = sortOrder.equals(Direction.ASC.toString()) ? Direction.ASC : Direction.DESC;
		return this.packageTrackingDao.findAllByUser_email(email, PageRequest.of(page, size, direction, sortBy))
				.stream().map(this.trackConverter::toBoundary).collect(Collectors.toList());

	}

	@Override
	public List<TrackBoundary> getTracksByShoppingCartId(String shoppingCartId, String sortBy, String sortOrder,
			int page, int size) {
		if (!validators.validateShppingCartId(shoppingCartId)) {
			throw new IllegalShoppingCartIdException("id cannot be empty of null");
		}
		Direction direction = sortOrder.equals(Direction.ASC.toString()) ? Direction.ASC : Direction.DESC;
		return this.packageTrackingDao
				.findAllByOrder_shoppingCartId(shoppingCartId, PageRequest.of(page, size, direction, sortBy)).stream()
				.map(this.trackConverter::toBoundary).collect(Collectors.toList());

	}

}
