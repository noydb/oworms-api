package com.power.oworms.mail.dto;

import com.power.oworms.common.util.Utils;

import java.time.LocalDateTime;

public class DailyReportDTO {

    public static final String TEMPLATE = "daily-report";
    public static final String ACTION = "new banana!";
    private String title;
    private String newBanana;
    private String eatBananaLink;

    public DailyReportDTO(String newBanana, String eatBananaLink) {
        this.title = "oworms | " + Utils.format(LocalDateTime.now(), "yyyy-MM-dd") + " | banana";

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
