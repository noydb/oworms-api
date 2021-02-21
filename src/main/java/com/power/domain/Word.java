package com.power.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WORD")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Word {

    @Id
    @Column
    @GeneratedValue
    private Long id;

    @Column
    private String theWord;

    @Column
    private String definition;

    @Column
    private String pronunciation;

    @Column
    private String origin;

    @Column
    private String partOfSpeech;
}
