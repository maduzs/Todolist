package sk.ba.novak.convertor;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.Stateless;

import sk.ba.novak.db.entity.ItemEntity;
import sk.ba.novak.db.entity.UserEntity;
import sk.ba.novak.rest.entity.Item;

@Stateless(name = "itemConvertor")
public class ItemConvertor {

	public Item fromEntityToRest(ItemEntity entity) throws NullPointerException{
		Item item= new Item();
		item.setId(entity.getId());
		item.setContent(entity.getContent());
		item.setCompleted(entity.isCompleted());
		item.setCreatedOn(entity.getValidFrom());
		return item;
	}

	public ItemEntity fromRestToEntityNew(Item item, UserEntity user) {
		ItemEntity entity = new ItemEntity();
		entity.setContent(item.getContent());
		entity.setCompleted(false);
		entity.setOwner(user);

		Date date = new Date();

		entity.setValidFrom(date);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, 900);
		
		entity.setValidUntil(cal.getTime());
		
		return entity;
	}
}
