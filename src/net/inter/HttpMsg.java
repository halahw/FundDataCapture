package net.inter;

public interface HttpMsg {

	public void header();
	
	public void post();

	public void get();
	
	public void getHtmlMsgFromWeb(String url);
	
	public void writeHtml2txt(String htmlMsg,String fileName);
	
	public void parseHtmlMsg(String htmlMsg);
	
	public void parseHtmlMsg2InsertSQL(String htmlMsg);
	
}
