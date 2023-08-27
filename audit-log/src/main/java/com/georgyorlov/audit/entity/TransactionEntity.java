package com.georgyorlov.audit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "public_id")
    private UUID publicId;

    @Column(name = "billing_public_id")
    private UUID billingPublicId;

    @Column(name = "user_public_id")
    private UUID userPublicId;

    @Column(name = "task_public_id")
    private UUID taskPublicId;

    @Column(name = "debit")
    private Long debit;

    @Column(name = "credit")
    private Long credit;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

}
