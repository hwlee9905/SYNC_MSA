package com.simple.book.domain.user.entity;

import com.simple.book.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity(name = "email_verification")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerification extends BaseTimeEntity {
    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "email")
    private String email;
}
