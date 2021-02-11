package com.example.demo.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateUtility {

	public Date parseDate(String dateFormat) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setLenient(false);
			Date date = sdf.parse(dateFormat);
			return date;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
}
