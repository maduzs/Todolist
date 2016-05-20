package sk.ba.novak.jsonify;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import sk.ba.novak.rest.entity.Item;

public class ItemJson {

	Gson gson = new Gson();

	public String toJson(Item item) {
		String jsonRepresentation = gson.toJson(item);
		return jsonRepresentation;
	}
	
	public Item fromJson(JsonObject json){
		Item credentials = gson.fromJson(json, Item.class);
		return credentials;
	}
}
