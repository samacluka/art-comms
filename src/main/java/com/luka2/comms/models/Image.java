package com.luka2.comms.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "ig_container_id")
    private Long igContainerId;

    @Column(name = "image_data")
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name="post_id", nullable=false)
    private Post post;
}
