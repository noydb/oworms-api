package com.power.oworms.mail.dto;

public class UpdatedWordEmailDTO {

    public static final String TEMPLATE = "oworm-update";
    public static final String ACTION = "A word was recently updated";
    private String title;
    private EmailWordDTO oldWord;
    private EmailWordDTO updatedWord;
    private String oldTags;
    private String newTags;
    private String retrievalLink;

    public UpdatedWordEmailDTO(String title, EmailWordDTO oldWord, EmailWordDTO updatedWord, String retrievalLink) {
        this.title = title;
        this.oldWord = oldWord;
        this.updatedWord = updatedWord;
        this.retrievalLink = retrievalLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EmailWordDTO getOldWord() {
        return oldWord;
    }

    public void setOldWord(EmailWordDTO oldWord) {
        this.oldWord = oldWord;
    }

    public EmailWordDTO getUpdatedWord() {
        return updatedWord;
    }

    public void setUpdatedWord(EmailWordDTO updatedWord) {
        this.updatedWord = updatedWord;
    }

    public String getOldTags() {
        return oldTags;
    }

    public void setOldTags(String oldTags) {
        this.oldTags = oldTags;
    }

    public String getNewTags() {
        return newTags;
    }

    public void setNewTags(String newTags) {
        this.newTags = newTags;
    }

    public String getRetrievalLink() {
        return retrievalLink;
    }

    public void setRetrievalLink(String retrievalLink) {
        this.retrievalLink = retrievalLink;
    }
}
