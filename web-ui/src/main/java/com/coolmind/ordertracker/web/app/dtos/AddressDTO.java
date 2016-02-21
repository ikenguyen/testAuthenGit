package com.coolmind.ordertracker.web.app.dtos;

import com.coolmind.ordertracker.core.model.Address;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by liemtran on 1/28/2016.
 */
public class AddressDTO {

    private Long   id;
    private Integer index;
    private String partyName;
    private String address;
    private String city;
    private String phone;
    private String email;
    private String status;
    private Long   version;
    private boolean defaultAddr;

    public AddressDTO() {
    }

    public AddressDTO(Integer index, String partyName, String address, String city,
                      String phone, String email, String status) {
        this.index = index;
        this.partyName = partyName;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.email = email;
        this.status = status;
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

    public Integer getIndex() {
        return index;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDefaultAddr() {
        return defaultAddr;
    }

    public void setDefaultAddr(boolean defaultAddr) {
        this.defaultAddr = defaultAddr;
    }

    public static AddressDTO fromAddress(Address addr){
        AddressDTO dto = new AddressDTO(addr.getIndex(), addr.getPartyName(), addr.getAddress(), addr.getCity(),
                            addr.getPhone(), addr.getEmail() , addr.getStatus());
        dto.setId(addr.getId());
        dto.setVersion(addr.getVersion());
        dto.setDefaultAddr(addr.getDefaultAddr());
        return dto;
    }

    public static List<AddressDTO> fromAddressList(Collection<Address> addressList){

        List<AddressDTO> addressDTOs = new ArrayList<>();

        for (Address addr : addressList){
            addressDTOs.add(fromAddress(addr));
        }

        return addressDTOs;
    }

    public static Address toAddress(AddressDTO dto){
        Address addr = new Address(dto.getIndex(), dto.getPartyName(), dto.getAddress(), dto.getCity(),
                dto.getPhone());
        addr.setId(dto.getId());
        addr.setEmail(dto.getEmail());
        addr.setStatus(dto.getStatus());
        addr.setVersion(dto.getVersion());
        addr.setDefaultAddr(dto.isDefaultAddr());
        return addr;
    }

    public static Set<Address> toAddressSet(Collection<AddressDTO> addressDTOs){
        Set<Address> addressSet = Sets.newHashSet();
        for(AddressDTO dto : addressDTOs) {
            addressSet.add(toAddress(dto));
        }
        return addressSet;
    }
}
