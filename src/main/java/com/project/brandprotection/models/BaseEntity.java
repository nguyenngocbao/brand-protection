package com.project.brandprotection.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Base.ID, updatable = false)
    protected Long id;

    @CreatedDate
    @Column(name = Base.CREATED_AT)
    protected LocalDateTime createdAt;

    @CreatedBy
    @Column(name = Base.CREATED_BY)
    protected String createdBy;

    @LastModifiedDate
    @Column(name = Base.UPDATED_AT)
    protected LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = Base.UPDATED_BY)
    protected String updatedBy;
}

class Base {
    static final String ID = "ID";
    static final String CREATED_AT = "CREATED_AT";
    static final String CREATED_BY = "CREATED_BY";
    static final String UPDATED_AT = "UPDATED_AT";
    static final String UPDATED_BY = "UPDATED_BY";
    private Base() {
    }
}
