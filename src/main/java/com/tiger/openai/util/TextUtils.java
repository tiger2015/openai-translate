package com.tiger.openai.util;

import com.tiger.openai.model.LineContent;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zenghu
 * @Date 2023年08月10日 20:48
 * @Description
 * @Version: 1.0
 **/
public class TextUtils {

    public static List<String> splitInline(String text, String regex) {
        List<String> result = new ArrayList<>();
        final String[] subTexts = text.split(regex);
        for (String sub : subTexts) {
            final String trimText = sub.trim();
            if (trimText.length() > 0) {
                result.add(trimText);
            }
        }
        return result;
    }

    public static List<String> stripLineByPattern(List<String> lines, List<String> patterns) {
        List<String> result = new ArrayList<>();
        for (String line : lines) {
            int matchCount = 0;
            for (String pattern : patterns) {
                if (line.matches(pattern)) {
                    matchCount++;
                    break;
                }
            }
            if (matchCount == 0) {
                result.add(line);
            }

        }
        return result;
    }

    public static String combineLines(List<String> lines, String lineSeparator) {
        StringBuilder buffer = new StringBuilder();
        for (String line : lines) {
            buffer.append(line).append(lineSeparator);
        }
        return buffer.toString();
    }


    public static List<LineContent> splitTitleAndContent(List<String> lines) {
        List<LineContent> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            if (line.matches("\\d+\\.(\\d+\\.)+.*")) {
                if (builder.length() > 0) {
                    LineContent content = new LineContent();
                    content.setFlag(LineContent.CONTENT);
                    content.setContent(builder.toString());
                    result.add(content);
                    builder.delete(0, builder.length());
                }
                LineContent content = new LineContent();
                content.setFlag(LineContent.TITLE);
                content.setContent(line);
                result.add(content);
            } else {
                builder.append(line);
            }
        }
        if (builder.length() > 0) {
            LineContent content = new LineContent();
            content.setFlag(LineContent.CONTENT);
            content.setContent(builder.toString());
            result.add(content);
        }
        return result;
    }

    public static List<String> combineLine(List<String> lines) {
        List<String> result = new ArrayList<>();
        int index = 0;
        StringBuilder builder = new StringBuilder();
        while (index < lines.size()) {
            String line = lines.get(index);
            if (line.matches("(\\d{1,2}.)+\\d+\\s.*")) {
                result.add(line);
            } else {
                builder.append(line);
                if (line.endsWith(".")) {
                    result.add(builder.toString());
                    builder.delete(0, builder.length());
                }
            }
            index++;
        }
        return result;
    }


}
