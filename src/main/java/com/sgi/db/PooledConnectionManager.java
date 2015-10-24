package com.sgi.db;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;


public class PooledConnectionManager {
	private PooledConnectionManager(){
	}

	private static DataSource dataSource;

	static {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			dataSource = setupDataSource("jdbc:hsqldb:hsql://localhost/xdb");
			//dataSource = setupDataSource("jdbc:hsqldb:file:path-to-file");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	private static  DataSource setupDataSource(String connectURI) {
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI,"SA","");
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
		GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
		genericObjectPoolConfig.setMaxIdle(5);
		genericObjectPoolConfig.setMaxTotal(20);
		ObjectPool<PoolableConnection> connectionPool =
				new GenericObjectPool<>(poolableConnectionFactory,genericObjectPoolConfig);
		poolableConnectionFactory.setPool(connectionPool);
		PoolingDataSource<PoolableConnection> dataSource =
				new PoolingDataSource<>(connectionPool);

		return dataSource;
	}


	public static DataSource getDataSource() {
		return dataSource;
	}
}
