package org.jvnet.libpam;

public class TestUser {
    public static void main(String[] args) throws Exception {
        UnixUser u = new PAM("sshd").authenticate(args[0], args[1]);
        System.out.println(u.getUID());
        System.out.println(u.getGroups());
        System.out.println(u.getGecos());
        System.out.println(u.getDir());
        System.out.println(u.getShell());
    }
}
