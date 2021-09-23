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
import java.time.LocalDate;

@Entity
@Table(name = "SECURITY_DATA")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SecurityData {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private int dailyRetrievalCount;

    @Column
    private int dailyActionCount;

}
