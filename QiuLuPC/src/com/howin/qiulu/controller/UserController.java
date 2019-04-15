package com.howin.qiulu.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.baidu.ueditor.define.FileType;
import com.howin.qiulu.pojo.User;
import com.howin.qiulu.pojo.UserDetail;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.UserService;
import com.howin.qiulu.service.impl.ImageService;

import net.coobird.thumbnailator.Thumbnails;


@Controller
@RequestMapping("/User")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private ImageService imageService;
	/*** 
	* Title:用户注册
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 上午11:04:04
	*/
	@RequestMapping("/register")
	@ResponseBody
	public QiuluResult register(String password, String telephone){
		QiuluResult result=new QiuluResult();
		result=userService.register(password, telephone);
		return result;
	}
	
	/*** 
	* Title: 查找这个手机号是否已经注册用户
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 下午2:22:41
	* 
	*/
	@RequestMapping("/findTelephone")
	@ResponseBody
	public QiuluResult findTelephone(String telephone){
		
		QiuluResult result=userService.findTelephone(telephone);
		return result;
		
	}
	
	@RequestMapping("/findTele")
	@ResponseBody
	public QiuluResult findTele(String telephone){
		
		QiuluResult result=userService.findTele(telephone);
		return result;
		
	}
	/*** 
	* Title: 用户登录
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 下午2:44:31
	* 
	*/
	@RequestMapping("/login")
	@ResponseBody
	public QiuluResult Login(HttpSession httpSession,String password,String telephone){
		
		QiuluResult result=new QiuluResult();
		result=	userService.Login(password, telephone);
		System.out.println(result);
		User u=(User) result.getObject();
		User user=new User();
		if(u!=null){
			user.setId(u.getId());
			user.setUsername(u.getUsername());
			user.setTelephone(u.getTelephone());
		}
		System.out.println(user);
		httpSession.setAttribute("user", user);
		return result;
	}
	/**
	 * 
	 * @author 张磊
	 * @Description:检测用户是否为登录状态
	 * @return QiuluResult
	 * @date 2017年3月23日 下午3:17:12
	 * @param session
	 * @return
	 */
	@RequestMapping("/isLogin")
	@ResponseBody
	public QiuluResult isLogin(HttpSession session){
		QiuluResult result=new QiuluResult();
		User user=(User)session.getAttribute("user");
		if(user==null){
			result.setStatus(false);
			result.setMessage("用户未登录，请先登录");
		}
		else{
	
			result.setStatus(true);
			result.setMessage("用户已登录");
			result.setObject(user);
		}
		return result;
	}
	
	/*** 
	* Title: 查看个人信息
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 下午3:08:45
	* 
	*/
	@RequestMapping("/lookUserDetail")
	@ResponseBody
	public QiuluResult LookUserDetail(HttpSession session){
		User user=(User) session.getAttribute("user");
		QiuluResult result=userService.LookUserDetail(user.getId());
		return result;
	}
	
	/*** 
	* Title: 完善个人信息
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 下午3:50:27
	* 
	*/
	@RequestMapping("/saveUserDetail")
	@ResponseBody
	public QiuluResult saveUserDetail(  UserDetail ud,HttpSession session){
			QiuluResult result=userService.saveUserDetail(ud);
			return result;

		
		
	}
	@RequestMapping("/findTelephoneByUserId")
	@ResponseBody
	public QiuluResult findTelephoneByUserId(Integer id,HttpSession session){
		QiuluResult result=new QiuluResult();
		User user=(User) session.getAttribute("user");
		result=userService.findTelephoneByUserId(user.getId());
		return result;
	}
	/*** 
	* Title: 修改密码
	* Description: id 用户id，Oldpassword 老密码，password，新密码
	* @author 杨冰冰
	* @date 2017年2月21日 上午9:56:10
	* 
	*/
	@RequestMapping("/updatePassword")
	@ResponseBody
	public QiuluResult updatePassword(Integer id,String password,HttpSession session){
			if(id==null){
		    User user=(User) session.getAttribute("user");
			QiuluResult result=userService.updatePassword(user.getId(), password);
			return result;
			}
			else{
				QiuluResult result=userService.updatePassword(id, password);
				return result;
			}
	}
	
	/**
	 * 获取session里面的user的telephone
	 * @param session
	 * @return
	 */
	@RequestMapping("/getSession")
	@ResponseBody
	public QiuluResult getTelephoneBySession(HttpSession session){
		QiuluResult result=new QiuluResult();
		User user=(User) session.getAttribute("user");
		result.setStatus(true);
		result.setObject(user.getTelephone());
		return result;
	}
	@RequestMapping("/img")
	@ResponseBody
	public void createImg(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 生成验证码及图片
		Object[] objs = ImageService.createImage();
		// 将验证码存入session
		String imgcode = (String) objs[0];
		HttpSession session = req.getSession();// 整个页面中的session都是同一个
		session.setAttribute("imgcode", imgcode);
		// 禁止图像缓存
		res.setHeader("Pragma", "no-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0);
		res.setContentType("image/jpeg");
		// 将图片输出给浏览器
		BufferedImage img = (BufferedImage) objs[1];
		// 将图像输出到Servlet输出流中。
		ServletOutputStream os = res.getOutputStream();
		ImageIO.write(img, "jpeg", os);// 图片，格式，输出流
		os.close();
	}
	
	/*** 
	* Title: 验证验证码输入是否正确
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月25日 下午4:35:43
	* 
	*/
	@RequestMapping("/verifyCode")
	@ResponseBody
    public QiuluResult verifyCode(HttpServletRequest req, HttpServletResponse res,String code) throws ServletException, IOException {
		QiuluResult result=new QiuluResult();
		System.out.println(result);
		res.setContentType("text/html;charset=utf-8");
		String imgcode=(String)req.getSession().getAttribute("imgcode");
		System.out.println(code);
		if(code==null||"".equals(code)){
			result.setStatus(false);
			result.setMessage("验证码为空");
		}else{
			if(imgcode.equalsIgnoreCase(code)){
				result.setStatus(true);
				result.setMessage("验证码正确");
			}else{
				result.setStatus(false);
				result.setMessage("验证码错误");
			}
		}
		return result;
	}
	/**
	 * @author 杨冰冰
	 * @Description: 注销
	 * @return QiuluResult
	 * @date 2017年4月7日 上午10:31:05
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("/logout")
	@ResponseBody
	public QiuluResult logout(HttpServletRequest request, HttpSession session){
		boolean flg=true;
		QiuluResult result=new QiuluResult();
		Enumeration<String> en=session.getAttributeNames();
		while(en.hasMoreElements()){
			String out=en.nextElement().toString();
			try {
				session.removeAttribute(out);
				
			} catch (NoSuchElementException e) {
				flg=false;
			}
		}
		   if(flg){
			    result.setStatus(true);
				result.setMessage("注销成功");
		   }
		    else{
		    	result.setStatus(false);
				result.setMessage("注销失败");
		    }
		return result;
	}
	/**
	 * @author 张磊
	 * @Description: 上传个人头像
	 * @return QiuluResult
	 * @date 2017年4月7日 下午1:56:18
	 * @param file
	 * @param request
	 * @param response
	 * @param httpSession
	 * @return
	 */
	@RequestMapping("/uploadHeadPort")
	@ResponseBody
	public QiuluResult uploadHeadPort(@RequestParam(value = "file", required = false)MultipartFile file,HttpServletRequest request,HttpServletResponse response,HttpSession httpSession){
		boolean flg=true;
		QiuluResult qiuluResult=new QiuluResult();
		 String img = ".png.jpg.jpeg.gif.bmp";		
		 User user=(User) httpSession.getAttribute("user");
		//保存图片
		/*String path1 = request.getSession().getServletContext().getRealPath("images/headPort/"+user.getId());  
		System.out.println(path1);*/
		/*String path = "F:\\pic\\qiulu\\headPort";*/
		
		//liunx 下的路径
		String path = "/apps/apache-tomcat-8.5.11/webapps/pic/qiulu/headPort";
		System.out.println(path);
		String fileName = file.getOriginalFilename();  
		//得到上传封面图片的后缀
		String suffix = FileType.getSuffixByFilename(fileName);
		//判断文件格式
		if(img.indexOf(suffix)==-1){//文件格式不是图片
			qiuluResult.setMessage("文件类型错误。");
			qiuluResult.setStatus(false);
			return qiuluResult;
		}     
		else{
			fileName=user.getId().toString().concat(suffix);
		File targetFile = new File(path,fileName);
		  if(!targetFile.exists()){  
			  targetFile.mkdirs();
		 }  
		//保存  
        try {  
             file.transferTo(targetFile); 
             /*Thumbnails.of(path.concat("\\").concat(fileName)).scale(1f).outputQuality(0.25f).toFile(path.concat("\\").concat(fileName));*/
            
             //linux 下的路径
             Thumbnails.of(path.concat("/").concat(fileName)).scale(1f).outputQuality(0.25f).toFile(path.concat("/").concat(fileName));
            
          } catch (Exception e) {  
              e.printStackTrace(); 
              flg=false;
             
          }  
        if(flg){
        
        	boolean b=userService.uploadHeadPort(fileName,user.getId());
        	if(b){
        		qiuluResult.setMessage("上传成功");
        		qiuluResult.setStatus(true);
        		qiuluResult.setObject(fileName);
        	  }
        	else{
        		qiuluResult.setMessage("保存失败");
        		qiuluResult.setStatus(false);
        		
        	 }
        	
        }
        else{
        	qiuluResult.setMessage("上传失败");
    		qiuluResult.setStatus(false);
         }
          return qiuluResult;
		}
		
	}
	
	
	/**
	 * @author 杨冰冰
	 * @Description: 个人信息页面展示头像，手机号，订单总数，收藏夹总数
	 * @return QiuluResult
	 * @date 2017年4月7日 下午2:10:01
	 * @param session
	 * @return
	 */
	@RequestMapping("/findHeadPortTelOrderFav")
	@ResponseBody
	public QiuluResult findHeadPortTelOrderFav(HttpSession session){
		User user=(User) session.getAttribute("user");
		QiuluResult result=new QiuluResult();
		result=userService.findHeadPortTelOrderFavByUserId(user.getId());
		return result;
	}
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
