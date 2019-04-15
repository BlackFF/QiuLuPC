package com.howin.qiulu.Util;
import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DOM4JReader {

 public String[] xmlRead() {
 File file = new File("D:"+File.separator+"postage.xml");
 /*//linux 下的路径
  File file = new File(File.separator+"apps"+File.separator+"pic"+File.separator+"postage.xml");*/
  SAXReader reader = new SAXReader();
  Document doc = null;
  String demo[] = new String[4];
  try {
   doc = reader.read(file);
  } catch (DocumentException e) {
   e.printStackTrace();
  }
  Element root = doc.getRootElement();
  Iterator iter = root.elementIterator();
  while (iter.hasNext()) {
   Element linkman = (Element) iter.next();
   demo[0] = linkman.elementText("postLine");
   demo[1] = linkman.elementText("postage");
  
  }
  
   return demo;
 }

}

