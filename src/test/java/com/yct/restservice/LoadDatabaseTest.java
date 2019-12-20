package com.yct.restservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class LoadDatabaseTest {
  private LoadDatabase loadDatabase;

  @Mock
  private EmployeeRepository repository;

  @BeforeEach
  void setUp() {
    loadDatabase = new LoadDatabase();
  }

  @Test
  void initDatabase() {
    CommandLineRunner commandLineRunner = loadDatabase.initDatabase(repository);
    assertThat(commandLineRunner, is(notNullValue()));
  }
}
