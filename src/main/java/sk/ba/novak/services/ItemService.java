package sk.ba.novak.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import sk.ba.novak.bo.ItemBO;
import sk.ba.novak.bo.SessionHistoryBO;
import sk.ba.novak.jsonify.ItemJson;
import sk.ba.novak.rest.entity.Item;

@Path("/item")
public class ItemService {
	
	private ItemJson jsonify = new ItemJson();
	
	private final String itemElement = "newItem";
	@EJB
	private SessionHistoryBO sessionHistoryBO;
	@EJB
	private ItemBO itemBO;
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllItems(String msg, @Context HttpHeaders headers) {
		try {
			String uuid;
			try {
				uuid = headers.getRequestHeader("RestUid").get(0);
			} catch (Exception ex) {
				return Response.status(401).entity(msg).build();
			}
			if (uuid == null || uuid.length() != 16)
				return Response.status(400).entity(msg).build();
			
			if (sessionHistoryBO.checkSessionValidityAndUpdate(uuid)) {
				Long userId = sessionHistoryBO.getUserIdFromSession(uuid);
				if (userId != null && userId > 0){
					List<Item> items = itemBO.getItemsForUser(userId);
					if (items == null)
						return Response.status(500).entity(msg).build();
					return Response.status(200).entity(items).build();
				}
			} else {
				return Response.status(401).entity(msg).build();
			}
		}
		catch (Exception ex) {
			return Response.status(500).entity(msg).build();
		}
		return Response.status(500).entity(msg).build();
	}
	
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertItem(String msg, @Context HttpHeaders headers) {
		try {
			String uuid;
			try {
				uuid = headers.getRequestHeader("RestUid").get(0);
			} catch (Exception ex) {
				return Response.status(401).entity(msg).build();
			}
			if (uuid == null || uuid.length() != 16)
				return Response.status(400).entity(msg).build();
			
			if (sessionHistoryBO.checkSessionValidityAndUpdate(uuid)) {
				JsonElement jelement = new JsonParser().parse(msg);
				JsonObject jobject = jelement.getAsJsonObject();
				jobject = jobject.getAsJsonObject(itemElement);
				Item item = jsonify.fromJson(jobject);
				
				Long userId = sessionHistoryBO.getUserIdFromSession(uuid);
				if (userId != null && userId > 0){
					Item result = itemBO.saveItem(userId, item);
					if (result != null)
						return Response.status(200).entity(result).build();
				}
			} else {
				return Response.status(401).entity(msg).build();
			}
		}
		catch (Exception ex) {
			return Response.status(500).entity(msg).build();
		}
		return Response.status(500).entity(msg).build();
	}
	
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateItem(String msg, @Context HttpHeaders headers) {
		try {
			String uuid;
			try {
				uuid = headers.getRequestHeader("RestUid").get(0);
			} catch (Exception ex) {
				return Response.status(401).entity(msg).build();
			}
			if (uuid == null || uuid.length() != 16)
				return Response.status(400).entity(msg).build();
			
			if (sessionHistoryBO.checkSessionValidityAndUpdate(uuid)) {
				JsonElement jelement = new JsonParser().parse(msg);
				JsonObject jobject = jelement.getAsJsonObject();
				jobject = jobject.getAsJsonObject(itemElement);
				Item item = jsonify.fromJson(jobject);
				
				Long userId = sessionHistoryBO.getUserIdFromSession(uuid);
				if (userId != null && userId > 0){
					Item result = itemBO.updateItem(item);
					if (result != null)
						return Response.status(200).entity(result).build();
				}
			} else {
				return Response.status(401).entity(msg).build();
			}
		}
		catch (Exception ex) {
			return Response.status(500).entity(msg).build();
		}
		return Response.status(500).entity(msg).build();
	}
	
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteItem(String msg, @Context HttpHeaders headers) {
		try {
			String uuid;
			try {
				uuid = headers.getRequestHeader("RestUid").get(0);
			} catch (Exception ex) {
				return Response.status(401).entity(msg).build();
			}
			if (uuid == null || uuid.length() != 16)
				return Response.status(400).entity(msg).build();
			
			if (sessionHistoryBO.checkSessionValidityAndUpdate(uuid)) {
				JsonElement jelement = new JsonParser().parse(msg);
				JsonObject jobject = jelement.getAsJsonObject();
				jobject = jobject.getAsJsonObject(itemElement);
				Item item = jsonify.fromJson(jobject);
				
				Long userId = sessionHistoryBO.getUserIdFromSession(uuid);
				if (userId != null && userId > 0){
					Boolean result = itemBO.deleteItem(item);
					if (result != null)
						return Response.status(200).entity(result).build();
				}
			} else {
				return Response.status(401).entity(msg).build();
			}
		}
		catch (Exception ex) {
			return Response.status(500).entity(msg).build();
		}
		return Response.status(500).entity(msg).build();
	}
}
