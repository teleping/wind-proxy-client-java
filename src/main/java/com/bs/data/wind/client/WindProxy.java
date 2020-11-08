package com.bs.data.wind.client;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.bs.common.utils.HttpUtil;
import com.bs.common.utils.PropUtil;

/**
 * 
 * @author zhangping
 *
 */
public class WindProxy {

	private static final Logger logger = Logger.getLogger(WindProxy.class);

	public static final String DEFAULT_CHARSET = "GBK";

	public static final int TIME_TRIES = 3;
	public static final String KEY_ERROR_CODE = "errorCode";

	public static final String T_STRING = "T";
	public static final String SPACE_STRING = " ";
	public static final String NAN_STRING = "\"NaN\"";
	public static final String NULL_STRING = "null";
	public static Pattern DATE_PATTERN = Pattern.compile(
			"[1-9]\\d{3}\\-(0[1-9]|1[0-2])\\-(0[1-9]|[1-2][0-9]|3[0-1])T(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d");

	public static final String TIME_FORMAT = "yyyy-MM-dd";

	// api.wind.edb=http://192.168.1.102:8080/wind/edb?codes={0}&startTime={1}&endTime={2}&options={3}
	// api.wind.wsd=http://192.168.1.102:8080/wind/wsd?codes={0}&fields={1}&startTime={2}&endTime={3}&options={4}
	// api.wind.wss=http://192.168.1.102:8080/wind/wss?codes={0}&fields={1}
	// api.wind.wset=http://192.168.1.102:8080/wind/wset?reportName={0}&options={1}

	public static final Properties CONFIG = PropUtil.getProp("config.properties");
	public static String SERVICE_URL = CONFIG.getProperty("api.wind.host").trim();
	public static String SERVICE_URL_EDB = SERVICE_URL + CONFIG.getProperty("api.wind.edb").trim();
	public static String SERVICE_URL_WSD = SERVICE_URL + CONFIG.getProperty("api.wind.wsd").trim();
	public static String SERVICE_URL_WSS = SERVICE_URL + CONFIG.getProperty("api.wind.wss").trim();
	public static String SERVICE_URL_WSET = SERVICE_URL + CONFIG.getProperty("api.wind.wset").trim();

	public static final String edbByProxy(String serviceUrl, String codes, String startTime, String endTime,
			String options) {
		String url = MessageFormat.format(serviceUrl, codes, startTime, endTime, options);
		return get(url, TIME_TRIES);
	}

	public static final String edbByProxy(String codes, String startTime, String endTime, String options) {
		return edbByProxy(SERVICE_URL_EDB, codes, startTime, endTime, options);
	}

	public static final WindData edb(String serviceUrl, String codes, String startTime, String endTime,
			String options) {
		WindData data = null;
		String json = edbByProxy(serviceUrl, codes, startTime, endTime, options);
		if (json != null) {
			data = JSON.parseObject(json, WindData.class);
		}
		return data;
	}

	public static final WindData edb(String codes, String startTime, String endTime, String options) {
		return edb(SERVICE_URL_EDB, codes, startTime, endTime, options);
	}

	public static final WindData edb(String serviceUrl, String codes, Date startTime, Date endTime) {
		return edb(serviceUrl, codes, new SimpleDateFormat(TIME_FORMAT).format(startTime),
				new SimpleDateFormat(TIME_FORMAT).format(endTime), "");
	}

	public static final WindData edb(String codes, Date startTime, Date endTime) {
		return edb(SERVICE_URL_EDB, codes, startTime, endTime);
	}

	public static final String wsdByProxy(String codes, String fields, String startTime, String endTime,
			String options) {
		String url = MessageFormat.format(SERVICE_URL_WSD, codes, fields, startTime, endTime, options);
		return get(url, TIME_TRIES);
	}

	public static final WindData wsd(String codes, String fields, String startTime, String endTime, String options) {
		WindData data = null;
		String json = wsdByProxy(codes, fields, startTime, endTime, options);
		if (json != null) {
			data = new WindData();
			data = JSON.parseObject(json, WindData.class);
		}
		return data;
	}

	public static final WindData wsd(String codes, String fields, Date startTime, Date endTime, String options) {
		return wsd(codes, fields, new SimpleDateFormat(TIME_FORMAT).format(startTime),
				new SimpleDateFormat(TIME_FORMAT).format(endTime), options);
	}

	public static final String wssByProxy(String codes, String fields) {
		String url = MessageFormat.format(SERVICE_URL_WSS, codes, fields);
		return get(url, TIME_TRIES);
	}

	public static final WindData wss(String codes, String fields) {
		WindData data = null;
		String json = wssByProxy(codes, fields);
		if (json != null) {
			data = new WindData();
			data = JSON.parseObject(json, WindData.class);
		}
		return data;
	}

	public static final String wsetByProxy(String reportName, String options) {
		String url = MessageFormat.format(SERVICE_URL_WSET, reportName, options);
		return get(url, TIME_TRIES);
	}

	public static final WindData wset(String reportName, String options) {
		WindData data = null;
		String json = wsetByProxy(reportName, options);
		if (json != null) {
			data = new WindData();
			data = JSON.parseObject(json, WindData.class);
		}
		return data;
	}

	private static final String get(String url, int times) {
		String json = null;
		try {
			while (times > 0) {
				json = HttpUtil.get(url, DEFAULT_CHARSET);

				if (json != null) {
					json = clear(json);
					break;
				} else {
					times--;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return json;
	}

	private static final String clear(String json) {
		Matcher m = DATE_PATTERN.matcher(json);
		while (m.find()) {
			String match = m.group();
			json = json.replace(match, match.replaceAll(T_STRING, SPACE_STRING));
		}

		return json.replace(NAN_STRING, NULL_STRING);
	}
}
