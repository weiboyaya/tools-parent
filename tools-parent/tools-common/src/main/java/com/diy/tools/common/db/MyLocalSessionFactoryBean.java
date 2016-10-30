package com.diy.tools.common.db;

import java.beans.Encoder;

import javax.sql.DataSource;

import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

import com.alibaba.druid.pool.DruidDataSource;

public class MyLocalSessionFactoryBean extends LocalSessionFactoryBean {

	private DataSource dataSource;

	public MyLocalSessionFactoryBean() {
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		DruidDataSource pooledDataSource = null;
		pooledDataSource = (DruidDataSource) dataSource;
		String user = pooledDataSource.getUsername();
		String password = pooledDataSource.getPassword();
		Encoder encoder = new Encoder();
		pooledDataSource.setUsername(user);
		pooledDataSource.setPassword(password);
		this.dataSource = pooledDataSource;
	}

}
