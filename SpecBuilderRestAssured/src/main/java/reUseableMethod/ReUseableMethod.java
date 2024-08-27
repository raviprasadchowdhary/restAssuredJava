package reUseableMethod;

import io.restassured.path.json.JsonPath;

public class ReUseableMethod {
	public static JsonPath stringToJsonPath(String input) {
		JsonPath output = new JsonPath(input);
		return output;
	}
}
