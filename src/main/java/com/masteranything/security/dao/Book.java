package com.masteranything.security.dao;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
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
public class Book extends BaseEntityAuditAware {

    // unique=true: will be enable at some point
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String authorName;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String synopsis;

    private String bookCover;
    private boolean archived;

    @Column(nullable = false)
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;

    @OneToMany(mappedBy="book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy="book")
    private List<BookTransactionHistory> transactionHistories;

    @Transient
    public double getRate(){
        if(null == feedbacks || feedbacks.isEmpty()){
            return 0d;
        }

        var rate = this.feedbacks.stream()
            .mapToDouble(Feedback::getNote)
            .average()
            .orElse(0d);

        return Math.round(rate * 10d) / 10d;
    }
}
