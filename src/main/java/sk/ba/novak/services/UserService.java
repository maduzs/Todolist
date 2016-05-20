package sk.ba.novak.services;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import sk.ba.novak.bo.SessionHistoryBO;
import sk.ba.novak.jsonify.CredentialsJson;
import sk.ba.novak.rest.entity.Credentials;
import sk.ba.novak.rest.entity.SessionHistory;

@Path("/users")
public class UserService {

	private CredentialsJson jsonify = new CredentialsJson();
	
	@EJB
	private SessionHistoryBO sessionHistoryBO;
	
	private final String credentialsElement = "user";
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginUser(String msg) {
		try {
			JsonElement jelement = new JsonParser().parse(msg);
			JsonObject jobject = jelement.getAsJsonObject();
			jobject = jobject.getAsJsonObject(credentialsElement);
			Credentials credentials = jsonify.fromJson(jobject);
			Long userId = sessionHistoryBO.checkValidCredentials(credentials.getLogin(), credentials.getPassword());
			if (userId != null && userId > 0){
				SessionHistory sH = sessionHistoryBO.login(userId);
				if (sH != null)
					return Response.status(200).entity(sH).build();
			}
			else
				return Response.status(401).entity(msg).build();
		} catch (Exception e) {
			e.printStackTrace();
			Response.status(500).entity(msg).build();
		}
		return Response.status(500).entity(msg).build();
	}
}
