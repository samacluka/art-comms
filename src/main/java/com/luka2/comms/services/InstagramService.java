package com.luka2.comms.services;

import com.luka2.comms.models.Account;

public interface InstagramService {
    Account createPost(byte[] image, String caption, Account account);
    Account createPost(Iterable<byte[]> image, String caption, Account account);

}
