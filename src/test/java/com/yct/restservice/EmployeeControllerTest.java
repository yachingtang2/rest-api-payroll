package com.yct.restservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeControllerTest {

  @Autowired
  private MockMvc mvc;

  @Test
  @Order(1)
  void getAllEmployees() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/employees").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("{\"_embedded\":{\"employeeList\":[{\"id\":1,\"firstName\":\"Test1\",\"lastName\":\"Test2\",\"name\":\"Test1 Test2\",\"role\":\"manager\",\"_links\":{\"self\":{\"href\":\"http://localhost/employees/1\"},\"employees\":{\"href\":\"http://localhost/employees\"}}},{\"id\":2,\"firstName\":\"Test3\",\"lastName\":\"Test4\",\"name\":\"Test3 Test4\",\"role\":\"associate\",\"_links\":{\"self\":{\"href\":\"http://localhost/employees/2\"},\"employees\":{\"href\":\"http://localhost/employees\"}}}]},\"_links\":{\"self\":{\"href\":\"http://localhost/employees\"}}}"));
  }

  @Test
  @Order(2)
  void saveNewEmployee() throws Exception {
    String requestJson = buildRequestJson(new Employee("Test5", "Test6", "policeman"));
    mvc.perform(MockMvcRequestBuilders.post("/employees")
          .contentType(MediaType.APPLICATION_JSON)
          .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(content().string("{\"id\":7,\"firstName\":\"Test5\",\"lastName\":\"Test6\",\"name\":\"Test5 Test6\",\"role\":\"policeman\",\"_links\":{\"self\":{\"href\":\"http://localhost/employees/7\"},\"employees\":{\"href\":\"http://localhost/employees\"}}}"));
  }

  static String buildRequestJson(Object object) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
    return objectWriter.writeValueAsString(object);
  }

  @Test
  @Order(3)
  void getEmployeeFound() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/employees/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("{\"id\":1,\"firstName\":\"Test1\",\"lastName\":\"Test2\",\"name\":\"Test1 Test2\",\"role\":\"manager\",\"_links\":{\"self\":{\"href\":\"http://localhost/employees/1\"},\"employees\":{\"href\":\"http://localhost/employees\"}}}"));
  }

  @Test
  @Order(4)
  void getEmployeeNotFound() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/employees/99"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Could not find employee 99"));
  }

  @Test
  @Order(5)
  void replaceEmployee() throws Exception {
    String requestJson = buildRequestJson(new Employee("Test33", "Test44", "teacher"));
    mvc.perform(MockMvcRequestBuilders.put("/employees/2")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(content().string("{\"id\":2,\"firstName\":\"Test33\",\"lastName\":\"Test44\",\"name\":\"Test33 Test44\",\"role\":\"teacher\",\"_links\":{\"self\":{\"href\":\"http://localhost/employees/2\"},\"employees\":{\"href\":\"http://localhost/employees\"}}}"));
  }

  @Test
  @Order(6)
  void replaceEmployeeNotFound() throws Exception {
    String requestJson = buildRequestJson(new Employee("Test77", "Test88", "dad"));
    mvc.perform(MockMvcRequestBuilders.put("/employees/9")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(content().string("{\"id\":8,\"firstName\":\"Test77\",\"lastName\":\"Test88\",\"name\":\"Test77 Test88\",\"role\":\"dad\",\"_links\":{\"self\":{\"href\":\"http://localhost/employees/8\"},\"employees\":{\"href\":\"http://localhost/employees\"}}}"));
  }

  @Test
  @Order(7)
  void deleteEmployee() throws Exception {
    mvc.perform(MockMvcRequestBuilders.delete("/employees/1"))
        .andExpect(status().isNoContent());
  }
}
