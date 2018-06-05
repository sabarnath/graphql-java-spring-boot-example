package com.example.DemoGraphQL;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.DemoGraphQL.model.Author;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoGraphQlApplicationTests {

	@Test
	public void invokeGraphQL() {

		//graphQL server endpoint
		URL graphqlEndpoint = null;
		try {
			graphqlEndpoint = new URL("http://localhost:8090/graphql");
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException thrown with the graphQL endpoint");
		}

		//headers can be added if needed
		Map<String, String> headers = new HashMap<String, String>();

		//graphQL query
		String query = "{\n" + "findAllAuthors {\n" + "id\n" + "firstName\n" + "lastName\n" + "}\n" + "}";

		//mapper to map the graphQL query response json to a list 
		Function<JsonObject, List<Author>> funcMapper = (JsonObject jsonObject) -> {
			List<Author> authors = new ArrayList<Author>();
			JsonElement jsonElement = jsonObject.get("findAllAuthors");
			JsonArray jsonArray = jsonElement.getAsJsonArray();
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonElement arrayElement = jsonArray.get(i);
				if (arrayElement.isJsonObject()) {
					JsonObject arrayObject = arrayElement.getAsJsonObject();
					Author author = new Author();
					author.setId(arrayObject.get("id").getAsLong());
					author.setFirstName(arrayObject.get("firstName").getAsString());
					author.setLastName(arrayObject.get("lastName").getAsString());
					authors.add(author);
				}
			}
			return authors;
		};

		//object holds the query and any variables needed
		JsonObject jsonEntity = new JsonObject();
		jsonEntity.addProperty("query", query);
		jsonEntity.add("variables", new JsonObject());

		//HttpClient to call the graphQL endpoint
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(graphqlEndpoint.toString());
		for (Map.Entry<String, String> header : headers.entrySet()) {

			httppost.addHeader(header.getKey(), header.getValue());
		}

		StringEntity entity = new StringEntity(jsonEntity.toString(), ContentType.APPLICATION_JSON);
		httppost.setEntity(entity);

		// Execute and get the response.
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			System.out.println("ClientProtocolException thrown while calling httpclient.execute");
		} catch (IOException e) {
			System.out.println("IOException thrown while calling httpclient.execute");
		}

		InputStream contentStream = null;
		try {
			contentStream = response.getEntity().getContent();
		} catch (UnsupportedOperationException e) {
			System.out.println("UnsupportedOperationException thrown while calling response.getEntity().getContent()");
		} catch (IOException e) {
			System.out.println("IOException thrown while calling response.getEntity().getContent()");
		}

		String contentString = null;
		try {
			contentString = IOUtils.toString(contentStream, "UTF-8");
		} catch (IOException e) {
			System.out.println("IOException thrown while calling IOUtils.toString(contentStream, \"UTF-8\")");
		}
		
		if (response.getStatusLine().getStatusCode() != 200) {
			System.out.println("The server responded with" + contentString + " and statuscode "
					+ response.getStatusLine().getStatusCode());
		}

		JsonObject responseJson = (JsonObject) (new JsonParser()).parse(contentString);

		
		//check for errors
		if (responseJson.has("error")) {
			System.out.println("Exception occured: " + responseJson.get("error").getAsString());
		}
		if (responseJson.has("errors")) {
			String message = "";
			for (JsonElement error : responseJson.get("errors").getAsJsonArray()) {
				message += error.toString();
			}
			System.out.println("Exception occured: " + message);
		}		

		//apply the mapper to the response
		List<Author> authorsList = funcMapper.apply(responseJson.get("data").getAsJsonObject());

		//Print the response
		for (Author author : authorsList) {
			System.out.println(author);
		}
	}

}
