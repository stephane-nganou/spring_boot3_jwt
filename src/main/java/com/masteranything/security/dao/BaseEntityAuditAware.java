package com.masteranything.security.dao;


import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class BaseEntityAuditAware extends BaseEntity {

    @CreatedBy
    @Column(nullable=false, updatable=false)
    private Long createdBy;
    @LastModifiedBy
    @Column(insertable=false)
    private Long lastModifiedBy;
    
}
