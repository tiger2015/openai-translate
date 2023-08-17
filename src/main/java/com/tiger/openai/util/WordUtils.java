package com.tiger.openai.util;

import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;

import java.io.*;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

/**
 * @Author Zenghu
 * @Date 2023年08月12日 15:54
 * @Description
 * @Version: 1.0
 **/
public class WordUtils {

    public static void readWord2016(String inputFile, String outputFile) {
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, false));
             XWPFDocument document = new XWPFDocument(inputStream)) {
            Iterator<XWPFParagraph> paragraphs = document.getParagraphsIterator();
            while (paragraphs.hasNext()) {
                XWPFParagraph paragraph = paragraphs.next();
                /**
                 * office 2016
                 * styleID:
                 * 4:标题，数字大小代表标题级别
                 * a5:正文
                 * a7:列表
                 *
                 */
                final String styleID = paragraph.getStyleID();
                if (styleID == null) continue;
                final String paragraphText = paragraph.getParagraphText();
                if (paragraphText ==null || paragraphText.length() == 0) continue;
                System.out.println(styleID+":"+paragraphText);
                if (styleID.matches("\\d")){
                    writer.write(paragraphText+"\n");
                }else if ("a5".equalsIgnoreCase(styleID)){
                    writer.write(paragraphText+"\n");
                }else if ("a7".equalsIgnoreCase(styleID)){
                    writer.write(paragraphText+"\n");
                }
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readWord2019(String inputFile, String outputFile) {
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, false));
             XWPFDocument document = new XWPFDocument(inputStream)) {
            Iterator<XWPFParagraph> paragraphs = document.getParagraphsIterator();
            while (paragraphs.hasNext()) {
                XWPFParagraph paragraph = paragraphs.next();
                /**
                 *
                 * office 2019
                 * BodyText: 正文
                 * Heading2: 二级标题
                 * ListParagraph: 列表
                 */
                final String styleID = paragraph.getStyleID();
                if (styleID == null) continue;
                final String paragraphText = paragraph.getParagraphText();
                if (paragraphText ==null || paragraphText.length() == 0) continue;
               // System.out.println(styleID+":"+paragraphText);
                if ("BodyText".equalsIgnoreCase(styleID)){
                    if (paragraphText.trim().startsWith("(") || paragraphText.trim().startsWith("（")){
                        continue;
                    }
                    writer.write(paragraphText+"\n");
                    //String style = paragraph.getStyle();
                  //  System.out.println(style+":"+paragraphText);
                }else if (styleID.startsWith("Heading")){
                  //  String numLevelText = paragraph.getNumLevelText();
                   // String numFmt = paragraph.getNumFmt();
                  //  BigInteger numID = paragraph.getNumID();
                   // String text = paragraph.getText();
                   // BodyType partType = paragraph.getPartType();
                   // String style = paragraph.getStyle();
                    CTP ctp = paragraph.getCTP();
                    List<CTBookmark> bookmarkStartList = ctp.getBookmarkStartList();
                    if (bookmarkStartList.size() > 1){
                        String name = bookmarkStartList.get(0).getName();
                       // System.out.println(name);
                        writer.write(name+"\n");
                    }

                   // IBody body = paragraph.getBody();
                  //  BigInteger numIlvl = paragraph.getNumIlvl();
                   //System.out.println(numLevelText);
                  //  writer.write(paragraph.getText()+"\n");
                }else if ("ListParagraph".equalsIgnoreCase(styleID)){
                    writer.write(paragraph.getText()+"\n");
                }
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
