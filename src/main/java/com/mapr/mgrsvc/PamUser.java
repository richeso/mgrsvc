package com.mapr.mgrsvc;

import org.jvnet.libpam.PAM;
import org.jvnet.libpam.UnixUser;

import java.util.HashMap;
import java.util.Map;

public class PamUser {
    @org.jetbrains.annotations.NotNull
    public static String getUserDescription(String username, String password) throws Exception {
        UnixUser u = new PAM("sshd").authenticate(username, password);
        System.out.println(u.getUID());
        System.out.println(u.getGroups());
        System.out.println(u.getUserName());
        System.out.println(u.getGecos());
        System.out.println(u.getDir());
        System.out.println(u.getShell());
        String description = "UID="+u.getUID() + " - GID=" + u.getGID() + " - UserName=" + u.getUserName();
        return description;
    }
    public static Map<String, Object> getUserData(String username, String password) throws Exception {
        UnixUser u = new PAM("sshd").authenticate(username, password);
        Map<String, Object> userData = new HashMap<String,Object>();
        userData.put("uid", u.getUID());
        userData.put("gid", u.getGID());
        userData.put("username", u.getUID());
        userData.put("groups", u.getGroups());
        userData.put("gecos", u.getGecos());
        userData.put("dir",u.getDir());
        userData.put("shell",u.getShell());
        return userData;
    }
}
