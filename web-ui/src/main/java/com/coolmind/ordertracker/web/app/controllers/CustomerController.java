package com.coolmind.ordertracker.web.app.controllers;


import com.coolmind.ordertracker.core.exception.BusinessException;
import com.coolmind.ordertracker.core.exception.ErrorCodeInstants;
import com.coolmind.ordertracker.core.model.Customer;
import com.coolmind.ordertracker.core.model.User;
import com.coolmind.ordertracker.core.services.CustomerService;
import com.coolmind.ordertracker.core.services.UserService;
import com.coolmind.ordertracker.web.app.dtos.CustomerDTO;
import com.coolmind.ordertracker.web.app.dtos.GroupDTO;
import com.coolmind.ordertracker.web.app.dtos.UserDTO;
import com.coolmind.ordertracker.web.app.dtos.UserInfoDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 *
 *  REST service for customers.
 *
 */

@RestController
@RequestMapping(value = CustomerController.API_URL, produces = {"application/json;charset=UTF-8"})
public class CustomerController {

    public static final String API_URL = "/customer";


    private static final Logger LOGGER = Logger.getLogger(CustomerController.class);

    @Autowired
    CustomerService customerService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<CustomerDTO> findAll(){
        return CustomerDTO.fromCustomerList(customerService.findAll());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{code}")
    public CustomerDTO findByCode(@PathVariable("code") String code) throws BusinessException {
        Customer cus = customerService.findByCode(code);

        if(cus == null) {
            throw new BusinessException("can't find customer with code :" + code, ErrorCodeInstants.NOT_FOUND);
        }

        return CustomerDTO.fromCustomer(cus);
    }

    /*@ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/groups")
    public List<GroupDTO> findGroupsByUser(@PathVariable("id") Long id) throws BusinessException {
        User user = userService.findByIdWithGroups(id);

        if(user == null) {
            throw new BusinessException("can't find user with id :" + id, ErrorCodeInstants.NOT_FOUND);
        }

        return GroupDTO.fromGroupList(user.getGroups());
    }*/

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST)
    public String createCustomer(@RequestBody CustomerDTO dto) throws BusinessException{

        String customerCode;
        Customer cus = CustomerDTO.toCustomer(dto);

        try {
            customerCode = customerService.createCustomer(cus);
        } catch (Exception ex){
            throw new BusinessException("can not create customer : " + dto.toString(),
                    ex, ErrorCodeInstants.UNIDENTIFIED);
        }

        return customerCode;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/{code}")
    public CustomerDTO updateCustomer(@RequestBody CustomerDTO dto, @PathVariable("code") String code)
            throws BusinessException{

        Customer cusToUpdate = CustomerDTO.toCustomer(dto);
        cusToUpdate.setCode(code);

        try{
            cusToUpdate = customerService.updateCustomer(cusToUpdate);
        } catch (Exception ex){
            throw new BusinessException("can not update customer : " + dto.toString(),ex, ErrorCodeInstants.UNIDENTIFIED);
        }

        return CustomerDTO.fromCustomer(cusToUpdate);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{code}")
    public CustomerDTO deleteCustomer(@PathVariable("code") String code) throws BusinessException{
       Customer cus = null;

        try{
            cus = customerService.deleteCustomer(code);
        } catch (Exception ex){
            throw new BusinessException("can not delete Customer with code: " + code, ex, ErrorCodeInstants.UNIDENTIFIED);
        }

        return CustomerDTO.fromCustomer(cus);
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
