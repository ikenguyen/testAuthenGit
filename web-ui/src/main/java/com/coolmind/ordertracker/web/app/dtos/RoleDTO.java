package com.coolmind.ordertracker.web.app.dtos;

import com.coolmind.ordertracker.core.model.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by LiemTran on 1/23/16.
 */
public class RoleDTO {

    private long id;
    private String name;
    private String desc;

    public RoleDTO(long id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public RoleDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static RoleDTO fromRole(Role role){
        return new RoleDTO(role.getId(), role.getName(), role.getDesc());
    }

    public static List<RoleDTO> fromRoleList(Collection<Role> roles){

        List<RoleDTO> roleDTOList = new ArrayList<>();

        for (Role role : roles){
            roleDTOList.add(fromRole(role));
        }

        return roleDTOList;

    }
}
