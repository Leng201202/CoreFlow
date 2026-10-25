package com.coreflow.banking.common.entity;

import java.time.OffsetDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass 
@EntityListeners (AuditingEntityListener.class)
@Getter 
@Setter 
public abstract class BaseEntity {

    @Column (name="active",nullable = false)
    private boolean active=true;

    @CreatedDate 
    @Column (name="created_at",nullable = false,updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate 
    @Column (name="updated_at")
    private OffsetDateTime updatedAt;

    @Column (name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column (name="created_by")
    private Long createdBy;

    @Column (name="updated_by")
    private Long updatedBy;

    @Column (name="deleted_by")
    private Long deletedBy;
    
}
