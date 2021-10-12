package com.example.springbootexample;

import org.jvnet.libpam.PAM;
import org.jvnet.libpam.UnixUser;

public class TestUser {
    @org.jetbrains.annotations.NotNull
    public static String getUserInfo(String username, String password) throws Exception {
        UnixUser u = new PAM("sshd").authenticate(username, password);
        System.out.println(u.getUID());
        System.out.println(u.getGroups());
        System.out.println(u.getUserName());
        System.out.println(u.getGecos());
        System.out.println(u.getDir());
        System.out.println(u.getShell());
        String description = u.getUID() + " - " + u.getUserName();
        return description;
    }
}
