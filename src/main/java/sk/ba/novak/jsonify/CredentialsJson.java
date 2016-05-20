package sk.ba.novak.jsonify;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import sk.ba.novak.rest.entity.Credentials;


public class CredentialsJson {
	
	Gson gson = new Gson();

	public String toJson(Credentials credentials) {
		String jsonRepresentation = gson.toJson(credentials);
		return jsonRepresentation;
	}
	
	public Credentials fromJson(JsonObject json){
		Credentials credentials = gson.fromJson(json, Credentials.class);
		return credentials;
	}
	
}
