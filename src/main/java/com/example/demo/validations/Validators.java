package com.example.demo.validations;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.boundary.Status;
import com.example.demo.boundary.TrackBoundary;
import com.example.demo.boundary.User;
import com.example.demo.data.Track;
import com.example.demo.utility.DateUtility;

@Component
public class Validators {

	@Autowired
	private DateUtility dateUtils;

	public boolean validateUser(User user) {
		return user != null && validateUserEmail(user.getEmail());
	}

	public boolean validateUserEmail(String userEmail) {
		if (userEmail == null || userEmail.isEmpty()) {
			return false;
		}
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		return Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE).matcher(userEmail).matches();

	}

	public boolean validateDescription(TrackBoundary trackBoundary, Track track) {
		return trackBoundary.getDescription() != null && !trackBoundary.getDescription().isEmpty()
				&& !track.getDescription().equals(trackBoundary.getDescription());
	}

	public boolean validateApproximatedArrivalDate(TrackBoundary trackBoundary, Track track) {
		return trackBoundary.getApproximatedArrivalDate() != null
				&& !trackBoundary.getApproximatedArrivalDate().isEmpty() && !track.getApproximatedArrivalDate()
						.equals(dateUtils.parseDate(trackBoundary.getApproximatedArrivalDate()));
	}

	public boolean validateShppingCartId(String shoppingCartId) {
		return shoppingCartId != null && !shoppingCartId.isEmpty();
	}

	public boolean validateStatus(String value) {
		return value.equals(Status.ACCEPTED.toString()) || value.equals(Status.ARRIVED.toString())
				|| value.equals(Status.DEPARTED.toString()) || value.equals(Status.LOST.toString());

	}
}
