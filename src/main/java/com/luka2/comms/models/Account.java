package com.luka2.comms.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name="ig_user_id")
    private Long igUserId;

    @Column(name="name", length=100, nullable=false, unique=false)
    private String name;

    @OneToMany(mappedBy = "account")
    private Set<Post> posts;
}
