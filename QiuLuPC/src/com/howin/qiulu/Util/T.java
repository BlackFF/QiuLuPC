package com.howin.qiulu.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class T {
	
        public static String getOrderIdByUUId() {
        	
            int machineId = (int)(Math.random()*10)%9+1;//最大支持1-9个集群机器部署
            int hashCodeV = UUID.randomUUID().toString().hashCode();
            if(hashCodeV < 0) {//有可能是负数
                hashCodeV = - hashCodeV;
            }
            // 0 代表前面补充0     
            // 4 代表长度为4     
            // d 代表参数为正数型
            return machineId + String.format("%010d", hashCodeV);
        }
        
        public static String getTimeShort() {
        	   SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        	   Date currentTime = new Date();
        	   String dateString = formatter.format(currentTime);
        	   return dateString;
        	}
        
        public static String getFourRandom(){
            Random random = new Random();
            String fourRandom = random.nextInt(10000) + "";
            int randLength = fourRandom.length();
            if(randLength<4){
              for(int i=1; i<=4-randLength; i++)
                  fourRandom = "0" + fourRandom  ;
          }
            String x=getTimeShort().concat(fourRandom);  
            return x;
        }
        
       /* public static void main(String[] args) {
            
        	for (int i = 0; i < 10000; i++) {
        		//int machineId = (int)(Math.random()*10)%9+1;
        		//System.out.println(machineId);
        		System.out.println(getOrderIdByUUId());
			}
        }*/
}