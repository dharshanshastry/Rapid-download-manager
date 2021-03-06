package com.sgi.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.hsqldb.persist.HsqlProperties;

import com.sgi.utils.FileUtils;

public class HSQLDBManager {

	private static final String SQL_DOWNLOAD_MANAGER_SQL_FILE_PATH = "/sql/download_manager.sql";
	
	
	final static String dbLocation = "."+File.separator+"DB"+File.separator; // change it to your db location
	static org.hsqldb.server.Server sonicServer;
	Connection dbConn = null;

	public static void startDBServer() {
		HsqlProperties props = new HsqlProperties();
		props.setProperty("server.database.0", "file:" + dbLocation + "RapidDB;");
		props.setProperty("server.dbname.0", "xdb");
		sonicServer = new org.hsqldb.Server();
		try {
			sonicServer.setProperties(props);
			sonicServer.start();
			if(!doesDBExist()){
				createDB();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private static boolean doesDBExist(){
		File dbFile = new File("."  +File.separator + "DB"+File.separator+"RapidDB.script");
		return dbFile.exists();

	}

	private static void createDB(){
		try {
			Connection conn = PooledConnectionManager.getDataSource().getConnection();
			Statement stmt = conn.createStatement();
			String sqlFile =  FileUtils.readFile(HSQLDBManager.class.getResourceAsStream(SQL_DOWNLOAD_MANAGER_SQL_FILE_PATH), 5000);
			String[] statements  = sqlFile.split(";");	
			for (String statement : statements) {
				stmt.executeQuery(statement);
			}
		} catch (Exception e) {
			// TODO: handle exception 
		}
	}

	public static void stopDBServer() {
		sonicServer.shutdown();
	}

	public Connection getDBConn() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			dbConn = DriverManager.getConnection(
					"jdbc:hsqldb:hsql://localhost/xdb", "SA", "");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return dbConn;
	}
}

