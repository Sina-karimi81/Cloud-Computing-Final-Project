package com.cloud.ordertrackingapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "order_tb")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "prod_count")
    private Integer count;
    @Column(name = "address")
    private String address;
    @Column(name = "status")
    private String status;

}
