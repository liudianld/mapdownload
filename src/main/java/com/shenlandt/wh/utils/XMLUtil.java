package com.shenlandt.wh.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.*;

import com.alibaba.fastjson.JSON;
/**
 * 用原生方式处理xml
 * 
 * <p/>
 * <p>User: weiq
 * <p>Date: 2015年9月22日 上午9:41:34 
 * <p>Version: 1.0
 */
public class XMLUtil {
    
    public static void parse() throws Exception{
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse (new File("src/test/java/org/cunframework/sc/map4j/res.xml"));

        // normalize text representation
        doc.getDocumentElement ().normalize ();
        System.out.println ("Root element of the doc is " + 
             doc.getDocumentElement().getNodeName());


        NodeList listOfPersons = doc.getElementsByTagName("data");
        int totalPersons = listOfPersons.getLength();
        System.out.println("Total no of data : " + totalPersons);
        File f = new File("d:/res.json");
        for(int s=0; s<listOfPersons.getLength() ; s++){
            Node firstPersonNode = listOfPersons.item(s);
            if(firstPersonNode.getNodeType() == Node.ELEMENT_NODE){
                System.out.println(1);
                Element firstPersonElement = (Element)firstPersonNode;

                //-------
                NodeList firstNameList = firstPersonElement.getElementsByTagName("value");
                Element firstNameElement = (Element)firstNameList.item(0);
                NodeList textFNList = firstNameElement.getChildNodes();
//                System.out.println("First Name : " + 
//                        ((Node)textFNList.item(0)).getNodeValue().trim());
                
                FileUtils.write(f, JSON.toJSONString(
                    JSON.parse(new String(
                        Encodes.decodeBase64(
                            ((Node) textFNList.item(0)).getNodeValue().trim()
                        )
                    )), true), true);
            }
        }
    }
}
