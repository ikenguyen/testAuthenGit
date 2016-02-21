package com.coolmind.ordertracker.web.app.dtos;

import com.coolmind.ordertracker.core.model.Customer;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by liemtran on 1/28/2016.
 */
public class CustomerDTO {

    private Long   id;
    private Long   version;
    private String code;
    private String name;
    private String city;
    private String country;
    private String phone;
    private String desc;

    private List<AddressDTO> addressList = Lists.newArrayList();

    public CustomerDTO() {
    }

    public CustomerDTO(String name, String city, String country, String phone, String desc) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.desc = desc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<AddressDTO> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressDTO> addressList) {
        this.addressList = addressList;
    }

    public static CustomerDTO fromCustomer(Customer customer){
        CustomerDTO dto = new CustomerDTO(customer.getName(), customer.getCity(),
                customer.getCountry(),customer.getPhone(), customer.getDesc());
        dto.setId(customer.getId());
        dto.setCode(customer.getCode());
        dto.setVersion(customer.getVersion());
        dto.setAddressList(AddressDTO.fromAddressList(customer.getAddressSet()));
        return dto;
    }

    public static List<CustomerDTO> fromCustomerList(Collection<Customer> customers){
        List<CustomerDTO> customerDTOs = new ArrayList<>();

        for (Customer cus : customers){
            customerDTOs.add(fromCustomer(cus));
        }

        return customerDTOs;
    }

    public static Customer toCustomer(CustomerDTO dto){
        Customer cus = new Customer(dto.getName(), dto.getCity(),
                dto.getCountry(),dto.getPhone());
        cus.setId(dto.getId());
        cus.setDesc(dto.getDesc());
        cus.setVersion(dto.getVersion());
        cus.setAddressSet(AddressDTO.toAddressSet(dto.getAddressList()));
        return cus;
    }


}
