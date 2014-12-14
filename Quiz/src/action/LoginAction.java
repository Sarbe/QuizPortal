package action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.DBManager;

import util.ConfigFile;
import bean.EmpDetails;

@WebServlet("/ss/LoginAction")
public class LoginAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String errMsg = "";
	DBManager db = new DBManager();
	static Logger log = Logger.getLogger(LoginAction.class);

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		execute(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		execute(request, response);
	}

	private void execute(HttpServletRequest request,
			HttpServletResponse response) {

		String method = request.getParameter("method") == null ? "" : request
				.getParameter("method");
		try {
			if (method.equals("login")) {

				login(request, response);

			} else {
				request.setAttribute("MSG", "Invalid Request.");
				response.sendRedirect("start.jsp");

			}
		} catch (Exception e) {
			// e.printStackTrace();
			request.setAttribute("MSG", e.getMessage());
			String a = (String) request.getAttribute("MSG");
			System.out.println("" + a);
			log.info(e.getMessage());
			try {
				// request.setAttribute("MSG", "Invalid Request.");
				// response.sendRedirect("start.jsp");
				request.getRequestDispatcher("start.jsp").forward(request,
						response);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ServletException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {

			String empId = request.getParameter("empId").trim();
			String passPhrase = request.getParameter("passPhrase").trim();

			if (!empId.equals("") && !passPhrase.equals("")) {

				HttpSession session = request.getSession(false);
				session.invalidate();
				session = request.getSession(true);

				session.setMaxInactiveInterval(Integer.parseInt(ConfigFile
						.getKey("session.MaxTime")));

				request.setAttribute("SESSIONID", session.getId());

				EmpDetails ed = db.getEmpDetails(Integer.parseInt(empId),
						passPhrase);
				if (null != ed) {

					db.updateLastRevisit(ed,session.getId(),"ONLINE");
					
					ed.setQuizTypes(db.getQuizTypes());
					
					if (ed.getEmpType().equals("A")) {
						// Get Quiz Details
						ed = db.getQuizDetailsforAdminUser(ed);
						ed.setDumpSets(db.countDumpQstns());
						session.setAttribute("EMP_DETAIL", ed);
						request.getRequestDispatcher("quizHomeAdmin.jsp").forward(request, response);
					} else {
						// Get Quiz Details
						ed = db.getQuizDetailsforNormalUser(ed);
						session.setAttribute("EMP_DETAIL", ed);
						request.getRequestDispatcher("quizHome.jsp").forward(request, response);
					}

				} else {
					errMsg = "ID/Pass Phrase not correct";
					request.setAttribute("MSG", errMsg);
					request.getRequestDispatcher("start.jsp").forward(request, response);
					// response.sendRedirect("start.jsp");

				}
			} else {
				errMsg = "Enter Valid Details";
				request.setAttribute("MSG", errMsg);
				request.getRequestDispatcher("start.jsp").forward(request, response);
			}

		} catch (Exception e) {
			throw e;
		}

	}

	

}
