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

    public static final String WORD2016 = "word2016";
    public static final String WORD2019 = "word2019";


    public static void readWordToText(String inputFile, String outputFile, int[] titleNumbers) {
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
                 * a3、a5:正文
                 * a4: 列表
                 * a7:列表
                 * office 2019
                 * BodyText: 正文
                 * Heading2: 二级标题
                 * ListParagraph: 列表
                 */
                final String styleID = paragraph.getStyleID();
                if (styleID == null) continue;
                final String paragraphText = paragraph.getParagraphText();
                if (paragraphText == null || paragraphText.length() == 0) continue;
                // System.out.println(styleID + ":" + paragraphText);
                // 标题
                if (styleID.matches("\\d") || styleID.matches("Heading\\d+")) {
                    BigInteger numIlvl = paragraph.getNumIlvl();
                    if (numIlvl == null) {
                        writer.write(paragraphText + "\n");
                    } else {
                        String numLevelText = paragraph.getNumLevelText();
                        int index = numIlvl.intValue();
                        for (int i = index + 1; i < titleNumbers.length; i++) {
                            titleNumbers[i] = 0;
                        }
                        titleNumbers[index]++;
                        // 需要将之前的置为0
                        for (int i = 0; i < index + 1; i++) {
                            numLevelText = numLevelText.replace("%" + (i + 1), titleNumbers[i] + "");
                        }
                        System.out.println("title:" + index + "," + numLevelText + "," + titleNumbers[index] + "," + paragraphText);
                        writer.write(numLevelText + " " + paragraphText + "\n");
                    }
                } else if (styleID.matches("a\\d+") || styleID.equals("BodyText") || styleID.equals("ListParagraph")) { //段落
                    writer.write(paragraphText + "\n");
                }
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
