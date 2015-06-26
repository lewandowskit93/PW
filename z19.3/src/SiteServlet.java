

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SiteServlet
 */
@WebServlet("/SiteServlet.html")
public class SiteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SiteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		ServletOutputStream out = response.getOutputStream();
		out.println("<html>");
		out.println("<body>");
		out.println();
		String svg_params = request.getParameter("y");
		if (svg_params != null) {
			out.println("<img width='100%' height='100%' src='SVGServlet?y="+svg_params+"'></img>");
		} else {
			out.println("<img width='100%' height='100%' src='SVGServlet'></img>");
		}
		
		out.println("</body>");
		out.println("</html>");
	}

}
