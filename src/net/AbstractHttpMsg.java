package net;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import net.inter.HttpMsg;

public abstract class AbstractHttpMsg implements HttpMsg {

  @Override
  public void writeHtml2txt(String htmlMsg, String saveFileName) {

    System.out.println("htmlMsg length: " + htmlMsg.length());
    if(htmlMsg.length() <= 0 )
      return;
    String savePath = "D:/" + saveFileName;
    try {
      File file = new File(savePath);
      FileWriter fileW = new FileWriter(file);
      BufferedWriter bw = new BufferedWriter(fileW);
      bw.write(htmlMsg);
      bw.close();
      file = new File(savePath);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public void getHtmlMsgFromWeb(String url) {
    // TODO Auto-generated method stub
    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    //CloseableHttpClient client = HttpClients.custom();
    CloseableHttpClient httpClient = httpClientBuilder.build();

    HttpGet httpGet = new HttpGet(url);
    CloseableHttpResponse  httpResponse = null;
    System.out.println(httpGet.getRequestLine());

    String htmlMsg = "";
    try {
      httpResponse = httpClient.execute(httpGet);
      HttpEntity entity = httpResponse.getEntity();
      System.out.println("status: " + httpResponse.getStatusLine());
      if (entity != null) {
        htmlMsg = EntityUtils.toString(entity, "GBK");
        System.out.println("response content length: " + entity.getContentLength());
        parseHtmlMsg2InsertSQL(htmlMsg);
      }
      
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    } finally {
      try {
        httpResponse.close();
        httpClient.close();
      } catch (IOException e) {
        // TODO: handle exception
        e.printStackTrace();
      }
    }
  }
}
