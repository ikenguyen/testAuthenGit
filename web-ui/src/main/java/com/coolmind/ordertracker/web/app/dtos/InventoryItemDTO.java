package com.coolmind.ordertracker.web.app.dtos;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Administrator on 2/3/2016.
 */
public class InventoryItemDTO {

    private Long   id;
    private Long   version;
    private String code;
    private String name;
    private String city;
    private String country;
    private String phone;
    private String desc;

    private List<Object> addressList = Lists.newArrayList();
}
