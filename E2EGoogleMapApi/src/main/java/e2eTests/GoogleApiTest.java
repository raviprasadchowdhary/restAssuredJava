package e2eTests;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojoAddPlace.DeletePlaceRequest;
import pojoRequest.AddPlaceRequest;
import pojoRequest.Location;
import pojoRequest.UpdatePlaceRequest;
import pojoResponse.GetPlaceResponse;
import reUseableMethod.ReUseableMethod;

public class GoogleApiTest {
	public static void main(String[] args) {

		/*
		 * ************************************************************************************************
		 * Add place
		 * ************************************************************************************************
		*/
		
		int addPlaceAccuracy = 50;
		String addPlaceAddress = "29, side layout, cohen 09";
		String addPlaceLanguage = "French-IN";
		String addPlaceName = "Frontline house3";
		String addPlacePhone = "(+91) 983 893 3937";
		String addPlaceWebsite = "http://google.com";
		double addPlaceLat = -38.383494d;
		double addPlaceLng = -33.427362d;
		String addPlaceTypes1 = "shoe park";
		String addPlaceTypes2 = "shop";
		
		AddPlaceRequest addPlaceRequest = new AddPlaceRequest();
		addPlaceRequest.setAccuracy(addPlaceAccuracy);
		addPlaceRequest.setAddress(addPlaceAddress);
		addPlaceRequest.setLanguage(addPlaceLanguage);
		addPlaceRequest.setName(addPlaceName);
		addPlaceRequest.setPhone_number(addPlacePhone);
		addPlaceRequest.setWebsite(addPlaceWebsite);
		
		Location location = new Location();
		location.setLat(addPlaceLat);
		location.setLng(addPlaceLng);
		addPlaceRequest.setLocation(location);
		
		List<String> types = new ArrayList<String>();
		types.add(addPlaceTypes1);
		types.add(addPlaceTypes2);
		
		addPlaceRequest.setTypes(types);
		
		RequestSpecification requestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();
		ResponseSpecification responseSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		RequestSpecification requestGiven = given().log().all().spec(requestSpec).body(addPlaceRequest);
		Response response = requestGiven.when().log().all().post("maps/api/place/add/json")
							.then().log().all().spec(responseSpec).extract().response();
		
		String addPlaceResponse = response.asString();
		
		/*
		 * ************************************************************************************************
		 * Get Place and verify it is as added
		 * ************************************************************************************************
		*/
		
		JsonPath addPlaceResponseJs = ReUseableMethod.stringToJsonPath(addPlaceResponse);
		String placeId = addPlaceResponseJs.get("place_id");
		
		RequestSpecification getRequestSpec = given().log().all().spec(requestSpec).queryParam("place_id", placeId);
		Response getPlaceResponse = getRequestSpec.when().log().all().get("maps/api/place/get/json").then()
				.log().all().spec(responseSpec).extract().response();
		
		GetPlaceResponse getPlaceResponseObject = getPlaceResponse.as(GetPlaceResponse.class);
		
		Assert.assertEquals(getPlaceResponseObject.getName(), addPlaceName);
		Assert.assertEquals(getPlaceResponseObject.getPhone_number(), addPlacePhone);
		Assert.assertEquals(getPlaceResponseObject.getAddress(), addPlaceAddress);
		Assert.assertEquals(getPlaceResponseObject.getWebsite(), addPlaceWebsite);
		Assert.assertEquals(getPlaceResponseObject.getLanguage(), addPlaceLanguage);
		Assert.assertEquals(getPlaceResponseObject.getLocation().getLatitude(), String.valueOf(addPlaceLat));
		Assert.assertEquals(getPlaceResponseObject.getLocation().getLongitude(), String.valueOf(addPlaceLng));
		Assert.assertEquals(getPlaceResponseObject.getTypes(), addPlaceTypes1 + "," + addPlaceTypes2);
		Assert.assertEquals(getPlaceResponseObject.getAccuracy(), String.valueOf(addPlaceAccuracy));
		
		/*
		 * ************************************************************************************************
		 * Update place
		 * ************************************************************************************************
		*/
		
		String updatedAddress = "401 Shannon Way";
		String key = "qaclick123";
		
		UpdatePlaceRequest updatePlaceRequest = new UpdatePlaceRequest();
		updatePlaceRequest.setAddress(updatedAddress);
		updatePlaceRequest.setPlace_id(placeId);
		updatePlaceRequest.setKey(key);
		RequestSpecification updateRequestSpec = given().log().all().spec(requestSpec).queryParam("place_id", placeId).body(updatePlaceRequest);
		Response updateAddressResponse = updateRequestSpec.when().log().all().put("maps/api/place/update/json").then().log().all().extract().response();
		
		
		/*
		 * ************************************************************************************************
		 * Get Place and verify it is as updated
		 * ************************************************************************************************
		*/
		
		RequestSpecification getRequestSpec1 = given().log().all().spec(requestSpec).queryParam("place_id", placeId);
		Response getPlaceResponse1 = getRequestSpec1.when().log().all().get("maps/api/place/get/json").then()
				.log().all().spec(responseSpec).extract().response();

		GetPlaceResponse getPlaceResponseObject1 = getPlaceResponse1.as(GetPlaceResponse.class);
		
		Assert.assertEquals(getPlaceResponseObject1.getName(), addPlaceName);
		Assert.assertEquals(getPlaceResponseObject1.getPhone_number(), addPlacePhone);
		Assert.assertEquals(getPlaceResponseObject1.getAddress(), updatedAddress);
		Assert.assertEquals(getPlaceResponseObject1.getWebsite(), addPlaceWebsite);
		Assert.assertEquals(getPlaceResponseObject1.getLanguage(), addPlaceLanguage);
		Assert.assertEquals(getPlaceResponseObject1.getLocation().getLatitude(), String.valueOf(addPlaceLat));
		Assert.assertEquals(getPlaceResponseObject1.getLocation().getLongitude(), String.valueOf(addPlaceLng));
		Assert.assertEquals(getPlaceResponseObject1.getTypes(), addPlaceTypes1 + "," + addPlaceTypes2);
		Assert.assertEquals(getPlaceResponseObject1.getAccuracy(), String.valueOf(addPlaceAccuracy));
		
		/*
		 * ************************************************************************************************
		 * Delete Place
		 * ************************************************************************************************
		*/
		
		DeletePlaceRequest deletePlaceRequest = new DeletePlaceRequest();
		deletePlaceRequest.setPlace_id(placeId);
		RequestSpecification deleteRequestSpec = given().log().all().spec(requestSpec).body(deletePlaceRequest);
		Response deleteRequestResponse = deleteRequestSpec.when().delete("maps/api/place/delete/json").then().log().all().spec(responseSpec).extract().response();
		
		/*
		 * ************************************************************************************************
		 * Get Place and verify it is deleted
		 * ************************************************************************************************
		*/
		
		RequestSpecification getRequestSpec11 = given().log().all().spec(requestSpec).queryParam("place_id", placeId);
		Response getPlaceResponse11 = getRequestSpec11.when().log().all().get("maps/api/place/get/json").then()
				.log().all().assertThat().statusCode(404).extract().response();
		
	}
}
