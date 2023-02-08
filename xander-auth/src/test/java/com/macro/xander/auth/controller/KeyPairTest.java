package com.macro.xander.auth.controller;

import com.alibaba.nacos.common.codec.Base64;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;


class KeyPairTest {

    // 私钥
    private static String privateKey;

    private static String content;

    @BeforeEach
    void setUp() {
//        privateKey = "sdu1232s9@#!@#41asdwqdascfgfiah./*f";
        privateKey = "123456";
        content = "Thisismysecret.";
    }

    @Test
    public void sign() throws Exception {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), privateKey.toCharArray());
//        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new FileUrlResource("D:\\Project\\Java\\xander-learn\\xander-auth\\src\\main\\resources\\jwt.jks"), privateKey.toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("jwt", privateKey.toCharArray());

        // 用公钥加密
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        byte[] bytes = Base64.encodeBase64((publicKey.getEncoded()));
        String s = new String(bytes);
        System.out.println("公钥:" + s);

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        byte[] priBytes = Base64.encodeBase64((privateKey.getEncoded()));
        String pri = new String(priBytes);
        System.out.println("私钥:" + pri);


        RSAKey key = new RSAKey.Builder(publicKey).build();
        Map<String, Object> stringObjectMap = new JWKSet(key).toJSONObject();
        List<Map<String,Object>> keys = (List<Map<String, Object>>) stringObjectMap.get("keys");

        // 前端
        RSAKey parse = RSAKey.parse(keys.get(0));
        PublicKey publicKey1 = parse.toPublicKey();
        System.out.println("公钥2:" + new String(Base64.encodeBase64(publicKey1.getEncoded())));
    }
}