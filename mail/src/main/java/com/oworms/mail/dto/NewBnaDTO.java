package com.oworms.mail.dto;

import com.oworms.common.util.Utils;

import java.time.LocalDateTime;

public class NewBnaDTO {

    public static final String TEMPLATE = "new-b";
    public static final String ACTION = "new banana!";
    private String title;
    private String newBanana;
    private String eatBananaLink;

    public NewBnaDTO(final String newBanana, final String eatBananaLink) {
        this.title = "oworms | week #" + Utils.format(LocalDateTime.now(), "ww") + " | banana";

        this.newBanana = newBanana;
        this.eatBananaLink = eatBananaLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewBanana() {
        return newBanana;
    }

    public void setNewBanana(String newBanana) {
        this.newBanana = newBanana;
    }

    public String getEatBananaLink() {
        return eatBananaLink;
    }

    public void setEatBananaLink(String eatBananaLink) {
        this.eatBananaLink = eatBananaLink;
    }
}
