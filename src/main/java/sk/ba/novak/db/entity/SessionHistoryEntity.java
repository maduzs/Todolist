package sk.ba.novak.db.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "SessionHistory")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "SessionHistoryEntity.findByUuid", query = "SELECT s FROM SessionHistoryEntity s WHERE s.uuid = :uuid "
				+ "AND :currentDate BETWEEN s.validFrom AND s.validUntil"),
		@NamedQuery(name = "SessionHistoryEntity.getUserId", query = "SELECT s.userSession FROM SessionHistoryEntity s WHERE s.uuid = :uuid "
				+ "AND :currentDate BETWEEN s.validFrom AND s.validUntil"),
		@NamedQuery(name = "SessionHistoryEntity.update", query = "UPDATE SessionHistoryEntity "
				+ "SET validUntil = :untilDate" + " WHERE uuid = :uuid "
				+ "AND :currentDate BETWEEN validFrom AND validUntil") })

public class SessionHistoryEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "uuid", length = 16)
	private String uuid;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "userSession", referencedColumnName = "userId")
	private UserEntity userSession;
	
	@Column(name = "validFrom")
	private Date validFrom;

	@Column(name = "validUntil")
	private Date validUntil;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public UserEntity getUserSession() {
		return userSession;
	}

	public void setUserSession(UserEntity userSession) {
		this.userSession = userSession;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

}
