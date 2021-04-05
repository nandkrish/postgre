package com.example.postgresql;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.verticle.ServerVerticle;
import com.example.verticle.EmployeeVerticle;

import io.vertx.core.Vertx;

/**
 * Runner for the vertx-spring sample
 *
 */
public class PostgreVertexApplication {

  public static void main( String[] args ) {
    ApplicationContext context = new AnnotationConfigApplicationContext(PostgresqlApplication.class);
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new EmployeeVerticle(context));
    vertx.deployVerticle(new ServerVerticle());
    System.out.println("Deployment done");
  }

}
