package com.zjlog.zbus.util;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.zjlog.zbus.util.ResponseUtil;
/**
*全国公交及路径规划查询调用示例代码 － 聚合数据
*在线接口文档：http://www.juhe.cn/docs/135
**/
 
public class BusUtil {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
 
    //配置您申请的KEY
    public static final String APPKEY ="0d4575496251e5c7249d1ef9429c0c1d";
 
    //1.公交线路查询
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void getRequest1(String city,String busLine,HttpServletRequest request, HttpServletResponse response){
        String result =null;
        String url ="http://op.juhe.cn/189/bus/busline";//请求接口地址
		Map params = new HashMap();//请求参数
            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
            params.put("dtype","");//返回数据的格式,xml或json，默认json
            params.put("city",city);//城市名称(如：苏州)或者城市代码（如：0512）
            params.put("bus",busLine);//公交线路
 
        try {
            result =net(url, params, "GET");
            JSONObject object = JSONObject.fromObject(result);
            if(object.getInt("error_code")==0){
                System.out.println(object.get("result"));
                ResponseUtil.write(response, object);
            }else{
                System.out.println(object.get("error_code")+":"+object.get("reason"));
                ResponseUtil.write(response,"{'error':'线路错误'}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    //2.公交站台经往车辆查询
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getRequest2(String city,String station,HttpServletRequest request, HttpServletResponse response){
        String result =null;
        String url ="http://op.juhe.cn/189/bus/station";//请求接口地址
        Map params = new HashMap();//请求参数
            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
            params.put("dtype","");//返回数据的格式,xml或json，默认json
            params.put("city",city);//城市名称(如：苏州)或者城市代码（如：0512）
            params.put("station",station);//公交站台名称
 
        try {
            result =net(url, params, "GET");
            JSONObject object = JSONObject.fromObject(result);
            if(object.getInt("error_code")==0){
                System.out.println(object.get("result"));
                ResponseUtil.write(response, object);
            }else{
                System.out.println(object.get("error_code")+":"+object.get("reason"));
                ResponseUtil.write(response, "{'error':'城市或站点不匹配'}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    //3.公交线路换乘方案
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getRequest3(String city,String xys,String type,HttpServletRequest request, HttpServletResponse response){
        String result =null;
        String url ="http://op.juhe.cn/189/bus/transfer";//请求接口地址
        Map params = new HashMap();//请求参数
            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
            params.put("dtype","");//返回数据的格式,xml或json，默认json
            params.put("city",city);//城市名称(如：苏州)或者城市代码（如：0512）
            params.put("xys",xys);//途经点坐标集合
            params.put("type",type);//行驶类型 0表示最快捷模式，尽可能乘坐轨道交通和快速公交线路； 2表示最少换乘模式，尽可能减少换乘次数； 3表示最少步行模式，尽可能减少步行距离； 4表示最舒适模式，；乘坐有空调的车线； 5表示纯地铁模式，只选择地铁换乘
 
        try {
            result =net(url, params, "GET");
            JSONObject object = JSONObject.fromObject(result);
            if(object.getInt("error_code")==0){
                System.out.println(object.get("result"));
                ResponseUtil.write(response, object);
            }else{
                System.out.println(object.get("error_code")+":"+object.get("reason"));
                ResponseUtil.write(response, "{'error':'换乘信息错误'}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
 
 
    public static void main(String[] args) {
    	//getRequest1("武汉","811");
    }
 
    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                        out.writeBytes(urlencode(params));
                } catch (Exception e) {
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }
 
    //将map型转为请求参数型
    @SuppressWarnings("rawtypes")
	public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}