package oAuthTest;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCoursesResponse;
import pojo.WebAutomation;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

public class OAuthTest {

	public static void main(String[] args) {

		/*
		 * ************************************************************************************************
		 * Generate OAuth token by hitting the end point "oauthapi/oauth2/resourceOwner/token"
		 * ************************************************************************************************
		*/		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		String oAuthTokenResponse = given().log().all()
		.multiPart("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.multiPart("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.multiPart("grant_type", "client_credentials")
		.multiPart("scope", "trust")
		.when().log().all()
		.post("oauthapi/oauth2/resourceOwner/token")
		.then().log().all()
		.assertThat().statusCode(200)
		.extract().response().asString()
		;
		
		JsonPath jsonOAuthTokenResponse = new JsonPath(oAuthTokenResponse);
		String accessToken = jsonOAuthTokenResponse.getString("access_token");
		
		/*
		 * ************************************************************************************************
		 * get the course details by hitting "oauthapi/getCourseDetails"
		 * ************************************************************************************************
		*/
		GetCoursesResponse getCourseDetailsResponse = given().log().all()
				.queryParam("access_token", accessToken)
				.when().log().all()
				.get("oauthapi/getCourseDetails")
				.then().log().all()
				.extract().response().as(GetCoursesResponse.class)
				;
		
		/*
		 * ************************************************************************************************
		 * print the price of "Cypress" course in webAutomation object of response
		 * ************************************************************************************************
		*/
		for(int i=0; i<getCourseDetailsResponse.getCourses().getWebAutomation().size(); i++) {
			if(getCourseDetailsResponse.getCourses().getWebAutomation().get(i).getCourseTitle().equalsIgnoreCase("Cypress")) {
				System.out.println("Cypress course price is $" + getCourseDetailsResponse.getCourses().getWebAutomation().get(i).getPrice());
			}
		}
		
		/*
		 * ************************************************************************************************
		 * get the list of courses in api object and compare it with the expected
		 * ************************************************************************************************
		*/

		List<Api> apiCoursesActualList = getCourseDetailsResponse.getCourses().getApi();

		String[] apiCoursesExpectedArray = { "Rest Assured Automation using Java", "SoapUI Webservices testing" };
		List<String> apiCoursesExpected = Arrays.asList(apiCoursesExpectedArray);
		
		ArrayList<String> apiCoursesActual = new ArrayList<String>();
		for(int i=0; i<apiCoursesActualList.size(); i++) {
			apiCoursesActual.add(apiCoursesActualList.get(i).getCourseTitle());
		}
		
		Assert.assertTrue(apiCoursesExpected.equals(apiCoursesActual));
		
		/*
		 * ************************************************************************************************
		 * get the list of courses in web automation object and compare it with the expected
		 * ************************************************************************************************
		*/
		
		List<WebAutomation> webAutomationCoursesActualList = getCourseDetailsResponse.getCourses().getWebAutomation();

		String [] webAutomationExpectedArray = {"Selenium Webdriver Java", "Cypress", "Protractor"};
		List<String> webAutomationExpected = Arrays.asList(webAutomationExpectedArray);
		
		ArrayList<String> webAutomationCoursesActual = new ArrayList<String>();
		for(int i=0; i<webAutomationCoursesActualList.size(); i++) {
			webAutomationCoursesActual.add(webAutomationCoursesActualList.get(i).getCourseTitle());
		}
		
		Assert.assertTrue(webAutomationCoursesActual.equals(webAutomationExpected));
		
		/*
		 * ************************************************************************************************
		 * get the list of courses in mobile object and compare it with the expected
		 * ************************************************************************************************
		*/
		
		String [] mobileCoursesExpectedArray = {"Appium-Mobile Automation using Java"};
		List<String> mobileCoursesExpected = Arrays.asList(mobileCoursesExpectedArray);
		
		ArrayList<String> mobileCoursesActual = new ArrayList<String>();
		for(int i=0; i<getCourseDetailsResponse.getCourses().getMobile().size(); i++) {
			mobileCoursesActual.add(getCourseDetailsResponse.getCourses().getMobile().get(i).getCourseTitle());
		}
		
		Assert.assertTrue(mobileCoursesExpected.equals(mobileCoursesActual));
	}

}
