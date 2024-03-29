package it.polimi.swim.session;

import it.polimi.swim.model.Ability;
import it.polimi.swim.model.AbilityRequest;
import it.polimi.swim.model.User;
import it.polimi.swim.session.exceptions.AbilityException;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for AbilityManager bean
 * 
 * @author Arcidiacono Fabio, Bielli Stefano
 * 
 */
@Remote
public interface AbilityManagerRemote {

	/**
	 * Search for an ability starting from the name passed
	 * 
	 * @param name
	 * @return null if no ability exist, the ability otherwise
	 * @throws AbilityException
	 */
	public Ability getAbilityByName(String name) throws AbilityException;

	/**
	 * Insert a new suggestion for the specified user
	 * 
	 * @param user
	 * @param text
	 * @throws AbilityException
	 *             with the error message
	 */
	public void insertSuggestion(User user, String text)
			throws AbilityException;

	/**
	 * @return list of all ability in the system
	 */
	public List<Ability> getAbilityList();

	/**
	 * 
	 * @return list of all suspended ability requests.
	 */
	public List<AbilityRequest> getAbilityRequests();

	/**
	 * update the state about an ability request.
	 * 
	 * @param id
	 *            about an ability request
	 * @param state
	 *            about an ability request
	 */
	public void updateAbilityRequestState(String id, String state);

	/**
	 * Insert a new ability in the sistem.
	 * 
	 * @param name
	 */
	public void insertNewAbility(String name);
}
