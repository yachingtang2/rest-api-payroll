package com.yct.restservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeRepresentationModelAssemblerTest {
  private EmployeeRepresentationModelAssembler assembler;

  @BeforeEach()
  void setUp() {
    assembler = new EmployeeRepresentationModelAssembler();
  }

  @Test
  void convertEmployeeToModel() {
    Employee employee = new Employee("My", "Name", "my role");
    EntityModel<Employee> employeeEntityModel = assembler.toModel(employee);
    assertThat(employeeEntityModel.getContent()).isEqualTo(employee);
    assertThat(Objects.requireNonNull(employeeEntityModel.getContent()).getName()).isEqualTo("My Name");
    assertThat(employeeEntityModel.getLinks("self").get(0).getRel().toString()).isEqualTo("self");
    assertThat(employeeEntityModel.getLinks("self").get(0).getHref()).isEqualTo("/employees/{id}");
    assertThat(employeeEntityModel.getLinks("employees").get(0).getHref()).isEqualTo("/employees");
  }
}
