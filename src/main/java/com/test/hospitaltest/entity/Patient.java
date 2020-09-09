package com.test.hospitaltest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    private UUID id;
    private String name;
    private Location location;
    private short age;
    private int acceptedOffers;
    private int canceledOffers;
    private int averageReplyTime;


}
