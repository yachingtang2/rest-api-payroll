package com.yct.restservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeControllerAdviceTest {

  private EmployeeControllerAdvice advice;

  @BeforeEach
  void setUp() {
    advice = new EmployeeControllerAdvice();
  }

  @Test
  void employeeNotFoundException() {
    assertThat(advice.employeeNotFoundHandler(new EmployeeNotFoundException(1L))).isEqualTo("Could not find employee 1");
  }
}
