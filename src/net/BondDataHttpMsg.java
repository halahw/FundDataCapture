package net;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.inter.HttpMsg;
import util.DateUtil;

public class BondDataHttpMsg extends AbstractHttpMsg {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    String bondUrl = "http://bond.eastmoney.com/data/bonddata.html";
    HttpMsg bondHttpMsg = new BondDataHttpMsg();
    bondHttpMsg.getHtmlMsgFromWeb(bondUrl);
  }

  @Override
  public void header() {
    // TODO Auto-generated method stub

  }

  @Override
  public void post() {
    // TODO Auto-generated method stub

  }

  @Override
  public void get() {
    // TODO Auto-generated method stub

  }

  @Override
  public void parseHtmlMsg(String htmlMsg) {
    Document doc = Jsoup.parse(htmlMsg);
    String msg = "";
    Elements aHtml = doc.select("ul");
    StringBuffer hrefMsg = new StringBuffer();
    for (Element element : aHtml) {
      Elements element1 = element.getElementsByClass("name");// 取class为name的element
      Elements element2 = element.getElementsByClass("code");// 取class为code的element
      msg = element1.select("li").text() + "-" + (element2.select("li").text());
      Pattern p = Pattern.compile(".*\\d+.*");
      Matcher m = p.matcher(msg);
      if (m.matches()) {// 包含数字，则认为是债券信息
        hrefMsg.append(msg).append("\n");
      }
    }
    writeHtml2txt(hrefMsg.toString(), "bond_data"
        + DateUtil.getDateyyyyMMddHHmmss() + ".txt");
  }

  @Override
  public void parseHtmlMsg2InsertSQL(String htmlMsg) {
    // TODO Auto-generated method stub
    Document doc = Jsoup.parse(htmlMsg);
    String msg = "";
    Elements aHtml = doc.select("ul");
    StringBuffer caputureBondMsg = new StringBuffer();
    StringBuffer insertSQL = new StringBuffer();
    for (Element element : aHtml) {
      Elements element1 = element.getElementsByClass("name");// 取class为name的element
      Elements element2 = element.getElementsByClass("code");// 取class为code的element
      msg = element1.select("li").text() + "-" + (element2.select("li").text());
      Pattern p = Pattern.compile(".*\\d+.*");
      Matcher m = p.matcher(msg);
      if (m.matches()) {// 包含数字，则认为是债券信息
        if(caputureBondMsg.toString().contains(msg)){
          continue;
        }
        caputureBondMsg.append(msg).append("\n");
        insertSQL.append("insert into t_fund_bond_info values ('" + DateUtil.getDateyyyyMMdd()
        + "','" + element2.select("li").text() + "','"
        + element1.select("li").text() + "',current_timestamp,current_timestamp) "
        + "on duplicate key update bond_name ='"+ element1.select("li").text() +"' ;")
        .append("\n");
      }
    }
    
    if (caputureBondMsg.toString().length() > 0) {
      /*writeHtml2txt(caputureBondMsg.toString(),
          "bond_data" + DateUtil.getDateyyyyMMddHHmmss() + ".txt");*/

      writeHtml2txt(insertSQL.toString(), "bond_sql" + DateUtil.getDateyyyyMMddHHmmss() + ".sql");
    }
    System.out.println("done");
  }

}
