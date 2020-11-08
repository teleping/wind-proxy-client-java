package com.bs.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @author zhangping
 *
 */
public class HttpUtil {

	public static final String get(String url) {
		return get(url, "UTF-8");
	}

	public static final String get(String url, String charset) {
		String content = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);

		try {
			CloseableHttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity, charset);

			response.close();
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	public static final String post(String url, String data) {
		String content = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new StringEntity(data, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity);

			EntityUtils.consume(entity);
			response.close();
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	public static final String post(String url, Map<String, String> map) {
		String content = null;

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);

		try {
			if (map != null) {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for (Entry<String, String> entry : map.entrySet())
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getKey()));
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			}
			CloseableHttpResponse response = httpclient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
			response.close();
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
}
