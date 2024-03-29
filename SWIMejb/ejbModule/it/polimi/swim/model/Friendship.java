package it.polimi.swim.model;

import it.polimi.swim.enums.RequestState;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * This class define Friendship entity bean
 * 
 * @author Arcidiacono Fabio, Bielli Stefano
 * 
 */
@Entity
@Table(name = "friendship")
public class Friendship implements Serializable {

	private static final long serialVersionUID = 8018447569861394482L;

	@Id
	@SequenceGenerator(name = "Sequence", sequenceName = "SEQ_CUSTOM_WD")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Sequence")
	@Column(name = "ID")
	private int id;

	@OneToOne
	@JoinColumn(name = "friendID", nullable = false)
	private User friend;

	@OneToOne
	@JoinColumn(name = "userID", nullable = false)
	private User user;

	@Column(name = "state", nullable = false)
	@Enumerated(EnumType.STRING)
	private RequestState state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getFriend() {
		return friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public RequestState getState() {
		return state;
	}

	public void setState(RequestState state) {
		this.state = state;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Friendship)) {
			return false;
		}
		Friendship other = (Friendship) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

}
