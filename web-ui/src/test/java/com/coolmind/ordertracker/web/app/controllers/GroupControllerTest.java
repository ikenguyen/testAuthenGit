package com.coolmind.ordertracker.web.app.controllers;

import com.coolmind.ordertracker.core.exception.ErrorCodeInstants;
import com.coolmind.ordertracker.core.model.Group;
import com.coolmind.ordertracker.core.model.Role;
import com.coolmind.ordertracker.core.model.User;
import com.coolmind.ordertracker.core.services.GroupService;
import com.coolmind.ordertracker.core.services.UserService;
import com.coolmind.ordertracker.web.app.dtos.GroupDTO;
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
public class GroupControllerTest {

    @Mock
    private GroupService groupService;

    private MockMvc mockMvc;

    @InjectMocks
    private GroupController groupController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }


    @Test
    public void testFindAll() throws Exception {
        Group grp1 = new Group("grp1");
        grp1.setId(1l);
        Group grp2 = new Group("grp2");
        grp2.setId(2l);
        List<Group> groups = Arrays.asList(grp1, grp2);

        when(groupService.findAll()).thenReturn(groups);

        mockMvc.perform(get(GroupController.API_URL + "/all")
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

        verify(groupService, times(1)).findAll();
        verifyNoMoreInteractions(groupService);
    }

    @Test
    public void testFindAllRoles() throws Exception {
        Role role1 = new Role("role1");
        role1.setId(1l);
        Role role2 = new Role("role2");
        role2.setId(2l);

        List<Role> roles = Arrays.asList(role1, role2);

        when(groupService.findAllRole()).thenReturn(roles);

        mockMvc.perform(get(GroupController.API_URL + "/roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(role1.getName())))
                .andExpect(jsonPath("$[0].desc", is(role1.getDesc())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is(role2.getName())))
                .andExpect(jsonPath("$[1].desc", is(role2.getDesc())));

        verify(groupService, times(1)).findAllRole();
        verifyNoMoreInteractions(groupService);
    }

    @Test
    public void findById() throws Exception {
        Group grp1 = new Group("grp1");
        grp1.setId(1l);

        when(groupService.findById(1l)).thenReturn(grp1);

        mockMvc.perform(get(GroupController.API_URL + "/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(grp1.getName())))
                .andExpect(jsonPath("$.desc", is(grp1.getDesc())));

        verify(groupService, times(1)).findById(1l);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    public void testFindRolesByGroup() throws Exception {
        Role role1 = new Role("role1");
        role1.setId(1l);

        Group grp = new Group("grp1","this is grp1");
        grp.setId(1l);
        grp.setRoles(Arrays.asList(role1));

        when(groupService.findByIdWithRoles(1l)).thenReturn(grp);

        mockMvc.perform(get(GroupController.API_URL + "/1/roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(role1.getName())))
                .andExpect(jsonPath("$[0].desc", is(role1.getDesc())));


        verify(groupService, times(1)).findByIdWithRoles(1l);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    public void findByIdNotFound() throws Exception {

        when(groupService.findById(1l)).thenReturn(null);

        MvcResult result = mockMvc.perform(get(GroupController.API_URL + "/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andReturn();

        assertEquals(ErrorCodeInstants.NOT_FOUND, result.getResponse().getContentAsString());
        verify(groupService, times(1)).findById(1l);
        verifyNoMoreInteractions(groupService);
    }


    @Test
    public void testCreateGroup() throws Exception {

        Group    grp = new Group("grpName", "grpDesc");
        grp.setId(1l);
        GroupDTO dto = GroupDTO.fromGroup(grp);

        when(groupService.createGroup(dto.getName(), dto.getDesc(), null))
                .thenReturn(grp);

        MvcResult result =
            mockMvc.perform(post(GroupController.API_URL)
                    .contentType(TestUtil.APPLICATION_JSON_UTF8)
                    .content(TestUtil.convertObjectToJsonBytes(dto))
            )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(String.valueOf(1l), content);
        verify(groupService, times(1)).createGroup(dto.getName(), dto.getDesc(), null);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    public void testCreateGroupThrowsException() throws Exception {

        GroupDTO dto = new GroupDTO(1l, "grpName", "grpDesc");


        when(groupService.createGroup(dto.getName(), dto.getDesc(), dto.getRoles()))
                .thenThrow(new Exception());

        MvcResult result =
                mockMvc.perform(post(GroupController.API_URL)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                )
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                        .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(ErrorCodeInstants.UNIDENTIFIED, content);
        verify(groupService, times(1)).createGroup(dto.getName(), dto.getDesc(), dto.getRoles());
        verifyNoMoreInteractions(groupService);
    }

    @Test
    public void testUpdateGroup() throws Exception {

        Group grp = new Group("grpName", "grpDesc");
        grp.setId(1l);
        GroupDTO dto = GroupDTO.fromGroup(grp);
        dto.setRoles(Arrays.asList(1l, 2l));

        when(groupService.findById(1l)).thenReturn(grp);
        when(groupService.updateGroup(grp, Arrays.asList(1l, 2l))).thenReturn(grp);

        mockMvc.perform(post(GroupController.API_URL + "/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(grp.getName())))
                .andExpect(jsonPath("$.desc", is(grp.getDesc())));


        verify(groupService, times(1)).findById(1l);
        verify(groupService, times(1)).updateGroup(grp, Arrays.asList(1l, 2l));
        verifyNoMoreInteractions(groupService);
    }

    @Test
    public void testUpdateGroupThrowsException() throws Exception {

        Group grp = new Group("grpName", "grpDesc");
        grp.setId(1l);
        GroupDTO dto = GroupDTO.fromGroup(grp);


        when(groupService.findById(1l)).thenReturn(grp);
        when(groupService.updateGroup(grp,null)).thenThrow(new Exception());

        MvcResult result =
                mockMvc.perform(post(GroupController.API_URL + "/1")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                )
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                        .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(ErrorCodeInstants.UNIDENTIFIED, content);
        verify(groupService, times(1)).findById(1l);
        verify(groupService, times(1)).updateGroup(grp, null);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    public void testDeleteUser() throws Exception {

        Group grp = new Group("grpName", "grpDesc");
        grp.setId(1l);

        when(groupService.deleteGroup(1l)).thenReturn(grp);

        mockMvc.perform(delete(GroupController.API_URL + "/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(grp.getName())))
                .andExpect(jsonPath("$.desc", is(grp.getDesc())));


        verify(groupService, times(1)).deleteGroup(1l);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    public void testDeleteGroupThrowsException() throws Exception {

        when(groupService.deleteGroup(1l)).thenThrow(new Exception());

        MvcResult result =
                mockMvc.perform(delete(GroupController.API_URL + "/1")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                )
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                        .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(ErrorCodeInstants.UNIDENTIFIED, content);
        verify(groupService, times(1)).deleteGroup(1l);
        verifyNoMoreInteractions(groupService);
    }

    public void testSetRolesIntoGroup() throws Exception {

        Group grp = new Group("grpName", "grpDesc");
        grp.setId(1l);
        Role role1 = new Role("role1");
        role1.setId(1l);
        Role role2 = new Role("role2");
        role1.setId(2l);

        long[] roles = {1l, 2l};

        when(groupService.findById(1l)).thenReturn(grp);
        when(groupService.updateGroup(grp)).thenReturn(grp);

        mockMvc.perform(post(GroupController.API_URL + "/1/roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content("[1,2]")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(grp.getName())))
                .andExpect(jsonPath("$.desc", is(grp.getDesc())));


        verify(groupService, times(1)).findById(1l);
        verify(groupService, times(1)).updateGroup(grp);
        verifyNoMoreInteractions(groupService);
    }
}
