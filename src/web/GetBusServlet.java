package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zjlog.zbus.util.BusUtil;
/**
 * 处理页面请求的servlet只有一个页面，怎么都请求到那里
 * @author zhangjia
 * 2016年4月18日 下午3:20:44
 * 作用
 */
/**
 * @author zhangjia
 * 2016年4月19日 下午5:53:03
 * 作用 
 */
public class GetBusServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost((HttpServletRequest) request, response);
	}

	/**
	 * 参数 method1：公交线路查询  method=method1，busLine=如811
	 * 参数 method2：公交站台经往车辆查询  method=method2，station=如湖北大学站
	 * 参数 method3：公交线路换乘方案  method=method3，type=类型
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//根据参数判断是哪一类请求
		String method=request.getParameter("method");
		if (method.equals("method1")) {
			String busLine=request.getParameter("busLine");
			try {
				//请求这busLine一条线的公交信息
				BusUtil.getRequest1("武汉", busLine, request,response);
			} catch (Exception e) {
			}
		}else if (method.equals("method2")) {
			try {
				String station=request.getParameter("station");
				//请求这busLine一条线的公交信息
				BusUtil.getRequest2("武汉", station, request, response);
			} catch (Exception e) {
			}
		}else if (method.equals("method3")) {
			try {
				String type=request.getParameter("type");
				String xys=request.getParameter("xys");
				BusUtil.getRequest3("武汉", xys, type, request, response);
			} catch (Exception e) {
			}
		}
	}
}
