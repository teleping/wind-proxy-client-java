package com.bs.data.wind.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author zhangping
 *
 */
public class WindData {

	private static final Logger logger = Logger.getLogger(WindData.class);

	// public static final String NULL_STRING = "NaN";
	// public static final String TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	public static final String CODE = "code";
	public static final String FIELD = "field";
	public static final String TIME = "time";
	public static final String DATA = "data";
	public static final String CLOSE = "CLOSE";

	private int errorCode = 0;
	private int cellCount = 0;

	private List<String> codeList = new ArrayList<String>();
	private List<String> fieldList = new ArrayList<String>();
	private List<Date> timeList = new ArrayList<Date>();
	private List<Object> data = new ArrayList<Object>();
	private String client = null;

	public static final Map<Integer, String> message = new HashMap<Integer, String>() {
		{
			put(-40520001, "未知错误");
			put(-40520002, "内部错误");
			put(-40520003, "系统错误");
			put(-40520004, "登录失败");
			put(-40520005, "无权限");
			put(-40520006, "用户取消");
			put(-40520007, "无数据");
			put(-40520008, "超时错误");
			put(-40520009, "本地WBOX错误");
			put(-40520010, "需要内容不存在");
			put(-40520011, "需要服务器不存在");
			put(-40520012, "引用不存在");
			put(-40520013, "其他地方登录错误");
			put(-40520014, "未登录使用WIM工具，故无法登录");
			put(-40520015, "连续登录失败次数过多");
			put(-40521001, "IO操作错误");
			put(-40521002, "后台服务器不可用");
			put(-40521003, "网络连接失败");
			put(-40521004, "请求发送失败");
			put(-40521005, "数据接收失败");
			put(-40521006, "网络错误");
			put(-40521007, "服务器拒绝请求");
			put(-40521008, "错误的应答");
			put(-40521009, "数据解码失败");
			put(-40521010, "网络超时");
			put(-40521011, "频繁访问");
			put(-40522001, "无合法会话");
			put(-40522002, "非法数据服务");
			put(-40522003, "非法请求");
			put(-40522004, "万得代码语法错误");
			put(-40522005, "不支持的万得代码");
			put(-40522006, "指标语法错误");
			put(-40522007, "不支持的指标");
			put(-40522008, "指标参数语法错误");
			put(-40522009, "不支持的指标参数");
			put(-40522010, "日期与时间语法错误");
			put(-40522011, "不支持的日期与时间");
			put(-40522012, "不支持的请求参数");
			put(-40522013, "数组下标越界");
			put(-40522014, "重复的WQID");
			put(-40522015, "请求无相应权限");
			put(-40522016, "不支持的数据类型");
			put(-40522017, "数据提取量超限");
			// put(-50520001, "程序异常(Proxy)");
			// put(-50522017, "提取数量限制(Proxy)");
		}
	};

	public List<String> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<String> codeList) {
		this.codeList = codeList;
	}

	public List<String> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<String> fieldList) {
		this.fieldList = fieldList;
	}

	public List<Date> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<Date> timeList) {
		this.timeList = timeList;
	}

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorCode != 0 ? message.get(this.getErrorCode()) : null;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public int getCellCount() {
		if (cellCount == 0) {
			cellCount = codeList.size() + fieldList.size() + timeList.size() + data.size();
		}
		return cellCount;
	}

	public void setCellCount(int cellCount) {
		this.cellCount = cellCount;
	}

	public List<JSONObject> getMapRows() {
		List<JSONObject> list = new ArrayList<JSONObject>();

		if (this.getErrorCode() != 0 || codeList.size() * fieldList.size() * timeList.size() != data.size()) {
			logger.error("Wind查询错误：" + (this.getErrorMessage() != null ? this.getErrorMessage() : this.getErrorCode()));
			return null;
		}

		// logger.debug(fieldList);
		Iterator<Object> it = this.getData().iterator();

		for (Date time : timeList) {
			for (String code : codeList) {
				JSONObject row = new JSONObject();
				boolean notNull = false;
				for (String field : fieldList) {
					Object obj = it.next();
					row.put(field, obj);
					if (obj != null) {
						notNull = true;
					}
				}
				if (notNull) {
					row.put(CODE, code);
					row.put(TIME, time);
					list.add(row);

					logger.debug(JSON.toJSONStringWithDateFormat(row, "yyyy-MM-dd"));
				}
			}
		}
		return list;
	}
}
