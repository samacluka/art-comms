package com.luka2.comms.services;

import com.luka2.comms.models.Account;

public interface InstagramService {
    void createPost(byte[] image, String caption, Account account) throws Exception;
    void createPost(Iterable<byte[]> image, String caption, Account account) throws Exception;

}
