package com.cloud.ordertrackingapp.api;

import lombok.Data;

@Data
public class OrderDTO {

    private Long userId;
    private String productName;
    private Integer count;
    private String address;

}
