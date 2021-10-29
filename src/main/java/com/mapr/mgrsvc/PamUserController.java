package com.mapr.mgrsvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
/*
curl -H "Accept: application/json" -H "Content-type: application/json" \
-X POST -d '{"name":"Corey", "type":"Volume Create"}' http://localhost:8080/api/process

The direct "mount" (-v /etc/passwd:/etc/passwd -v /etc/shadow:/etc/shadow) of the authentication files only works
if you have a non-changing set of users. If you like to perform a useradd or passwd without needing to restart
the jupyterhub docker container, i propose the following workaround:

mount the whole /etc/ foldert into the container, e.g. -v /etc/:/jup_etc/
create a copy_script that copies the files into the /etc folder in the container
cp -f /jup_etc/passwd /etc/passwd; cp -f /jup_etc/shadow /etc/shadow; cp -f /jup_etc/group /etc/group
run the copy_script at startup of the jupyerhub docker container and every time
when /etc/passwd, /etc/group, /etc/shadow have changed
(inside the container with docker exec -it "containername" bash)

also needed to install sssd
RUN DEBIAN_FRONTEND=noninteractive apt-get -yqq install sssd
in my Dockerfile and do
-v /var/lib/sss/pipes/:/var/lib/sss/pipes/
Source: https://github.com/arcenik/docker-authfromhost/blob/63df5e63fcb92cd5fa26755e144e53a7678e803f/debian-buster-slim/Dockerfile#L13
 */
@RestController
public class PamUserController {


    private static final Logger log = LoggerFactory.getLogger(PamUserController.class);

    @RequestMapping(value = "/api/pamuser",method = RequestMethod.POST,consumes="application/json")
    public ResponseEntity<String> process(@RequestBody Map<String, Object> payload) throws Exception {
        //System.out.println(payload);
        String userid = (String) payload.get("userid");
        String password = (String) payload.get("password");
        return authenticate(userid, password);
    }
    @GetMapping("/api/paminfo")
    public ResponseEntity<String> paminfo(@RequestParam Map<String,String> payload) {
        //System.out.println(payload);
        String userid = (String) payload.get("userid");
        String password = (String) payload.get("password");
        return authenticate(userid, password);
    }

    @NotNull
    private ResponseEntity<String> authenticate(String userid, String password) {
        try {
            Map<String,Object> userData = PamUser.getUserData(userid, password);
            ObjectMapper objectMapper = new ObjectMapper();
            String userDataString = objectMapper.writeValueAsString(userData);
            return ResponseEntity.ok(userDataString);
        } catch (Exception e) {
            String responseString = e.getMessage();
            System.out.println("Authentication Failure Response String: "+ responseString);
            log.debug("Response from PamAuthentication: "+responseString);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,String> response = new HashMap<String,String>();
            try {
                response.put("Error", responseString);
                return ResponseEntity.ok(objectMapper.writeValueAsString(response));
            } catch (Exception ex) {
                return ResponseEntity.ok("Error:"+ex.getMessage());
            }
        }
    }

}
