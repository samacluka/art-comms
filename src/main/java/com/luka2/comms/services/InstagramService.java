package com.luka2.comms.services;

import com.luka2.comms.models.Account;

public interface InstagramService {
    void createSingleImagePost(String image, String caption, Account account);

}
