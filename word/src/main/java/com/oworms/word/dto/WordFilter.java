package com.oworms.word.dto;

import java.util.List;

public class WordFilter {

    private final String word;
    private final List<String> pos;
    private final String def;
    private final String origin;
    private final String example;
    private final List<String> tags;
    private final String note;
    private final String creator;
    private final List<String> uuids;

    public WordFilter(final String word,
                      List<String> pos,
                      String def,
                      String origin,
                      String example,
                      List<String> tags,
                      String note,
                      String creator,
                      List<String> uuids) {
        this.word = word;
        this.pos = pos;
        this.def = def;
        this.origin = origin;
        this.example = example;
        this.tags = tags;
        this.note = note;
        this.creator = creator;
        this.uuids = uuids;
    }

    public String getWord() {
        return word;
    }

    public List<String> getPos() {
        return pos;
    }

    public String getDef() {
        return def;
    }

    public String getOrigin() {
        return origin;
    }

    public String getExample() {
        return example;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getNote() {
        return note;
    }

    public String getCreator() {
        return creator;
    }

    public List<String> getUuids() {
        return uuids;
    }
}
