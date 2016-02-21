package com.coolmind.ordertracker.web.app.controllers;


import com.coolmind.ordertracker.core.exception.BusinessException;
import com.coolmind.ordertracker.core.exception.ErrorCodeInstants;
import com.coolmind.ordertracker.core.model.Group;
import com.coolmind.ordertracker.core.model.Role;
import com.coolmind.ordertracker.core.model.User;
import com.coolmind.ordertracker.core.services.GroupService;
import com.coolmind.ordertracker.core.services.UserService;
import com.coolmind.ordertracker.web.app.dtos.GroupDTO;
import com.coolmind.ordertracker.web.app.dtos.RoleDTO;
import com.coolmind.ordertracker.web.app.dtos.UserDTO;
import com.coolmind.ordertracker.web.app.dtos.UserInfoDTO;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

/**
 *
 *  REST service for groups.
 *
 */

@RestController
@RequestMapping(value = GroupController.API_URL, produces = {"application/json;charset=UTF-8"})
public class GroupController {

    public static final String API_URL = "/group";


    private static final Logger LOGGER = Logger.getLogger(GroupController.class);


    @Autowired
    GroupService groupService;


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<GroupDTO> findAll(){
        return GroupDTO.fromGroupList(groupService.findAll());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/roles")
    public List<RoleDTO> findAllRoles(){
        return RoleDTO.fromRoleList(groupService.findAllRole());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public GroupDTO findById(@PathVariable("id") Long id) throws BusinessException {
        Group grp = groupService.findById(id);

        if(grp == null) {
            throw new BusinessException("can't find group with id :" + id, ErrorCodeInstants.NOT_FOUND);
        }

        return GroupDTO.fromGroup(grp);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/roles")
    public List<RoleDTO> findRolesByGroup(@PathVariable("id") Long id) throws BusinessException {
        Group grp = groupService.findByIdWithRoles(id);

        if (grp == null) {
            throw new BusinessException("can't find group with id :" + id, ErrorCodeInstants.NOT_FOUND);
        }

        return RoleDTO.fromRoleList(grp.getRoles());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST)
    public String createGroup(@RequestBody GroupDTO dto) throws BusinessException{
        Group grp = null;

        try {
             grp = groupService.createGroup(dto.getName(), dto.getDesc(), dto.getRoles());
        } catch (Exception ex){
            Throwable t = ex.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if ((t instanceof ConstraintViolationException)) {
                throw new BusinessException("can not update group : " + dto.toString(),
                        ex, ErrorCodeInstants.GROUP_NAME_EXISTED);
            } else {
                throw new BusinessException("can not update group : " + dto.toString(),
                        ex, ErrorCodeInstants.UNIDENTIFIED);
            }

        }

        return String.valueOf(grp.getId());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/{id}")
    public GroupDTO updateGroup(@RequestBody GroupDTO dto, @PathVariable("id") Long id) throws BusinessException{
        Group grp = groupService.findById(id);
        if (grp == null) {
            throw  new BusinessException("group is not found", ErrorCodeInstants.NOT_FOUND);
        }

        grp.setName(dto.getName());
        grp.setDesc(dto.getDesc());

        try{
            grp = groupService.updateGroup(grp, dto.getRoles());
        } catch (Exception ex){
            Throwable t = ex.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if ((t instanceof ConstraintViolationException)) {
                throw new BusinessException("can not update group : " + grp.toString(),
                        ex, ErrorCodeInstants.GROUP_NAME_EXISTED);
            } else {
                throw new BusinessException("can not update group : " + grp.toString(),
                        ex, ErrorCodeInstants.UNIDENTIFIED);
            }

        }

        return GroupDTO.fromGroup(grp);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public GroupDTO deleteGroup(@PathVariable("id") Long id) throws BusinessException{
        Group grp = null;

        try{
            grp = groupService.deleteGroup(id);
        } catch (Exception ex){
            throw new BusinessException("can not delete user with id: " + id, ex, ErrorCodeInstants.UNIDENTIFIED);
        }

        return GroupDTO.fromGroup(grp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> errorHandler(Exception exc) {
        LOGGER.error(exc.getMessage(), exc);
        ResponseEntity<String> responseEntity ;

        if(exc instanceof BusinessException) {
            responseEntity = new ResponseEntity<String>(((BusinessException) exc).getErrorCode(),
                    HttpStatus.BAD_REQUEST);
        } else {
            responseEntity = new ResponseEntity<String>(exc.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

}
