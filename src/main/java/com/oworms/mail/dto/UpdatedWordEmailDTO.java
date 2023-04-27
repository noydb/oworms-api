package com.oworms.mail.dto;

public class  UpdatedWordEmailDTO {

    public static final String TEMPLATE = "oworm-update";
    public static final String ACTION = "A word was recently updated";
    private String title;
    private String retrievalLink;
    private String editLink;
    private EmailWordDTO old;
    private EmailWordDTO updated;
    private String uuid;

    public UpdatedWordEmailDTO(String title, EmailWordDTO oldWord, EmailWordDTO updatedWord) {
        this.title = title;
        this.old = oldWord;
        this.updated = updatedWord;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public EmailWordDTO getOld() {
        return old;
    }

    public void setOld(EmailWordDTO old) {
        this.old = old;
    }

    public EmailWordDTO getUpdated() {
        return updated;
    }

    public void setUpdated(EmailWordDTO updated) {
        this.updated = updated;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
