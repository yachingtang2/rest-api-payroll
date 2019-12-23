package com.yct.restservice;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderControllerTest {

  @Autowired
  private MockMvc mvc;

  @Test
  @org.junit.jupiter.api.Order(9)
  void getAllOrders() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/orders").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("{\"_embedded\":{\"orderList\":[{\"id\":3,\"description\":\"Description 1\",\"status\":\"COMPLETED\",\"_links\":{\"orders\":{\"href\":\"http://localhost/orders\"},\"self\":{\"href\":\"http://localhost/orders/3\"}}},{\"id\":4,\"description\":\"Description 2\",\"status\":\"IN_PROGRESS\",\"_links\":{\"orders\":{\"href\":\"http://localhost/orders\"},\"self\":{\"href\":\"http://localhost/orders/4\"},\"cancel\":{\"href\":\"http://localhost/orders/4/cancel\"},\"complete\":{\"href\":\"http://localhost/orders/4/complete\"}}}]},\"_links\":{\"self\":{\"href\":\"http://localhost/orders\"}}}"));
  }

  @Test
  @org.junit.jupiter.api.Order(10)
  void getOrder() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/orders/3"))
        .andExpect(status().isOk())
        .andExpect(content().string("{\"id\":3,\"description\":\"Description 1\",\"status\":\"COMPLETED\",\"_links\":{\"orders\":{\"href\":\"http://localhost/orders\"},\"self\":{\"href\":\"http://localhost/orders/3\"}}}"));
  }

  @Test
  @org.junit.jupiter.api.Order(11)
  void getOrderNotFound() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/orders/99"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Could not find order 99"));
  }

  @Test
  @org.junit.jupiter.api.Order(12)
  void saveOrder() throws Exception {
    String requestJson = EmployeeControllerTest.buildRequestJson(new Order("Description 3", Status.IN_PROGRESS));
    mvc.perform(MockMvcRequestBuilders.post("/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(content().string("{\"id\":5,\"description\":\"Description 3\",\"status\":\"IN_PROGRESS\"," +
            "\"_links\":{\"orders\":{\"href\":\"http://localhost/orders\"},\"self\":{\"href\":\"http://localhost/orders/5\"},\"cancel\":{\"href\":\"http://localhost/orders/5/cancel\"},\"complete\":{\"href\":\"http://localhost/orders/5/complete\"}}}"));
  }

  @Test
  @org.junit.jupiter.api.Order(13)
  void cancelOrderStatusCompleted() throws Exception {
    mvc.perform(MockMvcRequestBuilders.delete("/orders/3/cancel"))
        .andExpect(status().isMethodNotAllowed())
        .andExpect(content().string("{\"logref\":\"Method not allowed\",\"message\":\"You can't cancel an order that is in the COMPLETED status\"}"));
  }

  @Test
  @org.junit.jupiter.api.Order(14)
  void cancelOrderStatusInProgress() throws Exception {
    mvc.perform(MockMvcRequestBuilders.delete("/orders/4/cancel"))
        .andExpect(status().isOk())
        .andExpect(content().string("{\"id\":4,\"description\":\"Description 2\",\"status\":\"CANCELLED\",\"_links\":{\"orders\":{\"href\":\"http://localhost/orders\"},\"self\":{\"href\":\"http://localhost/orders/4\"}}}"));
  }

  @Test
  @org.junit.jupiter.api.Order(15)
  void cancelOrderNotFound() throws Exception {
    mvc.perform(MockMvcRequestBuilders.delete("/orders/99/cancel"))
        .andExpect(status().isOk())
        .andExpect(content().string("{\"id\":4,\"description\":\"Description 2\",\"status\":\"CANCELLED\",\"_links\":{\"orders\":{\"href\":\"http://localhost/orders\"},\"self\":{\"href\":\"http://localhost/orders/4\"}}}"));
  }

  @Test
  @org.junit.jupiter.api.Order(16)
  void completeOrderStatusCompleted() throws Exception {
    mvc.perform(MockMvcRequestBuilders.put("/orders/5/complete"))
        .andExpect(status().isMethodNotAllowed())
        .andExpect(content().string("{\"logref\":\"Method not allowed\",\"message\":\"You can't complete an order that is in the COMPLETED status\"}"));
  }

  @Test
  @org.junit.jupiter.api.Order(16)
  void completeOrderStatusInProgress() throws Exception {
    mvc.perform(MockMvcRequestBuilders.put("/orders/6/complete"))
        .andExpect(status().isOk())
        .andExpect(content().string("{\"id\":6,\"description\":\"Description 4\",\"status\":\"COMPLETED\",\"_links\":{\"orders\":{\"href\":\"http://localhost/orders\"},\"self\":{\"href\":\"http://localhost/orders/6\"}}}"));
  }

  @Test
  @org.junit.jupiter.api.Order(17)
  void completeOrderNotFound() throws Exception {
    assertThrows(NestedServletException.class, () -> {mvc.perform(MockMvcRequestBuilders.put("/orders/99/complete"));});
//    mvc.perform(MockMvcRequestBuilders.put("/orders/99/complete"))
//        .andExpect(status().isOk());
  }

}
