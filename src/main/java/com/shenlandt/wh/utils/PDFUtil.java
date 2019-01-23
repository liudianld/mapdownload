package com.shenlandt.wh.utils;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 操作pdf(创建,修改,删除)
 * @author Administrator
 *
 */
public class PDFUtil {
	
	public String title;
	public PDFUtil(){
		this.title = "helo orcs";
	}
	/**
	 * 生成一个简单的仅带标题和插图的pdf文件
	 * @param fileName 生成的文件名称
	 * @param title pdf标题
	 * @param content 文本内容
	 * @param figure 插图
	 * @return
	 */
	public static OutputStream createSimplePdf(OutputStream outputStream, String title, String content, Image figure){
		title = title==null? "hello" : title;
		content = content == null? "orcs print" : content;
		
		// 创建一个Document对象 第一个参数是页面大小。接下来的参数分别是左、右、上和下页边距。  
		Document doc = new Document(PageSize.A4, 20, 20, 20, 20);  
        try {
            PdfWriter.getInstance(doc,outputStream);
            doc.open();
            //标题
            BaseFont bfChinese = BaseFont.createFont( "STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED); 
            Font f20 =  new  Font(bfChinese  ,  20 , Font.NORMAL);
            Paragraph t = new Paragraph(title,f20);
            t.setAlignment(Image.ALIGN_CENTER);
            doc.add(t); 
            doc.add(Chunk.NEWLINE);
            doc.addCreationDate();
            if(figure!=null){
	            Image img = figure;
				img.setAlignment(Image.ALIGN_CENTER);
				img.scalePercent(getPercent(img.getHeight(),img.getWidth()));
				doc.add(img);
            }
			return outputStream;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	doc.close();
        }
		return null;
	}
	
	public static OutputStream createSimplePdf(String fileName, String title, String content, String figurePath) throws Exception{
		Image img = null;
		if(figurePath!=null){
			if(figurePath.indexOf("http")>=0)
				img = Image.getInstance(new URL(figurePath));
			else
				img = Image.getInstance(figurePath);
		}
		return createSimplePdf(new FileOutputStream(fileName), title, content, img);
	}
	
	private static float getPercent(float h, float w) { 
        float p2 = 74.86f; 
        if(w>595){
        	p2 = (595-20)/w*100;
        }else if(h>780){
        	p2 = (780-20)/h*100;
        }
        return p2; 
    } 

	
	public static void main(String[] args){
	}
}
