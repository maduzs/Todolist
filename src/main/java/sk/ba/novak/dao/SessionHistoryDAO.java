package sk.ba.novak.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.ejb.Stateless;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.logging.Logger;

import sk.ba.novak.conf.ApplicationData;
import sk.ba.novak.db.entity.SessionHistoryEntity;
import sk.ba.novak.db.entity.UserEntity;

@Stateless(name = "sessionHistoryDAO")
public class SessionHistoryDAO {

	static Logger logger = Logger.getLogger(ApplicationData.loggerName);
	private String method = "";

	public boolean checkSessionValidityAndUpdate(String uuid) {
		method = "SessionHistoryDAO.selectByUuid";
		Transaction tx = null;
		Query query;
		SessionHistoryEntity queryResult = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		logger.trace(method + "begin ");
		try {
			tx = session.beginTransaction();

			query = session.getNamedQuery("SessionHistoryEntity.findByUuid").setParameter("currentDate", new Date())
					.setParameter("uuid", uuid);
			queryResult = (SessionHistoryEntity) query.uniqueResult();

			tx.commit();
			session.close();
			if (queryResult != null){
				logger.info(method + " success ");
				// update
				return updateSessionHistory(queryResult);
			}
		} catch (Exception ex) {
			logger.error(method + " failed ");
			logger.error(method + ex);
			if (tx != null)
				tx.rollback();
			session.close();
			return false;
		}
		logger.warn(method + " no valid session found! ");
		return false;
	}

	public String createSessionHistory(UserEntity user) {
		method = "SessionHistoryDAO.createSessionHistory";
		long LOWER_RANGE = 1000000000000000L;
		long UPPER_RANGE = 9999999999999999L;
		Random random = new Random();

		long randomValue = LOWER_RANGE + (long) (random.nextDouble() * (UPPER_RANGE - LOWER_RANGE));

		SessionHistoryEntity sessionHistoryEntity = new SessionHistoryEntity();
		sessionHistoryEntity.setUuid(String.valueOf(randomValue));
		sessionHistoryEntity.setUserSession(user);

		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, 1);

		sessionHistoryEntity.setValidFrom(date);
		sessionHistoryEntity.setValidUntil(cal.getTime());

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		logger.trace(method + "begin ");
		try {
			tx = session.beginTransaction();

			session.save(sessionHistoryEntity);

			tx.commit();
			session.close();

			logger.info(method + " success ");
			return String.valueOf(randomValue);

		} catch (Exception ex) {
			logger.error(method + " failed ");
			logger.error(method + ex);
			if (tx != null)
				tx.rollback();
			session.close();
			return null;
		}
	}
	
	public boolean updateSessionHistory(SessionHistoryEntity sessionHistoryEntity) {
		method = "SessionHistoryDAO.updateSessionHistory";
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, 1);
		
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query;
		
		logger.trace(method + "begin ");
		try {
			tx = session.beginTransaction();
		
			query = session.getNamedQuery("SessionHistoryEntity.update")
					.setParameter("uuid", sessionHistoryEntity.getUuid())
					.setParameter("currentDate", new Date())
					.setParameter("untilDate", cal.getTime());
			int queryResult = query.executeUpdate();

			tx.commit();
			session.close();
			if (queryResult <= 0){
				logger.warn(method + " failed to update SessionHistory ");
				return false;
			}
			logger.info(method + " success ");
			return true;

		} catch (Exception ex) {
			logger.error(method + " failed ");
			logger.error(method + ex);
			if (tx != null)
				tx.rollback();
			session.close();
			return false;
		}
	}
	
	/**
	 * gets the user from session
	 *
	 * @param uuid  the uuid from session
	 * @return the UserEntity from sessionHistory
	 */
	public Long getUserIdFromSession(String uuid) {
		method = "SessionHistoryDAO.getUserIdFromSession";
		
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query;

		UserEntity queryResult = null;
		
		logger.trace(method + "begin ");
		try {
			tx = session.beginTransaction();
			
			query = session.getNamedQuery("SessionHistoryEntity.getUserId")
					.setParameter("uuid", uuid)
					.setParameter("currentDate", new Date());
			
			queryResult = (UserEntity) query.uniqueResult();

			tx.commit();
			session.close();

			logger.info(method + " success");
			return queryResult.getId();

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
