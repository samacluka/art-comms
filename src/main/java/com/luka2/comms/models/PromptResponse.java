package com.luka2.comms.models;

import lombok.Data;

import java.util.List;

@Data
public class PromptResponse {
    List<String> children;
}
