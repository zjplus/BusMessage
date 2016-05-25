package com.zjlog.zbus.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author zhangjia
 * @time 2015年2月3日	下午8:33:31
 * @work 封装返回json格式
 */
public class ResponseUtil {

	public static void write(HttpServletResponse response,Object o)throws Exception{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(o.toString());
		out.flush();
		out.close();//PrintWriter对象
	}
}
