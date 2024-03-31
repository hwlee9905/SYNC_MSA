package com.simple.book.domain.user.entity;

import com.simple.book.domain.user.util.Address;
import com.simple.book.domain.user.util.ProfileImage;
import com.simple.book.domain.user.util.Role;
import com.simple.book.domain.user.util.Sex;
import com.simple.book.global.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String nickname;
    @Embedded
    private Address address;
    @Embedded
    private ProfileImage profileImage;
    @Lob
    private String introduction;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne
    @JoinColumn(name = "authentication_id")
    private Authentication authentication;
}
