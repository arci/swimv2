package it.polimi.swim.servlet;

import it.polimi.swim.enums.UserType;
import it.polimi.swim.model.User;
import it.polimi.swim.session.AbilityManagerRemote;
import it.polimi.swim.session.ProfileManagerRemote;
import it.polimi.swim.session.exceptions.AbilityException;
import it.polimi.swim.session.exceptions.UserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class InitializationServelt
 */
public class InitializationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InitializationServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		initDatabase(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("/SWIMweb");
	}

	private void initDatabase(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("pwd") != null
				&& request.getParameter("pwd").equals("admin")) {
			List<String> initError = new ArrayList<String>();
			List<String> initMessage = new ArrayList<String>();
			addUsers(initMessage, initError);
			addAbilities(initMessage, initError);
			request.setAttribute("initError", initError);
			request.setAttribute("initMessage", initMessage);
			getServletConfig().getServletContext()
					.getRequestDispatcher("/initialization/initialization.jsp")
					.forward(request, response);
		} else {
			response.sendRedirect("/SWIMweb");
		}
	}

	private void addUsers(List<String> initMessage, List<String> initError) {
		try {
			InitialContext jndiContext = new InitialContext();
			Object ref = jndiContext.lookup("ProfileManager/remote");
			ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;
			try {
				profileManager.getUserByUsername("admin");
				initError.add("user 'admin' already exists");
			} catch (UserException e) {
				User admin = new User();
				admin.setType(UserType.ADMIN);
				admin.setName("admin");
				admin.setUsername("admin");
				admin.setEmail("admin@swim.com");
				admin.setPassword("admin");
				profileManager.insertNewUser(admin);
				initMessage.add("user 'admin' inserted");
			}
			try {
				profileManager.getUserByUsername("mouse");
				initError.add("user 'mouse' already exists");
			} catch (UserException e) {
				User topolino = new User();
				topolino.setType(UserType.NORMAL);
				topolino.setName("mickey");
				topolino.setSurname("mouse");
				topolino.setUsername("mouse");
				topolino.setEmail("topo@fattoria.com");
				topolino.setPassword("mouse");
				topolino.setCity("Milano");
				topolino.setPhone(1234567890);
				profileManager.insertNewUser(topolino);
				initMessage.add("user 'mouse' inserted");
			}
			try {
				profileManager.getUserByUsername("dog");
				initError.add("user 'pippo' already exists");
			} catch (UserException e) {
				User pippo = new User();
				pippo.setType(UserType.NORMAL);
				pippo.setName("pippo");
				pippo.setSurname("");
				pippo.setUsername("dog");
				pippo.setEmail("cane@fattoria.com");
				pippo.setPassword("dog");
				pippo.setCity("Venezia");
				pippo.setPhone(1234567890);
				profileManager.insertNewUser(pippo);
				initMessage.add("user 'pippo' inserted");
			}
		} catch (NamingException e) {
			initError.add("can't reach the server");
		}
	}

	private void addAbilities(List<String> initMessage, List<String> initError) {
		try {
			InitialContext jndiContext = new InitialContext();
			Object ref = jndiContext.lookup("AbilityManager/remote");
			AbilityManagerRemote abilityManager = (AbilityManagerRemote) ref;
			try {
				abilityManager.getAbilityByName("giardiniere");
				initError.add("ability 'giardiniere' already exists");
			} catch (AbilityException e) {
				abilityManager.insertNewAbility("giardiniere");
				initMessage.add("ability 'giardiniere' inserted");
			}
			try {
				abilityManager.getAbilityByName("carpentiere");
				initError.add("ability 'carpentiere' already exists");
			} catch (AbilityException e) {
				abilityManager.insertNewAbility("carpentiere");
				initMessage.add("ability 'carpentiere' inserted");

			}
			try {
				abilityManager.getAbilityByName("imbianchino");
				initError.add("ability 'imbianchino' already exists");
			} catch (AbilityException e) {
				abilityManager.insertNewAbility("imbianchino");
				initMessage.add("ability 'imbianchino' inserted");
			}
		} catch (NamingException e) {
			initError.add("can't reach the server");
		}
	}
}
