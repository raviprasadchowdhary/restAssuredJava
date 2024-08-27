package specBuilderTest;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojoAddPlace.AddPlaceRequest;
import pojoAddPlace.Location;

public class GoogleApiTest {
	public static void main(String[] args) {

		/*
		 * ************************************************************************************************
		 * Comment
		 * ************************************************************************************************
		*/
		
		AddPlaceRequest addPlaceRequest = new AddPlaceRequest();
		addPlaceRequest.setAccuracy(50);
		addPlaceRequest.setAddress("29, side layout, cohen 09");
		addPlaceRequest.setLanguage("French-IN");
		addPlaceRequest.setName("Frontline house3");
		addPlaceRequest.setPhone_number("(+91) 983 893 3937");
		addPlaceRequest.setWebsite("http://google.com");
		
		Location location = new Location();
		location.setLat(-38.383494d);
		location.setLng(33.427362d);
		addPlaceRequest.setLocation(location);
		
		List<String> types = new ArrayList<String>();
		types.add("shoe park");
		types.add("shoe park");
		
		addPlaceRequest.setTypes(types);
		
		RequestSpecification requestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();
		ResponseSpecification responseSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		RequestSpecification requestGiven = given().log().all().spec(requestSpec).body(addPlaceRequest);
		Response response = requestGiven.when().log().all().post("maps/api/place/add/json")
							.then().log().all().spec(responseSpec).extract().response();
		
		String addPlaceResponse = response.asString();
		
		System.out.println(addPlaceResponse);
		
	}
}
