
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SVGServlet
 */
@WebServlet("/SVGServlet")
public class SVGServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public SVGServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("image/svg+xml");
		ServletOutputStream out = response.getOutputStream();
		out.println("<svg width='400' height='400' xmlns='http://www.w3.org/2000/svg'>");
		out.println("<g transform='translate(10 10)'>");
		out.println();
		
		out.println("<!-- wykres: 400 x 200 -->");
		out.println("<!-- os y + strzalki: -->");
		out.println("<line x1='0' y1='200' x2='0' y2='0' style='stroke:rgb(255,0,0);stroke-width:2' />");
		out.println("<line x1='0' y1='0' x2='5' y2='5' style='stroke:rgb(255,0,0);stroke-width:2' />");
		out.println("<line x1='0' y1='0' x2='-5' y2='5' style='stroke:rgb(255,0,0);stroke-width:2' />");
		out.println("<line transform='translate(0 40)' x1='-5' y1='0' x2='5' y2='0' style='stroke:rgb(0,255,0);stroke-width:1' />");
		out.println("<line transform='translate(0 80)' x1='-5' y1='0' x2='5' y2='0' style='stroke:rgb(0,255,0);stroke-width:1' />");
		out.println("<line transform='translate(0 120)' x1='-5' y1='0' x2='5' y2='0' style='stroke:rgb(0,255,0);stroke-width:1' />");
		out.println("<line transform='translate(0 160)' x1='-5' y1='0' x2='5' y2='0' style='stroke:rgb(0,255,0);stroke-width:1' />");
		out.println();
		
		out.println("<!-- os x + strzalki: -->");
		out.println("<line x1='0' y1='200' x2='400' y2='200' style='stroke:rgb(255,0,0);stroke-width:2' />");
		out.println("<line x1='395' y1='195' x2='400' y2='200' style='stroke:rgb(255,0,0);stroke-width:2' />");
		out.println("<line x1='395' y1='205' x2='400' y2='200' style='stroke:rgb(255,0,0);stroke-width:2' />");
		out.println("<line transform='translate(40 200)' x1='0' y1='-5' x2='0' y2='5' style='stroke:rgb(0,0,255);stroke-width:1' />");
		out.println("<line transform='translate(80 200)' x1='0' y1='-5' x2='0' y2='5' style='stroke:rgb(0,0,255);stroke-width:1' />");
		out.println("<line transform='translate(120 200)' x1='0' y1='-5' x2='0' y2='5' style='stroke:rgb(0,0,255);stroke-width:1' />");
		out.println("<line transform='translate(160 200)' x1='0' y1='-5' x2='0' y2='5' style='stroke:rgb(0,0,255);stroke-width:1' />");
		out.println("<line transform='translate(200 200)' x1='0' y1='-5' x2='0' y2='5' style='stroke:rgb(0,0,255);stroke-width:1' />");
		out.println("<line transform='translate(240 200)' x1='0' y1='-5' x2='0' y2='5' style='stroke:rgb(0,0,255);stroke-width:1' />");
		out.println("<line transform='translate(280 200)' x1='0' y1='-5' x2='0' y2='5' style='stroke:rgb(0,0,255);stroke-width:1' />");
		out.println("<line transform='translate(320 200)' x1='0' y1='-5' x2='0' y2='5' style='stroke:rgb(0,0,255);stroke-width:1' />");
		out.println("<line transform='translate(360 200)' x1='0' y1='-5' x2='0' y2='5' style='stroke:rgb(0,0,255);stroke-width:1' />");
		out.println();
		
		out.println("<!-- dane:  cy = 200 - f(x) -->");
		String y_data = request.getParameter("y");
		if(y_data!=null)
		{
			String[] y_splitted = y_data.split(",");
			for(int i=0;i<y_splitted.length && i<9;++i)
			{
				int x = 40*(i+1);
				try
				{
					int y = Integer.parseInt(y_splitted[i]);
					out.println("<circle transform='translate("+x+")' cx='0' cy='"+(200-y)+"' r='4' stroke='black' stroke-width='1' fill='red' />");
				}
				catch(NumberFormatException e)
				{
					out.println("<!-- invalid y="+y_splitted[i]+" -->");
				}
			}
		}
		
		out.println();
		
		out.println("</g>");
		out.println("</svg>");
	}

}
