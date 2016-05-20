package sk.ba.novak.bo;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import sk.ba.novak.dao.SessionHistoryDAO;
import sk.ba.novak.dao.UserDAO;
import sk.ba.novak.db.entity.UserEntity;
import sk.ba.novak.password.PasswordEncrypter;
import sk.ba.novak.rest.entity.SessionHistory;

@Stateless(name = "sessionHistoryBO")
public class SessionHistoryBO {

	@EJB
	private SessionHistoryDAO sessionHistoryDAO;
	@EJB
	private UserDAO userDAO;

	public Long checkValidCredentials(String login, String password){
		UserEntity user = userDAO.getUserByLogin(login);
		if (user == null)
			return null;
		return PasswordEncrypter.checkPassws(password, user.getPassword()) ? user.getId() : -1L;
	}
	
	public boolean checkSessionValidityAndUpdate(String uuid) {
		return sessionHistoryDAO.checkSessionValidityAndUpdate(uuid);
	}

	public SessionHistory login(Long userId) {
		return createSessionHistory(userId);
	}
	
	public SessionHistory createSessionHistory(Long userId) {
		SessionHistory sessionHistory = new SessionHistory();
		UserEntity user = userDAO.getUser(userId);
		String genUid = sessionHistoryDAO.createSessionHistory(user);
		if (genUid == null)
			return null;
		sessionHistory.setUuid(genUid);
		return sessionHistory;
	}
	
	public Long getUserIdFromSession(String uuid) {
		return sessionHistoryDAO.getUserIdFromSession(uuid);
	}

}
