package com.yct.restservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class LoadDatabase {

  @Bean
  CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
    return args -> {
      employeeRepository.save(new Employee("Test1", "Test2", "manager"));
      employeeRepository.save(new Employee("Test3", "Test4", "associate"));

      employeeRepository.findAll()
          .forEach(employee -> log.info("Preloaded " + employee));

      orderRepository.save(new Order("Description 1", Status.COMPLETED));
      orderRepository.save(new Order("Description 2", Status.IN_PROGRESS));
      orderRepository.save(new Order("Description 3", Status.COMPLETED));
      orderRepository.save(new Order("Description 4", Status.IN_PROGRESS));

      orderRepository.findAll()
          .forEach(order -> log.info("Preloaded " + order));
    };
  }
}
