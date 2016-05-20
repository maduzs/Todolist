package sk.ba.novak.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.logging.Logger;

import sk.ba.novak.conf.ApplicationData;
import sk.ba.novak.db.entity.ItemEntity;

@Stateless(name = "itemDAO")
public class ItemDAO {

	static Logger logger = Logger.getLogger(ApplicationData.loggerName);
	private String method = "";

	public List<ItemEntity> getItemsForUser(Long userId){
		method = "ItemDAO.getItemsForUser";
		
		List<?> queryResult = null;

		logger.trace(method + " begin");

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query;

		try {
			tx = session.beginTransaction();
			query = session.getNamedQuery("ItemEntity.findAllForUser")
					.setParameter("ownerId", userId)
					.setParameter("currentDate", new Date());
			queryResult = query.list();

			List<ItemEntity> entities = (List<ItemEntity>) (List<?>) queryResult;
			
			tx.commit();
			session.close();

			logger.info(method + " success");
			return entities;

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			session.close();
			logger.error(method + " failed");
			logger.error(method + e);
			return null;
		}
	}

	public ItemEntity saveItem(ItemEntity itemEntity, boolean update) {
		method = "ItemDAO.saveItem";
		logger.trace(method + " begin");
		Transaction tx = null;
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().openSession();
			
			tx = session.beginTransaction();
			if (!update)
				session.save(itemEntity);
			else
				session.update(itemEntity);

			tx.commit();
			session.close();
			logger.info(method + " success");
			
			return itemEntity;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			session.close();
			logger.error(method + " failed");
			logger.error(method + " " + e);
			return null;
		}
	}

	public ItemEntity getItem(long item_id) {
		method = "ItemDAO.getItem";
		
		ItemEntity result = null;

		logger.trace(method + " begin");

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {

			tx = session.beginTransaction();
			session = HibernateUtil.getSessionFactory().openSession();
			result =  (ItemEntity) session.get(ItemEntity.class, item_id);
            
			tx.commit();
			session.close();

			logger.info(method + " success");
			return result;

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			session.close();
			logger.error(method + " failed");
			logger.error(method + e);
			return null;
		}
	}
}
