package com.yezi.capacitywall.creatjson;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.yezi.capacitywall.bean.Seccode;

public class WriteJson {

	public String writeWifiInfo(List<Seccode> wifi){
		String json=JSON.toJSONString(wifi);
		System.out.println(json);
		return json;
	}
}
