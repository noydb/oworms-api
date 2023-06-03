package com.oworms.mail.dto;

public class NewWordEmailDTO extends EmailDTO {

    private EmailWordDTO word;
    private String retrievalLink;
    private String editLink;

    public NewWordEmailDTO(final String title, final EmailWordDTO word, final String retrievalLink) {
        super("oworm-new", title);
        this.word = word;
        this.retrievalLink = retrievalLink;
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
