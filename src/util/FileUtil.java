package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import com.google.common.base.Strings;

public class FileUtil {

  public static String getContent(String fileName){
    String filePath = "D:/";
    StringBuffer content = new StringBuffer();
    try {
      FileInputStream in = new FileInputStream(filePath+fileName);
      BufferedReader reader = new BufferedReader(new InputStreamReader(in,"GB2312"));
      String tempString = null;
      while( (tempString= reader.readLine()) != null){
        if(Strings.isNullOrEmpty(tempString)) continue;
        content.append(tempString);
      }
      reader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return content.toString();
  }
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    //System.out.print(getContent("hkstock.html"));
    String stock_name = "ADFD'ADAF";
    stock_name = stock_name.replace("'", "\"");
    System.out.println(stock_name);
  }

}
