package com.techsure.tsjgit.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

public class Config {
	private static Logger logger = LoggerFactory.getLogger(Config.class);
	public static String TECHSURE_HOME;

	private static final String CONFIG_FILE = "ezdeploy-config.properties";
	public static final String RESPONSE_TYPE_JSON = "application/json;charset=UTF-8";
	public static final String DATA_PATH;
	public static final String FILETRANS_PATH;
	public static final String USERNAME;
	public static final String PASSWORD;

	public static final List<String> ERROR_PATTERN = new ArrayList<String>();
	public static final List<String> WARN_PATTERN = new ArrayList<String>();
	public static final HashSet<String> SQL_EXTNAMES = new HashSet<String>();

	public static final List<String> RES_POSSIBLY_CHARSETS = new ArrayList<String>();

	public static int DEPLOY_EXEC_QUEUE_DEPTH = 1024;
	public static int DEPLOY_EXEC_MIN_THREAD_COUNT = 16;
	public static int DEPLOY_EXEC_MAX_THREAD_COUNT = 16;
	public static int DEPLOY_EXEC_THREAD_KEEPALIVETIME = 120;

	public static boolean DEBUG_DEPLOYEXEC = false;
	public static int DEPLOY_LOGTAIL_BUFLEN = 64 * 1024;

	public static boolean DEPLOY_LOGFILTER_BYTICKET = false;

	public static String PROXY_BACKEND_URL = null;
	public static Integer PROXY_CONN_TIMEOUT = -1;
	public static Integer PROXY_READ_TIMEOUT = -1;

	static {
		TECHSURE_HOME = System.getenv("TECHSURE_HOME");
		if (TECHSURE_HOME == null || "".equals(TECHSURE_HOME)) {
			TECHSURE_HOME = "/app";
		}
		USERNAME = getProperty(CONFIG_FILE, "auth.username", "techsure");
		PASSWORD = getProperty(CONFIG_FILE, "auth.password", "techsure");
		DATA_PATH = TECHSURE_HOME + File.separator + getProperty(CONFIG_FILE, "data.path", "ezdeploy/data");

		FILETRANS_PATH = TECHSURE_HOME + File.separator + (getProperty(CONFIG_FILE, "filetrans.path").endsWith(File.separator) ? getProperty(CONFIG_FILE, "filetrans.path") : getProperty(CONFIG_FILE, "filetrans.path") + File.separator);

		String errorPattern = getProperty(CONFIG_FILE, "error.pattern", "error:");
		for (String err : errorPattern.split(",")) {
			if (!err.trim().equals("")) {
				ERROR_PATTERN.add(err.trim());
			}
		}
		String warnPattern = getProperty(CONFIG_FILE, "warning.pattern", "warn:");
		for (String wrn : warnPattern.split(",")) {
			if (!wrn.trim().equals("")) {
				WARN_PATTERN.add(wrn);
			}
		}

		String possiblyCharsets = getProperty(CONFIG_FILE, "res.possibly.charsets", "UTF-8,GBK,ISO-8859-1");
		for (String charset : possiblyCharsets.split(",")) {
			if (!charset.trim().equals("")) {
				RES_POSSIBLY_CHARSETS.add(charset);
			}
		}

		String batchExecVal = null;
		try {
			batchExecVal = getProperty(CONFIG_FILE, "deployexec.queueDepth");
			DEPLOY_EXEC_QUEUE_DEPTH = Integer.valueOf(batchExecVal);
		} catch (Exception ex) {
		}

		try {
			batchExecVal = getProperty(CONFIG_FILE, "deployexec.minThreadCount");
			DEPLOY_EXEC_MIN_THREAD_COUNT = Integer.valueOf(batchExecVal);
		} catch (Exception ex) {
		}

		try {
			batchExecVal = getProperty(CONFIG_FILE, "deployexec.maxThreadCount");
			DEPLOY_EXEC_MAX_THREAD_COUNT = Integer.valueOf(batchExecVal);
		} catch (Exception ex) {
		}

		try {
			batchExecVal = getProperty(CONFIG_FILE, "deployexec.threadKeepAliveTime");
			DEPLOY_EXEC_THREAD_KEEPALIVETIME = Integer.valueOf(batchExecVal);
		} catch (Exception ex) {
		}

		try {
			batchExecVal = getProperty(CONFIG_FILE, "deploylog.logtail.buflen");
			DEPLOY_LOGTAIL_BUFLEN = Integer.valueOf(batchExecVal);
		} catch (Exception ex) {
		}

		try {
			batchExecVal = getProperty(CONFIG_FILE, "deploylog.filterbyticket");
			DEPLOY_LOGFILTER_BYTICKET = Boolean.parseBoolean(batchExecVal);
		} catch (Exception ex) {
		}

		try {
			batchExecVal = getProperty(CONFIG_FILE, "deployexec.threadKeepAliveTime");
			DEPLOY_EXEC_THREAD_KEEPALIVETIME = Integer.valueOf(batchExecVal);
		} catch (Exception ex) {
		}

		try {
			String sqlExtNameStrs = getProperty(CONFIG_FILE, "sql.extnames");
			String[] sqlExtNameArray = sqlExtNameStrs.split("\\s*,\\s*");
			for (String extName : sqlExtNameArray) {
				SQL_EXTNAMES.add(extName.toLowerCase());
			}
		} catch (Exception ex) {

		}

		try {
			String debugDeployexec = getProperty(CONFIG_FILE, "debug.deployexec");
			DEBUG_DEPLOYEXEC = Boolean.parseBoolean(debugDeployexec);
		} catch (Exception ex) {

		}

		PROXY_BACKEND_URL = getProperty(CONFIG_FILE, "proxy.backend.url");

		String proxyVal = null;
		try {
			proxyVal = getProperty(CONFIG_FILE, "proxy.conn.timeout");
			PROXY_CONN_TIMEOUT = Integer.valueOf(proxyVal);
		} catch (Exception ex) {
		}

		try {
			proxyVal = getProperty(CONFIG_FILE, "proxy.read.timeout");
			PROXY_READ_TIMEOUT = Integer.valueOf(proxyVal);
		} catch (Exception ex) {
		}
	}

	private static String getProperty(String configFile, String keyName, String defaultValue) {
		Properties pro = new Properties();
		InputStream is = Config.class.getClassLoader().getResourceAsStream(configFile);
		try {
			pro.load(is);
			if (is != null)
				is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("load " + configFile + " error: " + ex.getMessage(), ex);
		}

		String value = pro.getProperty(keyName, defaultValue);
		if (value != null)
			value = value.trim();

		return value;
	}

	private static String getProperty(String configFile, String keyName) {
		Properties pro = new Properties();
		InputStream is = Config.class.getClassLoader().getResourceAsStream(configFile);
		try {
			pro.load(is);
			if (is != null)
				is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("load " + configFile + " error: " + ex.getMessage(), ex);
		}
		String value = pro.getProperty(keyName);
		if (value != null)
			value = value.trim();

		return value;
	}
}
