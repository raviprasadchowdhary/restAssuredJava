package googleApiTest;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import pojoAddPlace.AddPlaceRequest;
import pojoAddPlace.Location;
import reUseableMethod.ReUseableMethod;
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class GoogleApiTest {
	public static void main(String[] args) {

		/*
		 * ************************************************************************************************
		 * Comment
		 * ************************************************************************************************
		*/
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
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
		
		String addPlaceResponse = given().log().all()
		.queryParam("key", "qaclick123")
		.body(addPlaceRequest)
		.when().log().all()
		.post("maps/api/place/add/json")
		.then().log().all()
		.assertThat().statusCode(200).extract().response().asString()
		;
		
		System.out.println(addPlaceResponse);
		
	}
}
