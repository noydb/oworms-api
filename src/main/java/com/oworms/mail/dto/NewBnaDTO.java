package com.oworms.mail.dto;

import com.oworms.util.Utils;

import java.time.LocalDateTime;

public class NewBnaDTO extends EmailDTO {

    private String newBanana;
    private String eatBananaLink;

    public NewBnaDTO(final String newBanana, final String eatBananaLink) {
        super(
                "new-b",
                String.format(
                        "new-bna, oworms | week %s %s | banana",
                        Utils.format(LocalDateTime.now(), "ww"),
                        Utils.format(LocalDateTime.now(), "yyyy")
                )
        );

        this.newBanana = newBanana;
        this.eatBananaLink = eatBananaLink;
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
