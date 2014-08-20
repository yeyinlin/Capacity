package com.yezi.capacitywall.intent;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import org.apache.http.util.EntityUtils;

public class HttpClient {

	/**
	 * ����http��ַ,
	 * 
	 * @param url
	 *            url��ַ
	 * @return ����ַ���
	 */
	public static String requestUrl(String url) {
		String result = null;
		URI mUri = null;
		DefaultHttpClient client = null;
		HttpGet request = null;
		ClientConnectionManager connectManager = null;
		int timeout = 2000;

		try {
			// ���ó�ʱ
			BasicHttpParams httpParameters = new BasicHttpParams();
			// �������ӳ�ʱ
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
			// ��������ʱ
			HttpConnectionParams.setSoTimeout(httpParameters, timeout); 

			mUri = new URI(url);
			request = new HttpGet(mUri);
			client = new DefaultHttpClient(httpParameters);
			connectManager = client.getConnectionManager();
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (client != null && connectManager != null) {
				connectManager.shutdown();
			}
		}

		return result;
	}

	/**
	 * ͨ��POST��ʽ��������
	 * 
	 * @param url
	 *            URL��ַ
	 * @param params
	 *            ����
	 * @return
	 * @throws Exception
	 */
	public static String httpPost1(String url, List<Parameter> params)
			throws Exception {
		String response = null;
		int timeoutConnection = 2000;
		int timeoutSocket = 5000;
		HttpParams httpParameters = new BasicHttpParams();// Set the timeout in
															// milliseconds
															// until a
															// connection is
															// established.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);// Set the default socket timeout
									// (SO_TIMEOUT) // in milliseconds which is
									// the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		// ����HttpClient��ʵ��
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		HttpPost httpPost = new HttpPost(url);
		if (params.size() >= 0) {
			// ����httpPost�������
			httpPost.setEntity(new UrlEncodedFormEntity(
					buildNameValuePair(params), "GBK"));
		}
		// ʹ��execute��������HTTP Post���󣬲�����HttpResponse����

		HttpResponse httpResponse = httpClient.execute(httpPost);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {
			// ��÷��ؽ��
			response = EntityUtils.toString(httpResponse.getEntity());
		} else {
			response = "�����룺" + statusCode;
		}
		return response;
	}

	/**
	 * ��Parameter���ͼ���ת����NameValuePair���ͼ���
	 * 
	 * @param params
	 *            ��������
	 * @return
	 */
	private static List<BasicNameValuePair> buildNameValuePair(
			List<Parameter> params) {
		List<BasicNameValuePair> result = new ArrayList<BasicNameValuePair>();
		for (Parameter param : params) {
			BasicNameValuePair pair = new BasicNameValuePair(param.getName(),
					param.getValue());
			result.add(pair);
		}
		return result;
	}

}
