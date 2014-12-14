package velocity;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import db.DBManager;


public class VelocityHtml {
	public static String getHtmlTepmlate(VelocityHelperClass helperClass,String vm) throws Exception  {
		
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

		ve.init();
		Template t = ve.getTemplate(vm);
		
		VelocityContext context = new VelocityContext();
		context.put("helpeCls", helperClass);
		// write to sysout
		// BufferedWriter writer = writer = new BufferedWriter(
		// new OutputStreamWriter(System.out));
		/* now render the template into a StringWriter */
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		/* show the World */
		String htmlData = writer.toString();
		// System.out.println( writer.toString() );
		return htmlData;
	}

	public static void main(String[] args) throws Exception {
		List n = new ArrayList();
		VelocityHtml v = new VelocityHtml();
		//System.out.println(v.getHtmlTepmlate(n));
	}
}