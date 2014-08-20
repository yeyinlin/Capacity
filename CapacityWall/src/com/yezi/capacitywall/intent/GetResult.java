package com.yezi.capacitywall.intent;

import java.util.List;

public class GetResult {

	public String getResult(String url){
		String requestUrl = HttpClient.requestUrl(url);
		System.out.println(requestUrl);
		return requestUrl;
	}
	
	
	
	public String getResult(String url,List<Parameter> params){
		String requestUrl;
		try {
			requestUrl = HttpClient.httpPost1(url, params);
			System.out.println(requestUrl);
			return requestUrl;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
