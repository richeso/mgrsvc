package com.example.springbootexample;

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
      UnixUser u = new PAM("sshd").authenticate("cheso", "weallscream");
      System.out.println(u.getUID());
      System.out.println(u.getGroups());
      System.out.println(u.getUserName());
      return ResponseEntity.ok("userid=" + u.getUID() + " username=" + u.getUserName());
    } catch (Exception e) {
      return ResponseEntity.ok(e.getMessage());
    }
  }

}
