package com.revature;

import com.revature.models.User;
import com.revature.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UserServiceTest {

    private UserService userService;

    @Before
    public void setup() {

        userService = mock(UserService.class);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userService.getAllUsers();

        Assert.assertTrue(users.isEmpty());

        verify(userService).getAllUsers();
    }



}
