package com.techstack.api.domain.entity;

import com.techstack.api.domain.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class AppUser extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Account> accounts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Address> addresses;
}
