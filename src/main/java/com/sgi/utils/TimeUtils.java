package com.sgi.utils;

public class TimeUtils {

	public static String converToTime(long timeRemainingInSecs) {
		int hours = (int) (timeRemainingInSecs / 3600);
		int minutes = (int) ((timeRemainingInSecs % 3600) / 60);
		int seconds = (int) (timeRemainingInSecs % 60);
		StringBuilder timeString  = new StringBuilder();
		if(hours != 0)
		{
			timeString.append(hours+ " h " );
		}
		if(minutes != 0){
			timeString.append(minutes +" m " );
		}
		if(seconds != 0){
			timeString.append(seconds+" s" );
		}
		//return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		return timeString.toString();
	}

}
