package com.capacity.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.capacity.contect.ConstantValue;
import com.capacity.domain.Result;
import com.capacity.domain.Seccode;
import com.capacity.logic.JavaConComputer;
import com.capacity.utils.CommonUtil;


public class OpenFeiQ extends HttpServlet {
	//计算器
	private String CALC="0";
	//记事本
	private String NOTEPAD="1";
	//15秒关机
	private String RONONCE="2";
	
	private String TSSHUTDN="3";
	
	private String MUSIC="4";
	
	private String COLSEMUSIC="5";
	
	
	private String ERROR="404";
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		JavaConComputer jcc1=new JavaConComputer();
//		jcc1.SetCmdContent(ConstantValue.CALC);
//		returnRusult(resp,"1");
		String code = req.getParameter("Code");
		if(code!=null){
			System.out.println(code);
			List<Seccode> parseArray = JSON.parseArray(code,
					Seccode.class);
			String code2 = parseArray.get(0).getCode();
			JavaConComputer jcc=new JavaConComputer();
			if(code2.equals(CALC)){
				jcc.SetCmdContent(ConstantValue.CALC);
				returnRusult(resp,"1");
			}else if(code2.equals(NOTEPAD)){
				jcc.SetCmdContent(ConstantValue.NOTEPAD);
				returnRusult(resp,"1");
			}
			else if(code2.equals(RONONCE)){
				jcc.SetCmdContent(ConstantValue.RONONCE);
				returnRusult(resp,"1");
			}
			else if(code2.equals(MUSIC)){
				jcc.SetCmdContent(ConstantValue.MUSIC);
				returnRusult(resp,"1");
			}
			
			else if(code2.equals(COLSEMUSIC)){
				jcc.SetCmdContent(ConstantValue.COLSEMUSIC);
				returnRusult(resp,"1");
			}
			else if(code2.equals(ERROR)){
				returnRusult(resp,"0");
			}
			else{
				returnRusult(resp,"0");
			}
		}
		
		System.out.println("有访问");
	}
	/**
	 * 返回的结果信息
	 * @param resp
	 */
	private void returnRusult(HttpServletResponse resp,String result) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		JSONObject object = new JSONObject(map);
		String JSON = object.toString();
		System.out.println(JSON);
		CommonUtil.renderJson(resp, JSON);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
		
	}
}
