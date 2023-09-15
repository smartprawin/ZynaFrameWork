package apiHelper;

import java.util.Map;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiHelper {

	private RequestSpecification request;
	private String token ;
	private ExtentTest test;
	
	public ApiHelper getBaseUrl(String url, ExtentTest test) {
		RestAssured.baseURI = url;
		this.test = test;
		this.request = RestAssured.given();
		this.test.log(Status.INFO, "Given Url is "+url);
		return this;
	}
	
	public ApiHelper addHeader(String param) {
		request
		.header("Authorization", param).contentType("application/json");
		test.log(Status.INFO, "Added the header token "+param);
		return this;
	}

	public ApiHelper addBody(Map<String, String> requestParams) {
		request.formParams(requestParams);
		test.generateLog(Status.INFO, MarkupHelper.toTable(requestParams, "requestParam"));
		return this;
	}
	
	public ApiHelper addBody(String key, String keyValue) {
		request.formParam(key, keyValue);
		test.log(Status.INFO, "Added the param for " + key +" key and value as "+ keyValue);
		return this;
	}

	public Response postRequest(String endPoint) {
		test.log(Status.INFO, "triggering a POST request for endpoint "+endPoint);
		return request.post(endPoint);
	}
	
	public Response patchRequest(String endPoint) {
		test.log(Status.INFO, "triggering a PATCH request for endpoint "+endPoint);
		return request.patch(endPoint);
	}
	
	public Response putRequest(String endPoint) {
		test.log(Status.INFO, "triggering a PUT request for endpoint "+endPoint);
		return request.put(endPoint);
	}
	
	public Response getRequest(String endPoint) {
		test.log(Status.INFO, "triggering a GET request for endpoint "+endPoint);
		return request.get(endPoint);
	}

	public String getToken() {
		return token;
	}
}
