package com.diy.tools.common.utils;

import org.apache.log4j.Logger;

public class LogerUtil {

	private Logger logger;
	private boolean debug;
	private boolean info;

	public LogerUtil(Class obj) {
		logger = Logger.getLogger(obj);
		debug = logger.isDebugEnabled();
		info = logger.isInfoEnabled();
	}

	public void debug(Object msg) {
		if (debug)
			logger.debug(msg);
	}

	public void info(Object msg) {
		if (info)
			logger.info(msg);
	}

	public void warn(Object msg) {
		logger.warn(msg);
	}

	public void error(Object msg) {
		logger.error(msg);
	}

	public void error(Object msg, Exception e) {
		logger.error(msg);
		logger.error(e.toString());
	}

}
