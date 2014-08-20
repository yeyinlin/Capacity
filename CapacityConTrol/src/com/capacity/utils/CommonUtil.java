package com.capacity.utils;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;





public class CommonUtil {
	//header ��������//
	private static final String ENCODING_PREFIX = "encoding";
	private static final String NOCACHE_PREFIX = "no-cache";
	private static final String ENCODING_DEFAULT = "UTF-8"; //
	private static final boolean NOCACHE_DEFAULT = true;

	//content-type ���� //
	private static final String TEXT = "text/plain";
	private static final String JSON = "application/json";
	private static final String XML = "text/xml";
	private static final String HTML = "text/html";


	// �ƹ�jsp/freemakerֱ������ı��ĺ��� //

	/**
	 * ֱ��������ݵļ�㺯��.

	 * eg.
	 * render("text/plain", "hello", "encoding:GBK");
	 * render("text/plain", "hello", "no-cache:false");
	 * render("text/plain", "hello", "encoding:GBK", "no-cache:false");
	 * 
	 * @param headers �ɱ��header���飬Ŀǰ���ܵ�ֵΪ"encoding:"��"no-cache:",Ĭ��ֵ�ֱ�ΪUTF-8��true.
	 */
	public static void render(final HttpServletResponse response,final String contentType, final String content, final String... headers) {
		try {
			//����headers����
			String encoding = ENCODING_DEFAULT;
			boolean noCache = NOCACHE_DEFAULT;
			for (String header : headers) {
				String headerName = StringUtils.substringBefore(header, ":");
				String headerValue = StringUtils.substringAfter(header, ":");

				if (StringUtils.equalsIgnoreCase(headerName, ENCODING_PREFIX)) {
					encoding = headerValue;
				} else if (StringUtils.equalsIgnoreCase(headerName, NOCACHE_PREFIX)) {
					noCache = Boolean.parseBoolean(headerValue);
				} else
					throw new IllegalArgumentException(headerName + "����һ���Ϸ���header����");
			}

			//����headers����
			String fullContentType = contentType + ";charset=" + encoding;
			response.setContentType(fullContentType);
			if (noCache) {
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
			}

			PrintWriter writer = response.getWriter();
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ֱ������ı�.
	 * @see #render(String, String, String...)
	 */
	public static void renderText(final HttpServletResponse response,final String text, final String... headers) {
		render(response,TEXT, text, headers);
	}

	/**
	 * ֱ�����HTML.
	 * @see #render(String, String, String...)
	 */
	public static void renderHtml(final HttpServletResponse response,final String html, final String... headers) {
		render(response,HTML, html, headers);
	}

	/**
	 * ֱ�����XML.
	 * @see #render(String, String, String...)
	 */
	public static void renderXml(final HttpServletResponse response,final String xml, final String... headers) {
		render(response,XML, xml, headers);
	}

	/**
	 * ֱ�����JSON.
	 * 
	 * @param string json�ַ���.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson(final HttpServletResponse response,final String jsonString, final String... headers) {
		render(response,JSON, jsonString, headers);
	}

	/**
	 * ֱ�����JSON.
	 * 
	 * @param map Map����,����ת��Ϊjson�ַ���.
	 * @see #render(String, String, String...)
	 */
	@SuppressWarnings("unchecked")
	public static void renderJson(final HttpServletResponse response,final Map map, final String... headers) {
		String jsonString = JSONObject.fromObject(map).toString();
		render(response,JSON, jsonString, headers);
	}

	/**
	 * ֱ�����JSON.
	 * 
	 * @param object Java����,����ת��Ϊjson�ַ���.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson(final HttpServletResponse response,final Object object, final String... headers) {
		String jsonString = JSONObject.fromObject(object).toString();
		render(response,JSON, jsonString, headers);
	}
	
	/**
	 * ֱ�����JSON.
	 * @param response
	 * @param list
	 * @param headers
	 */
	public static void renderJson(final HttpServletResponse response,final List<?> list, final String... headers) {
		String jsonString = JSONArray.fromObject(list).toString();
		render(response,JSON, jsonString, headers);
	}
	
	/**
	 * ֱ�����JSON.����java.sql.date��������
	 * @param response
	 * @param object
	 * @param headers
	 */
	public static void renderJsonForSqlDate(final HttpServletResponse response,final Object object, final String... headers) {
		JsDateJsonBeanProcessor beanProcessor = new JsDateJsonBeanProcessor();
 		JsonConfig config = new JsonConfig();
 		config.registerJsonBeanProcessor(java.sql.Date.class, beanProcessor);
 		JSONObject json = JSONObject.fromObject(object, config);
		render(response,JSON, json.toString(), headers);
	}
	
	
	public static void main(String args[]){
		
	}
	
	/**
	 * ֱ�����������ת��
	 * @param rep
	 * @param message
	 * @param URL
	 * @param headers
	 */
	public static void renderScript(final HttpServletResponse rep,final String message,final String... headers){
			PrintWriter printer = null;
			try {
				rep.setContentType("text/html;charset=UTF-8");
				printer = rep.getWriter();
				printer.write("<script language = 'javascript'>");
				printer.write("alert('"+message+"'),");
				printer.write("window.history.go(-1)");
				printer.write("</script>");
			} catch (IOException e) {
				System.err.println(e.getLocalizedMessage());
			} finally {
				if (printer != null) {
					printer.close();
				}
			}
	}
	
	/**
	 * json To List<?>
	 * @param jsonStr
	 * @param objClass
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<?> toList(final String jsonStr,Class<?> objClass){
		  JSONArray jsArray = JSONArray.fromObject(jsonStr);
		  List<?> list = JSONArray.toList(jsArray, objClass);
		  return list;
	}
	
	/**
	 * json to object
	 * @param jsonStr
	 * @param objClass
	 * @return
	 */
	public static Object toObject(final String jsonStr,Class<?> objClass){
		JSONObject jsObject = JSONObject.fromObject(jsonStr);
		return JSONObject.toBean(jsObject, objClass);
	}
	
}