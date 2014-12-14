package action;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity; 
import org.apache.velocity.context.Context;
import org.apache.velocity.servlet.VelocityServlet;

/**
 * Servlet implementation class VServlet
 */
@SuppressWarnings("deprecation")
@WebServlet(name = "VServlet", urlPatterns = "/ss/VServlet", initParams = @WebInitParam(name = "org.apache.velocity.properties", value = "/WEB-INF/velocity.properties"))
public class VServlet extends VelocityServlet {
	public Template handleRequest(HttpServletRequest request,
			HttpServletResponse response, Context context) {

		Template template = null;

		try {
			context.put("name", "Velocity Test");
			template = Velocity.getTemplate("hello.vm");
		} catch (Exception e) {
			System.err.println("Exception caught: " + e.getMessage());
		}

		return template;
	}
}
