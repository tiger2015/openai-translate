package com.tiger.openai.util;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import com.tiger.openai.model.HttpJsonResponse;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Zenghu
 * @Date 2023年08月10日 21:40
 * @Description
 * @Version: 1.0
 **/
@Slf4j
public class OpenAiUtil {

    public static String url = "https://api.openai.com/v1/chat/completions";
    public static String apiKey = "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";
    public static String prompt = "Please behave like GPT-4 model, translate the following text into Chinese.";

    public static Map<String, String> headers;

    static {
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + apiKey);

    }

    public static HttpJsonResponse translate(String content) {
        Map<String, Object> data = new HashMap<>();
        data.put("model", "gpt-3.5-turbo");
        data.put("temperature", 0.7);
        data.put("max_tokens", 200);

        Map<String, Object> messages = new HashMap<>();
        messages.put("role", "user");
        messages.put("content", prompt + ":\n" + content);
        data.put("messages", messages);
        try {
            return HttpUtils.sendPostRequest(url, headers, data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static void translateWithApi(String content) {
        OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(600));
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt + ":\n" + content)
                .model("gpt-3.5-turbo")
                .echo(true)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
    }
}
