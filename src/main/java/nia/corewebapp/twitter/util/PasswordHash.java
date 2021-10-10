package nia.corewebapp.twitter.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHash {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("admin"));
        System.out.println(new BCryptPasswordEncoder().encode("user1"));
        System.out.println(new BCryptPasswordEncoder().encode("user2"));
    }
}
