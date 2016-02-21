package com.coolmind.ordertracker.web.app.controllers;

import com.coolmind.ordertracker.core.exception.ErrorCodeInstants;
import com.coolmind.ordertracker.core.model.Group;
import com.coolmind.ordertracker.core.model.User;
import com.coolmind.ordertracker.core.services.UserService;
import com.coolmind.ordertracker.web.app.dtos.UserDTO;
import com.coolmind.ordertracker.web.app.dtos.UserInfoDTO;
import com.coolmind.ordertracker.web.utils.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by LiemTran on 1/17/16.
**/

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetUserInfo() throws Exception {

    }

    @Test
    public void testFindAll() throws Exception {
        User usr1 = new User("test1", "mr test 1", "pass123","tester@mail.com");
        usr1.setId(1l);
        User usr2 = new User("test2", "mr test 2", "pass345","tester2@mail.com");
        usr2.setId(2l);
        List<User> users = Arrays.asList(usr1, usr2);

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get(UserController.API_URL + "/all")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userName", is(usr1.getUsername())))
                .andExpect(jsonPath("$[0].fullName", is(usr1.getFullname())))
                .andExpect(jsonPath("$[0].email",    is(usr1.getEmail())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].userName", is(usr2.getUsername())))
                .andExpect(jsonPath("$[1].fullName", is(usr2.getFullname())))
                .andExpect(jsonPath("$[1].email",    is(usr2.getEmail())));

        verify(userService, times(1)).findAll();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void findById() throws Exception {
        User usr = new User("test1", "mr test 1", "pass123","tester@mail.com");
        usr.setId(1l);

        when(userService.findById(1l)).thenReturn(usr);

        mockMvc.perform(get(UserController.API_URL + "/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userName", is(usr.getUsername())))
                .andExpect(jsonPath("$.fullName", is(usr.getFullname())))
                .andExpect(jsonPath("$.email",    is(usr.getEmail())));

        verify(userService, times(1)).findById(1l);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void TestFindRolesByUser() throws Exception {
        User usr = new User("test1", "mr test 1", "pass123","tester@mail.com");
        Group grp1 = new Group("grp1", "grp1 desc");
        grp1.setId(1l);
        Group grp2 = new Group("grp2", "grp2 desc");
        grp2.setId(2l);

        usr.setGroups(Arrays.asList(grp1, grp2));
        usr.setId(1l);

        when(userService.findByIdWithGroups(1l)).thenReturn(usr);

        mockMvc.perform(get(UserController.API_URL + "/1/groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(grp1.getName())))
                .andExpect(jsonPath("$[0].desc", is(grp1.getDesc())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is(grp2.getName())))
                .andExpect(jsonPath("$[1].desc", is(grp2.getDesc())));

        verify(userService, times(1)).findByIdWithGroups(1l);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void findByIdNotFound() throws Exception {

        when(userService.findById(1l)).thenReturn(null);

        MvcResult result = mockMvc.perform(get(UserController.API_URL + "/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andReturn();

        assertEquals(ErrorCodeInstants.NOT_FOUND, result.getResponse().getContentAsString());
        verify(userService, times(1)).findById(1l);
        verifyNoMoreInteractions(userService);
    }


    @Test
    public void testCreateUser() throws Exception {

        UserDTO dto = new UserDTO();


        when(userService.createUser(dto.getUserName(), dto.getFullName(), dto.getEmail(),
                            dto.getPassword(), null))
                .thenReturn(1l);

        MvcResult result =
            mockMvc.perform(post(UserController.API_URL)
                    .contentType(TestUtil.APPLICATION_JSON_UTF8)
                    .content(TestUtil.convertObjectToJsonBytes(dto))
            )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(String.valueOf(1l), content);
        verify(userService, times(1)).createUser(dto.getUserName(), dto.getFullName(), dto.getEmail(),
                dto.getPassword(), null);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testCreateUserThrowsException() throws Exception {

        UserDTO dto = new UserDTO();


        when(userService.createUser(dto.getUserName(), dto.getFullName(), dto.getEmail(),
                dto.getPassword(), null))
                .thenThrow(new Exception());

        MvcResult result =
                mockMvc.perform(post(UserController.API_URL)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                )
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                        .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(ErrorCodeInstants.UNIDENTIFIED, content);
        verify(userService, times(1)).createUser(dto.getUserName(), dto.getFullName(), dto.getEmail(),
                dto.getPassword(), null);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testUpdateUser() throws Exception {

        User usr = new User("tester", "Mr Test", "password", "tester@mail.com");
        usr.setId(1l);
        UserInfoDTO dto = UserInfoDTO.fromUser(usr);

        when(userService.findById(1l)).thenReturn(usr);
        when(userService.updateUser(usr, null)).thenReturn(usr);

        mockMvc.perform(post(UserController.API_URL + "/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userName", is(usr.getUsername())))
                .andExpect(jsonPath("$.fullName", is(usr.getFullname())))
                .andExpect(jsonPath("$.email",    is(usr.getEmail())));


        verify(userService, times(1)).findById(1l);
        verify(userService, times(1)).updateUser(usr, null);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testUpdateUserThrowsException() throws Exception {

        UserDTO dto = new UserDTO();
        User usr = new User("tester", "Mr Test", "password", "tester@mail.com");


        when(userService.findById(1l)).thenReturn(usr);
        when(userService.updateUser(usr, null)).thenThrow(new Exception());

        MvcResult result =
                mockMvc.perform(post(UserController.API_URL + "/1")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                )
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                        .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(ErrorCodeInstants.UNIDENTIFIED, content);
        verify(userService, times(1)).findById(1l);
        verify(userService, times(1)).updateUser(usr, null);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testDeleteUser() throws Exception {

        User usr = new User("tester", "Mr Test", "password", "tester@mail.com");
        usr.setId(1l);

        when(userService.deleteUser(1l)).thenReturn(usr);

        mockMvc.perform(delete(UserController.API_URL + "/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userName", is(usr.getUsername())))
                .andExpect(jsonPath("$.fullName", is(usr.getFullname())))
                .andExpect(jsonPath("$.email",    is(usr.getEmail())));


        verify(userService, times(1)).deleteUser(1l);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testDeleteUserThrowsException() throws Exception {

        User usr = new User("tester", "Mr Test", "password", "tester@mail.com");

        when(userService.deleteUser(1l)).thenThrow(new Exception());

        MvcResult result =
                mockMvc.perform(delete(UserController.API_URL + "/1")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                )
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                        .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(ErrorCodeInstants.UNIDENTIFIED, content);
        verify(userService, times(1)).deleteUser(1l);
        verifyNoMoreInteractions(userService);
    }
}
