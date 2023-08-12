package com.tiger.openai;

import com.tiger.openai.util.PdfHelper;
import com.tiger.openai.util.TextUtils;
import com.tiger.openai.util.WordUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zenghu
 * @Date 2023年08月10日 20:18
 * @Description
 * @Version: 1.0
 **/
@Slf4j
public class TranslateApp {

    private static final List<String> FILTER_PATTERNS = new ArrayList<>();
    private static final int MAX_PAGES = 2;
    private static final List<String> FILTER_LINE_PATTERN = new ArrayList<>();

    static {
        FILTER_PATTERNS.add("\\d+");
        FILTER_PATTERNS.add("Part \\d+.+");
        FILTER_PATTERNS.add("IEEE\\s*");
        FILTER_PATTERNS.add("Std 802.11-2012.+");
        FILTER_PATTERNS.add("IEEE Std 802\\.11.+");
        FILTER_PATTERNS.add("IEEE Standard\\s.+");
        FILTER_PATTERNS.add("Copyright.+");
        FILTER_PATTERNS.add("Authorized licensed use.+");
        FILTER_PATTERNS.add("PART\\s+\\d+[:：].+");
        FILTER_PATTERNS.add(".+Copyright.*2012 IEEE.*");
        FILTER_PATTERNS.add("Figure \\d+-\\d+.*");

        FILTER_LINE_PATTERN.add("Authorized licensed use limited to: Rose Kung.");
        FILTER_LINE_PATTERN.add("Downloaded on 25-May-2012 fromthe IEEEStandards Store.");
        FILTER_LINE_PATTERN.add("Restrictions apply.");
        FILTER_LINE_PATTERN.add("Copyright IEEE.");
    }


    public static void main(String[] args) throws IOException {
        String input = "F:\\ym\\80211-2012\\802.11-2012.pdf";
        String output = "F:\\ym\\80211-2012\\802.11-2012.text";
        String docPath="C:\\Users\\ZengHu\\Desktop\\temp.docx";
        int startPage = 271;
        int endPage = 272;
        //extractTextFromPdf(input, output, startPage, endPage);
        WordUtils.readWord(docPath);
    }


    public static void extractTextFromPdf(String inputPath, String outputPath, int startPage, int endPage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, true))) {
            for (int start = startPage; start <= endPage; start += MAX_PAGES) {
                String content = PdfHelper.getPdfContentByPage(inputPath, start, Math.min(start + MAX_PAGES, endPage));
                List<String> split = TextUtils.splitInline(content, "\r|\n");
                List<String> list = TextUtils.stripLineByPattern(split, FILTER_PATTERNS);
                list = TextUtils.combineLine(list);
                for (String line : list) {
                    for (String filterLine : FILTER_LINE_PATTERN) {
                        line = line.replace(filterLine, "");
                    }
                    writer.write(line + "\n");
                }
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
