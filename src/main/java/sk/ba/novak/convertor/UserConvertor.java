package sk.ba.novak.convertor;

import javax.ejb.Stateless;

import sk.ba.novak.db.entity.UserEntity;
import sk.ba.novak.rest.entity.User;

/* Converts the User rest entity to User database entity and vice versa*/
@Stateless(name = "userConvertor")
public class UserConvertor {

	public User fromEntityToRest(UserEntity entity) throws Exception{
		User user= new User();
		user.setId(entity.getId());
		return user;
	}
	
	public UserEntity fromRestToEntity(User user) throws Exception{
		UserEntity entity = new UserEntity();
		entity.setId(user.getId());
		return entity;
	}
	
}
