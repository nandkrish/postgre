package com.example.verticle;



import org.springframework.context.ApplicationContext;

import com.example.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;


/**
 * Simple verticle to wrap a Spring service bean - note we wrap the service call
 * in executeBlocking, because we know it's going to be a JDBC call which blocks.
 * As a general principle with Spring beans, the default assumption should be that it will block unless you
 * know for sure otherwise (in other words use executeBlocking unless you know for sure your service call will be
 * extremely quick to respond)
 */
public class EmployeeVerticle extends AbstractVerticle {

  public static final String ALL_EMP_ADDRESS = "example.all.emps";

  // Reuse the Vert.x Mapper, alternatively you can use your own.
  private final ObjectMapper mapper = Json.mapper;

  private final EmployeeService service;

  public EmployeeVerticle(final ApplicationContext context) {
    service = (EmployeeService) context.getBean("employeeService");
  }

  private Handler<Message<String>> allEmpsHandler(EmployeeService service) {
    // It is important to use an executeBlocking construct here
    // as the service calls are blocking (dealing with a database)
    return msg -> vertx.<String>executeBlocking(promise -> {
          try {
            promise.complete(mapper.writeValueAsString(service.getAllEmps()));
          } catch (JsonProcessingException e) {
            System.out.println("Failed to serialize result");
            promise.fail(e);
          }
        },
        result -> {
          if (result.succeeded()) {
            msg.reply(result.result());
          } else {
            msg.reply(result.cause().toString());
          }
        });
  }

  @Override
  public void start() throws Exception {
    super.start();
    vertx.eventBus().<String>consumer(ALL_EMP_ADDRESS).handler(allEmpsHandler(service));
  }
}

