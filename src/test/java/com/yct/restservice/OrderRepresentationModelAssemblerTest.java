package com.yct.restservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.assertj.core.api.Assertions.assertThat;

class OrderRepresentationModelAssemblerTest {
  private OrderRepresentationModelAssembler assembler;

  @BeforeEach
  void setUp() {
    assembler = new OrderRepresentationModelAssembler();
  }

  @Test
  void toModelOrderStatusCompleted() {
    Order order = new Order("from Ohio", Status.COMPLETED);
    EntityModel<Order> orderEntityModel = assembler.toModel(order);
    assertThat(orderEntityModel.getContent()).isEqualTo(order);
    assertThat(orderEntityModel.getLinks("orders").get(0).getHref()).isEqualTo("/orders");
    assertThat(orderEntityModel.getLinks("self").get(0).getRel().toString()).isEqualTo("self");
    assertThat(orderEntityModel.getLinks("self").get(0).getHref()).isEqualTo("/orders/{id}");
    assertThat(orderEntityModel.getLinks("cancel")).isEmpty();
    assertThat(orderEntityModel.getLinks("complete")).isEmpty();
  }

  @Test
  void toModelOrderStatusInProgress() {
    Order order = new Order("from Texas", Status.IN_PROGRESS);
    EntityModel<Order> orderEntityModel = assembler.toModel(order);
    assertThat(orderEntityModel.getContent()).isEqualTo(order);
    assertThat(orderEntityModel.getLinks("orders").get(0).getHref()).isEqualTo("/orders");
    assertThat(orderEntityModel.getLinks("self").get(0).getRel().toString()).isEqualTo("self");
    assertThat(orderEntityModel.getLinks("self").get(0).getHref()).isEqualTo("/orders/{id}");
    assertThat(orderEntityModel.getLinks("cancel").get(0).getHref()).isEqualTo("/orders/{id}/cancel");
    assertThat(orderEntityModel.getLinks("complete").get(0).getHref()).isEqualTo("/orders/{id}/complete");
  }
}
