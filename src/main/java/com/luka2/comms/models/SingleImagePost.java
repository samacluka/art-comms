package com.luka2.comms.models;

import lombok.Data;

@Data
public class SingleImagePost {
    private int igMediaId;
    private int igContainerId;
    private int igUserId;
    private String image;
    private String imageName;
    private String caption;
}
