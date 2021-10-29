package com.mapr.mgrsvc;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class DownloadController {

    private static final Logger log = LoggerFactory.getLogger(DownloadController.class);

    @GetMapping("/api/export")
        public ResponseEntity<String> paminfo(@RequestParam Map<String,String> payload,HttpServletResponse response) {
            //System.out.println(payload);
            String userid = (String) payload.get("userid");
            String password = (String) payload.get("password");
            FileSystemResource resource = new FileSystemResource("/tmp/tmpfile.csv");
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=file.zip");
            try (ZipOutputStream zippedOut = new ZipOutputStream(response.getOutputStream())) {
                // validate user first to ensure user is allowed
                Map userData = PamUser.getUserData(userid,password);
                ZipEntry e = new ZipEntry(resource.getFilename());
                // Configure the zip entry, the properties of the file
                e.setSize(resource.contentLength());
                e.setTime(System.currentTimeMillis());
                // etc.
                zippedOut.putNextEntry(e);
                // And the content of the resource:
                StreamUtils.copy(resource.getInputStream(), zippedOut);
                zippedOut.closeEntry();
                zippedOut.finish();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         return ResponseEntity.ok("Download Request Completed");
    }
}
