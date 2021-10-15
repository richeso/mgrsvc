package com.mapr.mgrsvc;

import org.jvnet.libpam.PAM;
import org.jvnet.libpam.UnixUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingRestController {

  @RequestMapping(method = RequestMethod.GET, path = "/api/ping")
  public ResponseEntity<String> getPing() {
    try {
      return ResponseEntity.ok("pong");
    } catch (Exception e) {
      return ResponseEntity.ok(e.getMessage());
    }
  }

}
