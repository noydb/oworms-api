package com.oworms.mail.dto;

public abstract class EmailDTO {

    private final String template;

    private final String title;

    protected EmailDTO(final String template, final String title) {
        this.template = template;
        this.title = title;
    }

    public String getTemplate() {
        return template;
    }

    public String getTitle() {
        return title;
    }
}
