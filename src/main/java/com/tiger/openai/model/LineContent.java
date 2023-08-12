package com.tiger.openai.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Zenghu
 * @Date 2023年08月10日 21:21
 * @Description
 * @Version: 1.0
 **/
@Data
public class LineContent implements Serializable {
    private static final long serialVersionUID = -1366493523472654389L;
    public static final int TITLE = 0;
    public static final int CONTENT = 1;
    private int flag;
    private String content;
}
