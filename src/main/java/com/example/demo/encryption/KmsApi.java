package com.example.demo.encryption;

public interface KmsApi {
    String encrypt(String str);
    String decrypt(String str);
}
