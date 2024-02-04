package com.luka2.comms.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "settings")
public class Setting {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name="num_children_in_prompt_generation")
    private Integer numChildrenInPromptGeneration;

}
