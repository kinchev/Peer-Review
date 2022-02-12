package com.telerik.peer.services;

import com.telerik.peer.models.Team;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.repositories.contracts.UserRepository;
import com.telerik.peer.services.contracts.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.telerik.peer.TestHelpers.createMockUser;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userServiceImpl;




    @Test
    void getAll_should_callRepository() {
        Mockito.when(userRepository.getAll())
                .thenReturn(new ArrayList<>());

        userServiceImpl.getAll();

        Mockito.verify(userRepository, Mockito.times(1)).getAll();
    }
    @Test
    void getById_should_returnUser_when_matchExist() {
        User mockUser = createMockUser();
        Mockito.when(userRepository.getById(mockUser.getId())).thenReturn(mockUser);

        User result = userServiceImpl.getById(mockUser.getId());

        Assertions.assertAll(
                () -> Assertions.assertEquals(mockUser.getId(), result.getId()),
                () -> Assertions.assertEquals(mockUser.getUsername(), result.getUsername()),
                () -> Assertions.assertEquals(mockUser.getPassword(), result.getPassword()),
                () -> Assertions.assertEquals(mockUser.getEmail(), result.getEmail()),
                () -> Assertions.assertEquals(mockUser.getNumber(), result.getNumber()),
                () -> Assertions.assertEquals(mockUser.getPhotoName(), result.getPhotoName())


        );
    }

}
