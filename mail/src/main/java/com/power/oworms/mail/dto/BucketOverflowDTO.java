package com.power.oworms.mail.dto;

import com.power.oworms.common.util.Utils;

import java.time.LocalDateTime;

public class BucketOverflowDTO {

    public static final String TEMPLATE = "bucket-overflow";
    public static final String ACTION = "bucket token limit reached";
    private String title;
    private String className;
    private String context;

    public BucketOverflowDTO(String className, String context) {
        this.title = "oworms | " + Utils.format(LocalDateTime.now(), "yyyy-MM-dd") + " | bucket overflow";

        this.className = className;
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
