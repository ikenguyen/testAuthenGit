package com.coolmind.ordertracker.web.app.controllers;


import com.coolmind.ordertracker.core.exception.BusinessException;
import com.coolmind.ordertracker.core.exception.ErrorCodeInstants;
import com.coolmind.ordertracker.core.model.User;
import com.coolmind.ordertracker.core.services.UserService;
import com.coolmind.ordertracker.web.app.dtos.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 *
 *  REST service for users.
 *
 */

@RestController
@RequestMapping(value = UserController.API_URL, produces = {"application/json;charset=UTF-8"})
public class UserController {

    public static final String API_URL = "/user";


    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET)
    public UserInfoDTO getUserInfo(Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        return user != null ? new UserInfoDTO(user.getId(),user.getUsername(), user.getFullname(), user.getEmail()) : null;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<UserInfoDTO> findAll(){
        return UserInfoDTO.fromUserList(userService.findAll());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public UserInfoDTO findById(@PathVariable("id") Long id) throws BusinessException {
        User user = userService.findById(id);

        if(user == null) {
            throw new BusinessException("can't find user with id :" + id, ErrorCodeInstants.NOT_FOUND);
        }

        return UserInfoDTO.fromUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/groups")
    public List<GroupDTO> findGroupsByUser(@PathVariable("id") Long id) throws BusinessException {
        User user = userService.findByIdWithGroups(id);

        if(user == null) {
            throw new BusinessException("can't find user with id :" + id, ErrorCodeInstants.NOT_FOUND);
        }

        return GroupDTO.fromGroupList(user.getGroups());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST)
    public String createUser(@RequestBody UserDTO user) throws BusinessException{

        long id;

        try {
             id = userService.createUser(user.getUserName(), user.getFullName(), user.getEmail(),
                    user.getPassword(), user.getGroups());
        } catch (Exception ex){
            throw new BusinessException("can not create user : " + user.toString(),ex, ErrorCodeInstants.UNIDENTIFIED);
        }

        return String.valueOf(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/{id}")
    public UserInfoDTO updateUser(@RequestBody UserDTO dto, @PathVariable("id") Long id) throws BusinessException{
        User user = userService.findById(id);
        if (user == null) {
            throw  new BusinessException("user is not found", ErrorCodeInstants.NOT_FOUND);
        }

        user.setFullname(dto.getFullName());
        user.setEmail(dto.getEmail());

        try{
            user = userService.updateUser(user, dto.getGroups());
        } catch (Exception ex){
            throw new BusinessException("can not update user : " + user.toString(),ex, ErrorCodeInstants.UNIDENTIFIED);
        }

        return UserInfoDTO.fromUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public UserInfoDTO deleteUser(@PathVariable("id") Long id) throws BusinessException{
        User user = null;

        try{
            user = userService.deleteUser(id);
        } catch (Exception ex){
            throw new BusinessException("can not delete user with id: " + id, ex, ErrorCodeInstants.UNIDENTIFIED);
        }

        return UserInfoDTO.fromUser(user);
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
