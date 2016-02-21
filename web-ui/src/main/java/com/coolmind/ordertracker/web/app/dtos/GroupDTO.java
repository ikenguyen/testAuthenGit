package com.coolmind.ordertracker.web.app.dtos;

import com.coolmind.ordertracker.core.model.Group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by LiemTran on 1/22/16.
 */
public class GroupDTO {

    private long id;
    private String name;
    private String desc;

    private List<Long> roles;

    public GroupDTO() {
    }

    public GroupDTO(long id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
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

    public List<Long> getRoles() {
        return roles;
    }

    public void setRoles(List<Long> roles) {
        this.roles = roles;
    }

    public static GroupDTO fromGroup(Group group){
        return new GroupDTO(group.getId(), group.getName(), group.getDesc());
    }

    public static List<GroupDTO> fromGroupList(Collection<Group> groups){

        List<GroupDTO> groupDTOList = new ArrayList<>();

        for (Group grp : groups){
            groupDTOList.add(fromGroup(grp));
        }

        return groupDTOList;

    }
}
