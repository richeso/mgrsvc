package com.mapr.mgrsvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class MgrServiceApplication {

  public static void main(final String[] args) {
    SpringApplication.run(MgrServiceApplication.class, args);
  }

}
