package com.Mercy.API;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.Test;

import com.Mercy.Base.BaseClass;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import apiHelper.HttpRequestHelper;
import utility.Constants;

public class rapidAPI extends BaseClass {


	public rapidAPI() {
		testName = "rapidAPI";
	}

	@Test
	public void Countries() throws Throwable {
		node = test.createNode(testName+"_Countries");
		new HttpRequestHelper(Constants.API_URI+"/countries",node)
				.header("X-RapidAPI-Key", Constants.RapidAPI_KEY)
				.header("X-RapidAPI-Host", Constants.RapidAPI_HOST)
				.method(Constants.RapidAPI_METHOD)
				.getResponse()
				.assertStatusCode(200);
	}

	@Test
	public void Statistics() throws Throwable {
		node = test.createNode(testName+"_Statistics");
		new HttpRequestHelper(Constants.API_URI+"/statistics",node)
				.header("X-RapidAPI-Key", Constants.RapidAPI_KEY)
				.header("X-RapidAPI-Host", Constants.RapidAPI_HOST)
				.method(Constants.RapidAPI_METHOD)
				.getResponse()
				.assertStatusCode(200);
	}

	@Test
	public void History() throws Throwable {
		node = test.createNode(testName+"_History");
		String body = new HttpRequestHelper(Constants.API_URI+"/history?country=usa&day=2020-06-02",node)
				.header("X-RapidAPI-Key", Constants.RapidAPI_KEY)
				.header("X-RapidAPI-Host", Constants.RapidAPI_HOST)
				.method(Constants.RapidAPI_METHOD)
				.getResponse()
				.assertStatusCode(200)
				.getBody();
		node.info(MarkupHelper.createCodeBlock(body, CodeLanguage.JSON));
		
	}
	
	public JSONObject JsonObj(String JsonResponse) throws Throwable {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(JsonResponse);
		return (JSONObject) obj;
	}
	
	public JSONArray JsonNestArray(String JsonResponse) throws Throwable {
		JSONObject jsonObj = JsonObj(JsonResponse);
		Object object = jsonObj.get("response");
		return (JSONArray) object;
	}
	
	public JSONArray JsonArray(String JsonResponse) throws Throwable {
		JSONArray jsonArray = JsonNestArray(JsonResponse);
		return (JSONArray) jsonArray.get(1);
		
	}
	
	public  Object JsonParse(String JsonResponse) throws Throwable {
		JSONArray jsonArray = JsonArray(JsonResponse);
		return jsonArray.get(1);
		
	}

}
