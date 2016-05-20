package sk.ba.novak.dao;

import javax.ejb.Stateless;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.logging.Logger;

import sk.ba.novak.conf.ApplicationData;
import sk.ba.novak.db.entity.UserEntity;

@Stateless(name = "userDao")
public class UserDAO {

	static Logger logger = Logger.getLogger(ApplicationData.loggerName);
	private String method = "";
	
	public UserEntity getUser(Long user_id){
		method = "UserDAO.getUser";
		
		UserEntity result = null;

		logger.trace(method + " begin");

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {

			tx = session.beginTransaction();
			session = HibernateUtil.getSessionFactory().openSession();
			result =  (UserEntity) session.get(UserEntity.class, user_id);
            
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
	
	public UserEntity getUserByLogin(String login){
		method = "UserDAO.getUserByLogin";
		
		UserEntity queryResult = null;

		logger.trace(method + " begin");

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query;

		try {
			tx = session.beginTransaction();
			query = session.getNamedQuery("UserEntity.findByCredentials").setParameter("login", login);
			queryResult = (UserEntity) query.uniqueResult();

			tx.commit();
			session.close();

			logger.info(method + " success");
			return queryResult;

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
