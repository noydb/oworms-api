package com.oworms.mail.dto;

import com.oworms.util.Utils;

import java.time.LocalDateTime;

public class BucketOverflowDTO extends EmailDTO {

    private String className;
    private String context;

    public BucketOverflowDTO(String className, String context) {
        super(
                "bucket-overflow",
                "oworms | " + Utils.format(LocalDateTime.now(), "yyyy-MM-dd") + " | bucket overflow"
        );

        this.className = className;
        this.context = context;
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
