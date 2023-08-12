package com.tiger.openai.util;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * @Author Zenghu
 * @Date 2023年08月12日 15:54
 * @Description
 * @Version: 1.0
 **/
public class WordUtils {

    public static void readWord(String inputFile) {
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
             XWPFDocument document = new XWPFDocument(inputStream)) {
            Iterator<XWPFParagraph> paragraphs = document.getParagraphsIterator();
            while (paragraphs.hasNext()) {
                XWPFParagraph paragraph = paragraphs.next();
                /**
                 * styleID:
                 * 4:标题，数字大小代表标题级别
                 * a5:正文
                 * a7:列表
                 *
                 */

                final String styleID = paragraph.getStyleID();
                if (styleID == null) continue;
                final String paragraphText = paragraph.getParagraphText();
                if (paragraphText != null && paragraphText.length() > 0) {
                    System.out.println(styleID + ":" + paragraphText);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
