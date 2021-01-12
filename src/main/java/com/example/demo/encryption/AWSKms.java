package com.example.demo.encryption;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.util.Base64;
import com.example.demo.Security.CustomLoginSuccessHandler;
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
