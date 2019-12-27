package com.yct.restservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderControllerAdviceTest {
  private OrderControllerAdvice advice;

  @BeforeEach
  void setUp() {
    advice = new OrderControllerAdvice();
  }

  @Test
  void handleException() {
    assertThat(advice.orderNotFoundHandler(new OrderNotFoundException(9L))).isEqualTo("Could not find order 9");
  }
}
