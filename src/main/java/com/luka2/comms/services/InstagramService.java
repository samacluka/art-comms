package com.luka2.comms.services;

import com.luka2.comms.models.Account;
import com.luka2.comms.models.Image;

import java.util.Set;

public interface InstagramService {
    Account createPost(Image image, String caption, Account account);
    Account createPost(Set<Image> image, String caption, Account account);

}
