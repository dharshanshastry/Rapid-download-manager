package com.sgi.utils;

public class UnitUtility {

	public  static String convertBytesToReadableMBOrGB(long fileSize){
		float fileSizeInKB  = (float)(fileSize/1024);
		if(fileSizeInKB>1024){
			float fileSizeinMB =(float) (fileSizeInKB/1024);

			if(fileSizeinMB>1024){
				float fileSizeinGB =(float) (fileSizeinMB/1024);
				return fileSizeinGB+" GB";
			}
			return fileSizeinMB+" MB";
		}
		return fileSizeInKB+" KB";


	}
	
}
