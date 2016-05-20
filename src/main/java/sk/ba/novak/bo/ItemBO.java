package sk.ba.novak.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import sk.ba.novak.conf.ApplicationData;
import sk.ba.novak.convertor.ItemConvertor;
import sk.ba.novak.dao.ItemDAO;
import sk.ba.novak.dao.UserDAO;
import sk.ba.novak.db.entity.ItemEntity;
import sk.ba.novak.db.entity.UserEntity;
import sk.ba.novak.rest.entity.Item;

@Stateless(name = "itemBO")
public class ItemBO {
	
	static Logger logger = Logger.getLogger(ApplicationData.loggerName);
	private String method = "";
	
	@EJB
	private ItemDAO itemDAO;
	@EJB
	private UserDAO userDAO;
	@EJB
	private ItemConvertor itemConvertor;
	
	public List<Item> getItemsForUser(Long userId){
		List<ItemEntity> entities = itemDAO.getItemsForUser(userId);
		return convertEntityListToRest(entities);
	}
	
	public Item saveItem(Long userId, Item item){ 
		method = "ItemBO.saveItem";
		try{
			UserEntity user = userDAO.getUser(userId);
			ItemEntity entity = itemConvertor.fromRestToEntityNew(item, user);
			itemDAO.saveItem(entity, false);
			return itemConvertor.fromEntityToRest(entity);
		}
		catch (Exception ex){
			logger.error(method + " " + ex);
			return null;
		}
	}
	
	public Item updateItem(Item item){
		method = "ItemBO.updateItem";
		try{
			ItemEntity entity = itemDAO.getItem(item.getId());
			entity.setContent(item.getContent());
			entity.setCompleted(item.isCompleted());
			itemDAO.saveItem(entity, true);
			return itemConvertor.fromEntityToRest(entity);
		}
		catch (Exception ex){
			logger.error(method + " " + ex);
			return null;
		}
	}
	
	public boolean deleteItem(Item item){
		method = "ItemBO.deleteItem";
		try{
			ItemEntity entity = itemDAO.getItem(item.getId());
			entity.setValidUntil(new Date());
			itemDAO.saveItem(entity, true);
			return true;
		}
		catch (Exception ex){
			logger.error(method + " " + ex);
			return false;
		}
	}
	
	private List<Item> convertEntityListToRest(List<ItemEntity> entities) {
		method = "ItemBO.convertEntityListToRest";
		List<Item> items = new ArrayList<Item>();
		try {
			for (int i = 0; i < entities.size(); i++) {
				items.add(itemConvertor.fromEntityToRest(entities.get(i)));
			}
		} catch (Exception ex) {
			logger.error(method + " " + ex);
			return null;
		}
		return items;
	}
}
