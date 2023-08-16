package com.winterfoodies.winterfoodies_project.config;


import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JasyptConfigTest .class)
class JasyptConfigTest { // public제거

    @Value("${jasypt.encryptor.password}")
    private String encryptKey;

    @Test
    public void encryptDecryptTest() {
        String text = "HAHA"; // 암호화 대상 평문


        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword(encryptKey); // Jasypt를 이용하여 암호화하는 키 (프로퍼티의 jasypt.encryptor.password)
        jasypt.setAlgorithm("PBEWithMD5AndDES"); // Jasypt를 이용한 암호화 알고리즘

        String encryptedText = jasypt.encrypt(text);
        System.out.println("enc : " + encryptedText);
        String decryptedText = jasypt.decrypt(encryptedText);
        System.out.println("dec : " + decryptedText);
    }
}