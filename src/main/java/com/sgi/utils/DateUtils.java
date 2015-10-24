package com.sgi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {



	public static Date formateDate (Date date){
		try {
			if(date == null){
				return null;
			}
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
			System.out.println(formatter.format(date));
			Date s = formatter.parse(formatter.format(date));
			System.out.println(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}


}
