package com.yct.restservice;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "CUSTOMER_ORDER")
class Order {

  private @Id @GeneratedValue Long id;
  private String description;
  private Status status;

  Order() {}

  Order(String description, Status status) {
    this.description = description;
    this.status = status;
  }
}
