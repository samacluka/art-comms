package com.luka2.comms.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "prompts")
public class Prompt {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name="text", nullable=false)
    private String text;

    @Column(name="post_at")
    private Date postAt;

    @Column(name="posted_at")
    private Date postedAt;

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Prompt parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Prompt> children;

    @ManyToOne
    @JoinColumn(name="account_id", referencedColumnName="id", nullable=false)
    private Account account;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="post_id", referencedColumnName="id")
    private Post post;
}
