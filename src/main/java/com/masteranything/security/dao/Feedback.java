package com.masteranything.security.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Feedback extends BaseEntityAuditAware{

    private Double note;
    private String comment;

    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;
}
