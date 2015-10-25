package com.sgi.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sgi.vo.DownloadVO;

public class FileDetailsUtils {


	public static DownloadVO getInitialFileUtils(String fileURL){
		DownloadVO downloadVO = new DownloadVO();
		try {
			URL url = new URL(fileURL);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			int responseCode = httpConn.getResponseCode();
			httpConn.setReadTimeout(5000);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				String fileName = "";
				String disposition = httpConn.getHeaderField("Content-Disposition");
				String contentType = httpConn.getContentType();
				int totalFileLength = httpConn.getContentLength();

				if (disposition != null) {
					// extracts file name from header field
					int index = disposition.indexOf("filename=");
					if (index > 0) {
						String decodedDisposition=java.net.URLDecoder.decode(disposition, "UTF-8");
						fileName  = decodedDisposition.substring(index + 10,
								decodedDisposition.length() - 1);
					}
				} else {
					// extracts file name from URL

					String decodedUrl=java.net.URLDecoder.decode(fileURL, "UTF-8");
					fileName = decodedUrl.substring(decodedUrl.lastIndexOf("/") + 1,
							decodedUrl.length());
				}
				downloadVO.setFileLengthInBytes(totalFileLength);
				downloadVO.setFileName(fileName);
				downloadVO.setFileExtension(fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
		return downloadVO;

	}

}
