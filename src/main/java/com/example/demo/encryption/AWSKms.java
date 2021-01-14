package com.example.demo.encryption;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.*;
import com.amazonaws.util.Base64;
import com.example.demo.Security.CustomLoginSuccessHandler;
import egovframework.rte.fdl.cryptography.EgovPasswordEncoder;
import egovframework.rte.fdl.cryptography.impl.EgovARIACryptoServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Component
public class AWSKms implements KmsApi {
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.kms.arn}")
    private String arn;

    @Value("${cloud.aws.kms.region}")
    private String region;

    private AWSKMS kms = null;

    @Override
    public String encrypt(String str) {

        if(kms == null)
        {
            BasicAWSCredentials credential = new BasicAWSCredentials(accessKey, secretKey);
            AWSCredentialsProvider provider = new AWSStaticCredentialsProvider(credential);

            kms = (AWSKMS)((AWSKMSClientBuilder)AWSKMSClientBuilder
                    .standard()
                    .withRegion(region))
                    .withCredentials(provider)
                    .build();
        }

        //String desc = "Key for protecting critical data";
        //CreateKeyRequest req = new CreateKeyRequest().withDescription(desc);
        //CreateKeyResult result = kms.createKey(req);
        //String strArn = result.getKeyMetadata().getArn();

        String keyId = arn;//"arn:aws:kms:us-west-2:111122223333:key/1234abcd-12ab-34cd-56ef-1234567890ab";
        GenerateDataKeyRequest dataKeyRequest = new GenerateDataKeyRequest();
        dataKeyRequest.setKeyId(keyId);
        dataKeyRequest.setKeySpec("AES_256");
        GenerateDataKeyResult dataKeyResult = kms.generateDataKey(dataKeyRequest);
        ByteBuffer plaintextKey = dataKeyResult.getPlaintext();
        ByteBuffer encryptedKey = dataKeyResult.getCiphertextBlob();
        //String str11 = new String(plaintextKey.array());
        //String str22 = new String(encryptedKey.array());

        EncryptRequest req1 = new EncryptRequest().withKeyId(keyId).withPlaintext(plaintextKey);
        ByteBuffer ciphertext11 = kms.encrypt(req1).getCiphertextBlob();

        DecryptRequest req2 = new DecryptRequest().withCiphertextBlob(encryptedKey).withKeyId(keyId);
        ByteBuffer plainText11 = kms.decrypt(req2).getPlaintext();

        DecryptRequest req3 = new DecryptRequest().withCiphertextBlob(ciphertext11).withKeyId(keyId);
        ByteBuffer plainText22 = kms.decrypt(req3).getPlaintext();

        String testStr = "Hello world!!!ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()1234567890";
        String test1111 = AriaUtil.encryptedAria(testStr, new String(plainText11.array()));
        String test2222 = AriaUtil.encryptedAria(testStr, new String(plainText22.array()));
        // FeH2DUy7wpRshtyQ/DfzvA==
        String test3333 = AriaUtil.decryptedArea(test1111, new String(ciphertext11.array()));
        String test3334 = AriaUtil.decryptedArea(test1111, new String(encryptedKey.array()));
        String test4444 = AriaUtil.decryptedArea(test1111, new String(plainText11.array()));
        String test5555 = AriaUtil.decryptedArea(test1111, new String(plainText22.array()));



        byte[] plaintextBytes = str.getBytes(StandardCharsets.UTF_8);
        EncryptRequest encReq = new EncryptRequest();
        encReq.setKeyId(arn);
        encReq.setPlaintext(ByteBuffer.wrap(plaintextBytes));
        ByteBuffer cipherText = kms.encrypt(encReq).getCiphertextBlob();
        byte[] base64EncodedValue = Base64.encode(cipherText.array());
        return new String(base64EncodedValue, StandardCharsets.UTF_8);
    }

    @Override
    public String decrypt(String str) {
        ByteBuffer encryptedText = ByteBuffer.wrap(Base64.decode(str));
        DecryptRequest req = (new DecryptRequest()).withCiphertextBlob(encryptedText);
        ByteBuffer plainText = kms.decrypt(req).getPlaintext();
        return new String(plainText.array());
        //return new String(Base64.encode(plainText.array()), StandardCharsets.UTF_8);
    }
}
