package net;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Strings;

import net.inter.HttpMsg;
import util.DateUtil;

public class StockDataHttpMsg extends AbstractHttpMsg {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    String stockUrl = "http://quote.eastmoney.com/stocklist.html";
    HttpMsg stockHttpMsg = new StockDataHttpMsg();
    stockHttpMsg.getHtmlMsgFromWeb(stockUrl);
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
    // TODO Auto-generated method stub
    Document doc = Jsoup.parse(htmlMsg);
    Elements aHtml = doc.select("a");
    StringBuffer hrefMsg = new StringBuffer();
    String msg = "";
    for (Element element : aHtml) {
      msg = Strings.nullToEmpty(element.text());
      /*
       * if (msg.indexOf("(") > 0) {// 包含括号和数字，则认为是股票信息 hrefMsg.append(msg).append("\n"); }
       */
      Pattern p = Pattern.compile(".*\\d+.*");
      Matcher m = p.matcher(msg);
      if (m.matches()) {// 包含数字，则认为是股票信息
        hrefMsg.append(msg).append("\n");
      }
    }
    writeHtml2txt(hrefMsg.toString(), "stock_data"
        + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".txt");
  }

  @Override
  public void parseHtmlMsg2InsertSQL(String htmlMsg) {
    // TODO Auto-generated method stub
    Document doc = Jsoup.parse(htmlMsg);
    Elements aHtml = doc.select("a");
    StringBuffer capatureStockMsg = new StringBuffer();
    StringBuffer insertSQL = new StringBuffer();
    String msg = "";
    
    if(aHtml.isEmpty()){
      System.out.println("capture html's length is 0");
      return;
    }
    for (Element element : aHtml) {
      msg = Strings.nullToEmpty(element.text());
      /*
       * if (msg.indexOf("(") > 0) {// 包含括号和数字，则认为是股票信息 hrefMsg.append(msg).append("\n"); }
       */
      Pattern p = Pattern.compile(".*\\d+.*");
      Matcher m = p.matcher(msg);
      if (m.matches() && msg.indexOf("(") > 0) {// 包含数字，则认为是股票信息

        if (capatureStockMsg.toString().contains(msg)) {
          continue;
        }
        capatureStockMsg.append(msg).append("\n");
        insertSQL.append("insert into t_fund_stock_info values ('" + DateUtil.getDateyyyyMMdd()
            + "','" + msg.substring(msg.indexOf("(") + 1, msg.indexOf(")")) + "','"
            + msg.substring(0, msg.indexOf("(")) + "',current_timestamp,current_timestamp) "
           + " on duplicate key update stock_name ='"+ msg.substring(0, msg.indexOf("(")) +"' ;")
            .append("\n");
      }
    }
    
    if (capatureStockMsg.toString().length() > 0) {
      /*writeHtml2txt(capatureStockMsg.toString(),
          "stock_data" + DateUtil.getDateyyyyMMddHHmmss() + ".txt");*/
      writeHtml2txt(insertSQL.toString(), "stock_sql" + DateUtil.getDateyyyyMMddHHmmss() + ".sql");
    }
    System.out.println("done");
  }

}
