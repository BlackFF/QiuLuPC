package com.howin.qiulu.Util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;



public class Utils {
	public static final String img = ".png.jpg.jpeg.gif.bmp";		
			
	
	
	/**
	   * 通过PrintWriter将响应数据写入response，ajax可以接受到这个数�?
	   * 
	   * @param response
	   * @param data 
	   */
	  public static void renderData(HttpServletResponse response, String data) {
	    PrintWriter printWriter = null;
	    try {
	      printWriter = response.getWriter();
	      printWriter.print(data);
	    } catch (IOException ex) {
	      ex.printStackTrace();
	    } finally {
	      if (null != printWriter) {
	        printWriter.flush();
	        printWriter.close();
	      }
	    }
	  }
}
