package com.example.demo.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.example.demo.Converter.TrackConverter;
import com.example.demo.boundary.History;
import com.example.demo.boundary.Status;
import com.example.demo.boundary.TrackBoundary;
import com.example.demo.boundary.User;
import com.example.demo.consumers.RestUserConsumer;
import com.example.demo.dal.PackageTrackingDao;
import com.example.demo.data.Track;
import com.example.demo.exceptions.IncorrectStatusException;
import com.example.demo.exceptions.InvalidEmailException;
import com.example.demo.exceptions.NoSuchTrackException;

@Service
public class PackageTrackingServiceWithDB implements PackageTrackingService {

	private PackageTrackingDao packageTrackingDao;
	private TrackConverter trackConverter;
	private RestUserConsumer restUserConsumer;

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

	@Override
	public TrackBoundary create(TrackBoundary trackBoundary) {
		User user = restUserConsumer.getUser(trackBoundary.getUser().getEmail());
		if (user != null && !user.getEmail().isEmpty()) {
			// TODO - Check if shoppingCartId exists && expired == true
			Track track = this.trackConverter.toEntity(trackBoundary);
			track = this.packageTrackingDao.save(track);
			return this.trackConverter.toBoundary(track);
		}
		return null;
	}

	@Override
	public TrackBoundary getSpecificTrack(String trackId) {

		return this.trackConverter.toBoundary(this.packageTrackingDao.findById(trackId)
				.orElseThrow(() -> new NoSuchTrackException("Track with id " + trackId + "wasn't found")));

	}

	@Override
	public void updateTrack(TrackBoundary track, String trackId) {
		Track oldTrack = this.packageTrackingDao.findById(trackId).orElseThrow(() -> new RuntimeException());
		
		// Update history list
		History oldInfo = new History();
		oldInfo.setCreatedTimestamp(oldTrack.getCreatedTimestamp());
		oldInfo.setDescription(oldTrack.getDescription());
		oldInfo.setStatus(oldTrack.getStatus());
		oldTrack.getHistory().add(oldInfo);
		
		// Update description
		if (track.getDescription() != null && !track.getDescription().isEmpty()
				&& !oldTrack.getDescription().equals(track.getDescription()))
			oldTrack.setDescription(track.getDescription());

		// Update Approx. arrival date
		if (track.getApproximatedArrivalDate() != null && !track.getApproximatedArrivalDate().isEmpty()
				&& !oldTrack.getApproximatedArrivalDate().equals(track.getApproximatedArrivalDate()))
			oldTrack.setApproximatedArrivalDate(parseDate(track.getApproximatedArrivalDate()));

		// Update status
		if (track.getStatus() != null) {
			switch (track.getStatus()) {
			case ACCEPTED:
					oldTrack.setStatus(track.getStatus());
				break;
			case ARRIVED:
				if (oldTrack.getStatus() == Status.LOST || oldTrack.getStatus() == Status.DEPARTED)
					oldTrack.setStatus(track.getStatus());
				break;
			case DEPARTED:
				throw new IncorrectStatusException("Cannot update to this status.");
			case LOST:
				if (oldTrack.getStatus() == Status.ARRIVED || oldTrack.getStatus() == Status.DEPARTED)
					oldTrack.setStatus(track.getStatus());
				break;
			default:
				throw new IncorrectStatusException("Failed to update status.");
			}
		}
		// Update created timestamp
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
		if(!validateUserEmail(email)) {
			throw new InvalidEmailException("Email must be in the format of example@example.com");
		}
		Direction direction = sortOrder.equals(Direction.ASC.toString()) ? Direction.ASC : Direction.DESC;
		return this.packageTrackingDao
		.findAllByUser_emailAndApproximatedArrivalDateGreaterThanEqual(email, parseDate(value), 
				PageRequest.of(page, size,direction, sortBy))
		.stream()
		.map(this.trackConverter::toBoundary).collect(Collectors.toList());		
		
		
	}

	@Override
	public List<TrackBoundary> getTracksByCreatedTimestamp(String email, String value, String sortBy, String sortOrder,
			int page, int size) {
		if(!validateUserEmail(email)) {
			throw new InvalidEmailException("Email must be in the format of example@example.com");
		}
		Direction direction = sortOrder.equals(Direction.ASC.toString()) ? Direction.ASC : Direction.DESC;
		return this.packageTrackingDao
		.findAllByUser_emailAndCreatedTimestampGreaterThanEqual(email, parseDate(value), 
				PageRequest.of(page, size,direction, sortBy))
		.stream()
		.map(this.trackConverter::toBoundary).collect(Collectors.toList());		
	}

	@Override
	public List<TrackBoundary> getTracksByStatus(String email, String value, String sortBy, String sortOrder, int page,
			int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrackBoundary> getTracksByEmail(String email, String value, String sortBy, String sortOrder, int page,
			int size) {
		if(!validateUserEmail(email)) {
			throw new InvalidEmailException("Email must be in the format of example@example.com");
		}
		Direction direction = sortOrder.equals(Direction.ASC.toString()) ? Direction.ASC : Direction.DESC;
		return this.packageTrackingDao
		.findAllByUser_email(email, 
				PageRequest.of(page, size,direction, sortBy))
		.stream()
		.map(this.trackConverter::toBoundary).collect(Collectors.toList());		
	
	}

	@Override
	public List<TrackBoundary> getTracksByShoppingCartId(String sortBy, String sortOrder, int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Date parseDate(String dateFormat) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setLenient(false);
			Date date = sdf.parse(dateFormat);
			System.err.println(date);
			return date;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public boolean validateUserEmail(String userEmail) {
		if (userEmail == null || userEmail.isEmpty()) {
			return false;
		}
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                "[a-zA-Z0-9_+&*-]+)*@" + 
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                "A-Z]{2,7}$"; 
		return Pattern.compile(emailRegex,
				Pattern.CASE_INSENSITIVE).matcher(userEmail).matches();

	}

}
