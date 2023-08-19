package com.georgyorlov.accounting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "public_id")
    private UUID publicId;

    @Column(name = "user_public_id")
    private UUID userPublicId;

    @Column(name = "current_balance")
    private Long currentBalance;

}
