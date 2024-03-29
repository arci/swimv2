package it.polimi.swim.servlet;

import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;
import it.polimi.swim.session.FriendsManagerRemote;
import it.polimi.swim.session.ProfileManagerRemote;
import it.polimi.swim.session.exceptions.UserException;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProfileServlet extends HttpServlet {

	private static final long serialVersionUID = -4806101806094670071L;

	public ProfileServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (!haveToShowSessionUserProfile(request, response)) {
			try {
				InitialContext jndiContext = new InitialContext();

				Object ref = jndiContext.lookup("ProfileManager/remote");
				ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;
				ref = jndiContext.lookup("FriendsManager/remote");
				FriendsManagerRemote friendsManager = (FriendsManagerRemote) ref;

				User userLoaded = profileManager.getUserByUsername(request
						.getParameter("username"));
				request.setAttribute("userLoaded", userLoaded);
				getUserInformation(userLoaded, request, response);

				if (friendsManager.areFriends((User) request.getSession()
						.getAttribute("User"), userLoaded)) {
					request.setAttribute("friendState", "friend");
				} else if (friendsManager.isRequestPending((User) request
						.getSession().getAttribute("User"), userLoaded)
						|| friendsManager.isRequestPending(userLoaded,
								(User) request.getSession()
										.getAttribute("User"))) {
					request.setAttribute("friendState", "pending");
				} else {
					request.setAttribute("friendState", "notFriends");
				}
			} catch (NamingException e) {
				request.setAttribute("error", "can't reach the server");
			} catch (UserException ue) {
				request.setAttribute("error", ue.getMessage());
			}
			request.setAttribute("from", request.getParameter("from"));
			System.out
					.println("*** [ProfileServlet] forwarding to profile.jsp ***");
			getServletConfig().getServletContext()
					.getRequestDispatcher("/user/profile.jsp")
					.forward(request, response);
		} else {
			System.out
					.println("*** [ProfileServlet] doGet, no params, show current user profile ***");
			getUserInformation(
					(User) request.getSession().getAttribute("User"), request,
					response);
			System.out
					.println("*** [ProfileServlet] forwarding to profile.jsp ***");
			getServletConfig().getServletContext()
					.getRequestDispatcher("/user/profile.jsp")
					.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			InitialContext jndiContext = new InitialContext();

			Object ref = jndiContext.lookup("ProfileManager/remote");
			ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;

			User user = (User) request.getSession().getAttribute("User");
			if (hasChange(request, response)) {
				if (request.getParameter("password").equals(user.getPassword())) {

					Hashtable<String, Object> params = new Hashtable<String, Object>();

					params.put("name", request.getParameter("name"));
					params.put("surname", request.getParameter("surname"));
					params.put("username", request.getParameter("username"));
					params.put("email", request.getParameter("email"));
					params.put("city", request.getParameter("city"));

					if (request.getParameter("phone") != null) {
						try {
							if (request.getParameter("phone").length() > 8
									&& request.getParameter("phone").length() < 15) {
								params.put("phone", Integer.parseInt(request
										.getParameter("phone")));

								request.getSession().removeAttribute("User");
								request.getSession().setAttribute(
										"User",
										profileManager.updateProfile(user,
												params));
								request.setAttribute("result",
										"your cheanges has been registered.");
							} else {
								request.setAttribute("error",
										"your phone number must be consistent.");

							}
						} catch (NumberFormatException e) {
							request.setAttribute("error",
									"the phone must be composed only by numbers");
						}
					} else {

						request.getSession().removeAttribute("User");
						request.getSession().setAttribute("User",
								profileManager.updateProfile(user, params));
						request.setAttribute("result",
								"your cheanges has been registered.");
					}

				} else {
					request.setAttribute("error",
							"your password is wrong, insert the correct password to apply the changes.");
				}
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		getServletConfig().getServletContext()
				.getRequestDispatcher("/user/modifyProfile.jsp")
				.forward(request, response);
	}

	private void getUserInformation(User user, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			InitialContext jndiContext = new InitialContext();

			Object ref = jndiContext.lookup("ProfileManager/remote");
			ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;

			Double rating = profileManager.getUserRating(user);
			request.setAttribute("rating", (int) Math.rint(rating));
			System.out.println("*** [ProfileServlet] user rating set ***");
			for (Ability ability : user.getAbilities()) {
				Double abilityRating = profileManager.getAbilityRating(user,
						ability);
				request.setAttribute(ability.getName(),
						(int) Math.rint(abilityRating));
			}
			System.out.println("*** [ProfileServlet] abilities rating set ***");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private boolean haveToShowSessionUserProfile(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("username") != null
				&& request.getParameter("username") != "") {
			System.out.println("*** [ProfileServlet] doGet, show '"
					+ request.getParameter("username") + "' profile ***");
			return false;
		} else {
			return true;
		}
	}

	private boolean hasChange(HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("User");
		String attribute = request.getParameter("name");
		if (attribute != null && !attribute.equals(user.getName())) {
			return true;
		}

		attribute = request.getParameter("surname");
		if (attribute != null && !attribute.equals(user.getSurname())) {
			return true;
		}

		attribute = request.getParameter("username");
		if (attribute != null && !attribute.equals(user.getUsername())) {
			return true;
		}

		attribute = request.getParameter("email");
		if (attribute != null && !attribute.equals(user.getEmail())) {
			return true;
		}

		attribute = request.getParameter("city");
		if (attribute != null && !attribute.equals(user.getCity())) {
			return true;
		}

		attribute = request.getParameter("phone");
		if (attribute != null) {
			try {
				if (Integer.parseInt(attribute) != user.getPhone()) {
					return true;
				}
			} catch (NumberFormatException e) {
			}
			return false;
		}

		return false;
	}
}
