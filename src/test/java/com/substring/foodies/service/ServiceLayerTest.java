package com.substring.foodies.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceLayerTest {

    @Autowired
    private UserService userService;

    @Test
    public void testSaveUser()
    {
        userService.save();
    }

    @Test
    public void testUpdateUser()
    {
        userService.updateUser();
    }

}
