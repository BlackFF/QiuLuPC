package com.howin.qiulu.test;

import java.io.File;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.baidu.ueditor.define.FileType;
import com.howin.qiulu.Util.HanyuPinyinHelper;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

@Controller
public class Test {

	
	@RequestMapping("/test")
	public void test(@RequestParam(value = "file", required = false)MultipartFile file){
		if (!file.isEmpty()) {
			try {
				// 文件保存路径
				String path = "F:\\pic\\qiulu\\onSale";  
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
				/*Thumbnails.of(path.concat("\\").concat(fileName)).sourceRegion(Positions.CENTER,700,270)
				.size(700,270).toFile(path.concat("\\").concat(fileName)); */
				Thumbnails.of(path.concat("\\").concat(fileName)).size(700,270).toFile(path.concat("\\").concat(fileName));
				//Thumbnails.of(path.concat("\\").concat(fileName)).scale(1f).outputQuality(0.25f).toFile(path.concat("\\").concat(fileName));
				
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}
	
	@RequestMapping("/thranslate")
	@ResponseBody
	public String keyWord(String keyword) {
		
			HanyuPinyinHelper hanyuPinyinHelper = new HanyuPinyinHelper();
			keyword = hanyuPinyinHelper.toHanyuPinyin(keyword);// 把汉字转化为拼音
			keyword = keyword.toLowerCase();// 把大写字母转化为小写
			System.out.println(keyword);
			return keyword;
		}
	}
	

