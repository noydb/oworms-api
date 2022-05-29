package com.oworms.mail.dto;

public class NewWordEmailDTO {

    public static final String TEMPLATE = "oworm-new";
    public static final String ACTION = "Summary";
    private String title;
    private EmailWordDTO word;
    private String retrievalLink;
    private String editLink;

    public NewWordEmailDTO(String title, EmailWordDTO word, String retrievalLink) {
        this.title = title;
        this.word = word;
        this.retrievalLink = retrievalLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EmailWordDTO getWord() {
        return word;
    }

    public void setWord(EmailWordDTO word) {
        this.word = word;
    }

    public String getRetrievalLink() {
        return retrievalLink;
    }

    public void setRetrievalLink(String retrievalLink) {
        this.retrievalLink = retrievalLink;
    }

    public String getEditLink() {
        return editLink;
    }

    public void setEditLink(String editLink) {
        this.editLink = editLink;
    }
}
