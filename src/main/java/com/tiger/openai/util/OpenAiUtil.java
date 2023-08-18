package com.tiger.openai.util;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import com.tiger.openai.model.HttpJsonResponse;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
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
    public static String prompt = "translate the following text into Chinese.";

    public static Map<String, String> headers;

    static {
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + apiKey);

    }

    public static HttpJsonResponse translate(String content) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("model", "text-davinci-001");
        data.put("temperature", 0.7);
        data.put("max_tokens", 40000);

        Map<String, Object> messages = new HashMap<>();
        messages.put("role", "user");
        messages.put("content", prompt + "\n" + content);
        data.put("messages", messages);
        try {
            return HttpUtils.sendPostRequest(url, headers, data);
        } catch (Exception e) {
            throw e;
        }
    }

    public static String translateWithApi(String content) {
        OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(60));
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt + "\n" + content)
                .model("ada")
                .temperature(0.5)
                .maxTokens(2000)
                .build();
        List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
        StringBuilder builder = new StringBuilder();
        for (CompletionChoice choice : choices) {
            builder.append(choice.getText());
        }
        return builder.toString();
    }
}
