package com.luka2.comms.services;

import com.luka2.comms.models.Account;
import com.luka2.comms.models.Image;
import com.luka2.comms.models.Prompt;

import java.util.Set;

public interface AIService {

    Image image(Prompt prompt);
    Set<Prompt> prompts(Account account);

}
