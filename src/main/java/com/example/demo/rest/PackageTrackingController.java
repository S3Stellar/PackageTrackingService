package com.example.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.boundary.TrackBoundary;
import com.example.demo.service.PackageTrackingService;
import com.example.demo.utility.RouteFilterType;

@RestController
public class PackageTrackingController {
	private PackageTrackingService packageTrackingService;

	@Autowired
	public void setPackageTrackingService(PackageTrackingService packageTrackingService) {
		this.packageTrackingService = packageTrackingService;
	}

	@RequestMapping(path = "/tracks", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public TrackBoundary create(@RequestBody TrackBoundary track) {
		return this.packageTrackingService.create(track);
	}

	@RequestMapping(path = "/tracks/{trackId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public TrackBoundary getSpecificTrack(@PathVariable("trackId") String trackId) {
		return this.packageTrackingService.getSpecificTrack(trackId);
	}

	@RequestMapping(path = "/tracks/{trackId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateTrack(@RequestBody TrackBoundary track, @PathVariable("trackId") String trackId) {
		this.packageTrackingService.updateTrack(track, trackId);
	}

	@RequestMapping(path = "/tracks", method = RequestMethod.DELETE)
	public void deleteAllTracks() {
		this.packageTrackingService.deleteAll();
	}

	@RequestMapping(path = "/tracks/byEmail/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public TrackBoundary[] getTracksByEmail(@PathVariable("email") String email,
			@RequestParam(name = "filterType", required = false, defaultValue = "") String type,
			@RequestParam(name = "filterValue", required = false) String value,
			@RequestParam(name = "sortBy", required = false, defaultValue = "createdTimestamp") String sortBy,
			@RequestParam(name = "sortOrder", required = false, defaultValue = "DESC") String sortOrder,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {

		switch (type) {
		case RouteFilterType.BY_APPROXIMATED_ARRIVAL_DATE:
			return this.packageTrackingService
					.getTracksByApproximatedArrivalDate(email, value, sortBy, sortOrder, page, size)
					.toArray(new TrackBoundary[0]);
		case RouteFilterType.BY_CREATED_TIMESTAMP:
			return this.packageTrackingService.getTracksByCreatedTimestamp(email, value, sortBy, sortOrder, page, size)
					.toArray(new TrackBoundary[0]);
		case RouteFilterType.BY_STATUS:
			return this.packageTrackingService.getTracksByStatus(email, value, sortBy, sortOrder, page, size)
					.toArray(new TrackBoundary[0]);
		default:
			return this.packageTrackingService.getTracksByEmail(email, sortBy, sortOrder, page, size)
					.toArray(new TrackBoundary[0]);
		}
	}

	@RequestMapping(path = "/tracks/byCartId/{shoppingCartId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public TrackBoundary[] getTrackByShoppingCartId(@PathVariable("shoppingCartId") String shoppingCartId,
			@RequestParam(name = "sortBy", required = false, defaultValue = "createdTimestamp") String sortBy,
			@RequestParam(name = "sortOrder", required = false, defaultValue = "DESC") String sortOrder,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {

		return this.packageTrackingService.getTracksByShoppingCartId(shoppingCartId, sortBy, sortOrder, page, size)
				.toArray(new TrackBoundary[0]);
	}
}
