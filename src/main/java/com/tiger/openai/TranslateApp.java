package com.tiger.openai;

import com.tiger.openai.model.HttpJsonResponse;
import com.tiger.openai.util.OpenAiUtil;
import com.tiger.openai.util.PdfHelper;
import com.tiger.openai.util.TextUtils;
import com.tiger.openai.util.WordUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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


    // 程序入口
    public static void main(String[] args) throws IOException {
        String input = "F:\\ym\\80211-2012\\802.11-2012.pdf";
        // 输出结果文件
        String output = "F:\\翻译\\802.11-2012-1400-1499.text";
        // 输入源文件，doc
        String docPath = "F:\\翻译\\802.11-2012-1400-1499.docx";
        int startPage = 271;
        int endPage = 272;
        //extractTextFromPdf(input, output, startPage, endPage);
        // 将word转换为文本，以段落为单元
        // int[] titleNumbers = {11, 8, 2, 6, 0};
        // WordUtils.readWordToText(docPath, output, titleNumbers);
        log.info("start");
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(30);
                String result = OpenAiUtil.translateWithApi("hello world");
                log.info("result:{}", result);
                if (result.length() > 0) {
                    break;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
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
