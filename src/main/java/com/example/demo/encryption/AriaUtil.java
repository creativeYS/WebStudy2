package com.example.demo.encryption;

//import com.amazonaws.util.Base64;
import egovframework.rte.fdl.cryptography.EgovPasswordEncoder;
import egovframework.rte.fdl.cryptography.impl.EgovARIACryptoServiceImpl;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class AriaUtil {


    //암호화
    public static String encryptedAria(String value, String key){
        String encrypted = "";
        try {
            EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
            EgovARIACryptoServiceImpl egovARIACryptoServiceImpl = new EgovARIACryptoServiceImpl();

            String hasedPassword = egovPasswordEncoder.encryptPassword(key);
            egovPasswordEncoder.setHashedPassword(hasedPassword);
            egovPasswordEncoder.setAlgorithm("SHA-256");
            egovARIACryptoServiceImpl.setPasswordEncoder(egovPasswordEncoder);
            egovARIACryptoServiceImpl.setBlockSize(1025);

            byte[] byteencrypted = egovARIACryptoServiceImpl.encrypt(value.getBytes("UTF-8"), key);
            encrypted = Base64.encodeBase64String(byteencrypted);

        }catch(Exception e){
            return encrypted;
        }

        return encrypted;
    }

    //복호화
    public static String decryptedArea(String value, String key){
        String decrypted = "";
        try {
            EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
            EgovARIACryptoServiceImpl egovARIACryptoServiceImpl = new EgovARIACryptoServiceImpl();

            String hasedPassword = egovPasswordEncoder.encryptPassword(key);
            egovPasswordEncoder.setHashedPassword(hasedPassword);
            egovPasswordEncoder.setAlgorithm("SHA-256");
            egovARIACryptoServiceImpl.setPasswordEncoder(egovPasswordEncoder);
            egovARIACryptoServiceImpl.setBlockSize(1025);

            byte[] bytedecrypted = egovARIACryptoServiceImpl.decrypt(Base64.decodeBase64(value.getBytes()), key);
            decrypted = new String(bytedecrypted, "UTF-8");
        }catch(Exception e){
            return decrypted;
        }

        return decrypted;
    }
}
