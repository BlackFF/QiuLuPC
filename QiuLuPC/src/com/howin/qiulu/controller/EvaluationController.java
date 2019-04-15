package com.howin.qiulu.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.baidu.ueditor.define.FileType;
import com.howin.qiulu.pojo.Evaluation;
import com.howin.qiulu.pojo.User;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.EvaluationService;
import com.howin.qiulu.service.OrderService;

import net.coobird.thumbnailator.Thumbnails;
import net.sf.json.JSONArray;
@Controller
@RequestMapping("/Eva") 
public class EvaluationController {
	@Autowired
	private EvaluationService evaluationService;
	@Resource
     private OrderService orderService;
	
	/**
	 * 查找评价
	 * @param id
	 * @return
	 */
	@RequestMapping("/findEvaBySkuId")
	@ResponseBody
	public QiuluResult findEvaBySkuId(Integer id,Integer pageNo){
		if(pageNo==null){
			pageNo=1;
		}
		QiuluResult result=evaluationService.findEvaBySkuId(id,5,pageNo);
		return result;
	}
	
	/**
	 * @author 杨冰冰
	 * @Description:评价页面查看所有已经完成订单
	 * @return QiuluResult
	 * @date 2017年4月11日 上午11:00:58
	 * @param session
	 * @return
	 */
	@RequestMapping("/findOrderResult")
	@ResponseBody
	public QiuluResult findOrderResult(HttpSession session,Integer pageNo){
		User user=(User) session.getAttribute("user");
		if(pageNo==null){
			pageNo=1;
		}
		QiuluResult result=evaluationService.findOrderResultByStatus3(user.getId(),5,pageNo);
		Object o=result.getObject();
		return result;
	}
	
	/**
	 * @author 杨冰冰
	 * @Description: 未评价的订单
	 * @return QiuluResult
	 * @date 2017年4月11日 下午2:31:54
	 * @param session
	 * @return
	 */
	@RequestMapping("/findEvaNoFinish")
	@ResponseBody
	public QiuluResult findEvaNoFinish(HttpSession session,Integer pageNo){
		User user=(User) session.getAttribute("user");
		if(pageNo==null){
			pageNo=1;
		}
		QiuluResult result=new QiuluResult();
		result=evaluationService.findEvaNoFinish(user.getId(),5,pageNo);
		return result;
	}
	/**
	 * @author 杨冰冰
	 * @Description: 已经评价的订单
	 * @return QiuluResult
	 * @date 2017年4月11日 下午2:32:07
	 * @param userId
	 * @return
	 */
	@RequestMapping("/findEvaFinish")
	@ResponseBody
	public QiuluResult findEvaFinish(HttpSession session,Integer pageNo){
		User user=(User) session.getAttribute("user");
		if(pageNo==null){
			pageNo=1;
		}
		QiuluResult result=new QiuluResult();
		result=evaluationService.findEvaFinish(user.getId(),5,pageNo);
		return result;
	}
	 
	/**
	 * @author 杨冰冰
	 * @Description: 查看所有评价
	 * @return QiuluResult
	 * @date 2017年4月12日 上午11:46:58
	 * @param session
	 * @param pageNo
	 * @return
	 */
	@RequestMapping("/findEvaResultByUserId")
	@ResponseBody
	public QiuluResult findEvaResultByUserId(HttpSession session,Integer pageNo){
		User user=(User) session.getAttribute("user");
		if(pageNo==null){
			pageNo=1;
		}
		QiuluResult result=new QiuluResult();
		result=evaluationService.findEvaResultByUserId(user.getId(), 5, pageNo);
		return result;
	}
	/**
	 * @author 杨冰冰
	 * @Description: 查看某一件商品的所有评价
	 * @return QiuluResult
	 * @date 2017年4月12日 下午2:01:13
	 * @param orderId
	 * @param skuId
	 * @return
	 */
	@RequestMapping("/findEvaResultBySkuId")
	@ResponseBody
	public QiuluResult findEvaResultBySkuId(Integer skuId,Integer pageNo){
		QiuluResult result=new QiuluResult();
		if(pageNo==null){
			pageNo=1;
		}
		result=evaluationService.findEvaResultBySkuId(skuId,5,pageNo);
		return result;
	}
	
	/**
	 * 
	 * @author 张磊
	 * @Description:提交评论（包含多图上传）
	 * @return QiuluResult
	 * @date 2017年4月13日 下午4:38:44
	 * @param files
	 * @param orderId
	 * @param ev
	 * @return
	 */
	@RequestMapping("/addEvaluation")
	@ResponseBody
	public QiuluResult addEvaluation(@RequestParam(value = "files", required = false)MultipartFile[] files,Evaluation ev){
		//List<String> list =new ArrayList<String>();
		Map<String,String> map=new HashMap<String,String>();
		if(files!=null&&files.length>0){
			int num=files.length;
			String[] images=new String[num];
			//循环获取file数组中得文件
			for(int i = 0;i<files.length;i++){
				MultipartFile file = files[i];
				//保存文件
				String fileName=saveFile(file);
				images[i]=fileName;
			}
			
			JSONArray json = JSONArray.fromObject(images); 
			String image=json.toString();
			System.out.println(image);
			ev.setImage(image);
		}
		QiuluResult result = orderService.evaluationByOrderId(ev);
		result.getObject();
		
		return result;
	}
	
	
	private String saveFile(MultipartFile file) {
		// 判断文件是否为空
		if (!file.isEmpty()) {
			try {
				// 文件保存路径
				String path = "F:\\pic\\qiulu\\evaluation";  
				
				/*//linux 下的路径
				String path = "/apps/apache-tomcat-8.5.11/webapps/pic/qiulu/evaluation"; */ 
				//获取文件名
				String fileName = file.getOriginalFilename();  
				
				//得到上传封面图片的后缀
				String suffix = FileType.getSuffixByFilename(fileName);
				 Date date = new Date();
				 Long d=date.getTime();
				 String s=d.toString();
				 fileName=s.concat(suffix);
				 System.out.println(fileName);
				File targetFile = new File(path,fileName);
				  if(!targetFile.exists()){  
					  targetFile.mkdirs();
				 }  
			
				// 转存文件
				file.transferTo(targetFile);
				//图片压缩处理
				 Thumbnails.of(path.concat("/").concat(fileName)).scale(1f).outputQuality(0.25f).toFile(path.concat("/").concat(fileName));
				/* Thumbnails.of(path.concat("\\").concat(fileName)).scale(1f).outputQuality(0.25f).toFile(path.concat("\\").concat(fileName));*/
				return fileName;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	@RequestMapping("/findEvaByImage")
	@ResponseBody
	public QiuluResult findEvaByImage(Integer id,Integer pageNo){
		QiuluResult result=new QiuluResult();
		if(pageNo==null){
			pageNo=1;
		}
		result=evaluationService.findEvaByImage(id, 5, pageNo);
		return result;
	}
	@RequestMapping("/findEvaNum")
	@ResponseBody
	public QiuluResult findEvaNum(Integer id){
		QiuluResult result=new QiuluResult();
		result=evaluationService.findEvaNum(id);
		return result;
	}
	
	
	
	
	
	
	
	
	
	
}
