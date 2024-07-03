package com.delivery.camunda.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    private static final long serialVersionUID = 24466982L;
    private String id;
    private List<String>items;
    private String address;
    private Date foodReady;
    private Date delivered;
    private String processId;
}
