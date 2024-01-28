package com.luka2.comms.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "posts")
public class Post {

    public static final int MAX_IMAGE_NUM = 10;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name="ig_container_id")
    private Long igContainerId;

    @Column(name="ig_media_id")
    private Long igMediaId;

    @Column(name = "caption")
    private String caption;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Image> images;

    @ManyToOne
    @JoinColumn(name="account_id", nullable=false)
    private Account account;

    public Set<Long> getImageContainerIds(){
        return this.getImages().stream()
                .map(Image::getIgContainerId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public boolean hasMultipleImages(){
        return this.images.size() > 1;
    }

    public static boolean validNumImages(int numImages){
        return numImages > 0 && numImages <= MAX_IMAGE_NUM;
    }
}
