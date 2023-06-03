package com.oworms.mail.dto;

public class UpdatedWordEmailDTO extends EmailDTO {

    private String retrievalLink;
    private String editLink;
    private EmailWordDTO old;
    private EmailWordDTO updated;
    private String uuid;

    public UpdatedWordEmailDTO(final String title, EmailWordDTO oldWord, EmailWordDTO updatedWord) {
        super("oworm-update", title);
        this.old = oldWord;
        this.updated = updatedWord;
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
