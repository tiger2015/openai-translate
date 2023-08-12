package com.tiger.openai.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author Zenghu
 * @Date 2023年08月10日 20:03
 * @Description
 * @Version: 1.0
 **/
@Slf4j
public class PdfHelper {


    private static boolean checkFile(String filePath) throws FileNotFoundException {
        if (!Files.exists(Paths.get(filePath))) {
            throw new FileNotFoundException(filePath);
        }
        return true;
    }

    /**
     * 获取总页数
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static int getTotalPage(String filePath) throws IOException {
        checkFile(filePath);
        PDDocument pdDocument = null;
        try (RandomAccessBufferedFileInputStream bufferedFileInputStream = new RandomAccessBufferedFileInputStream(filePath)) {
            PDFParser pdfParser = new PDFParser(bufferedFileInputStream);
            pdfParser.parse();
            pdDocument = pdfParser.getPDDocument();
            return pdDocument.getNumberOfPages();
        } finally {
            if (pdDocument != null) {
                pdDocument.close();
            }
        }
    }

    /**
     *
     * @param filePath
     * @param startPage 包含
     * @param endPage 包含
     * @return
     * @throws IOException
     */
    public static String getPdfContentByPage(String filePath, int startPage, int endPage) throws IOException {
        checkFile(filePath);
        PDDocument pdDocument = null;
        try (RandomAccessBufferedFileInputStream bufferedFileInputStream = new RandomAccessBufferedFileInputStream(filePath)) {
            PDFParser pdfParser = new PDFParser(bufferedFileInputStream);
            pdfParser.parse();
            pdDocument = pdfParser.getPDDocument();
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            pdfTextStripper.setStartPage(startPage);
            pdfTextStripper.setEndPage(endPage);
            return pdfTextStripper.getText(pdDocument);
        } finally {
            if (pdDocument != null) {
                pdDocument.close();
            }
        }
    }
}
