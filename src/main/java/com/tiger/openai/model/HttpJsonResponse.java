package com.tiger.openai.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author Zenghu
 * @Date 2023年08月10日 19:58
 * @Description
 * @Version: 1.0
 **/
@Data
public class HttpJsonResponse implements Serializable {
    private static final long serialVersionUID = 3639942514578980622L;
    private int statusCode;
    private Map<String, String> headers;
    private String body;
}
