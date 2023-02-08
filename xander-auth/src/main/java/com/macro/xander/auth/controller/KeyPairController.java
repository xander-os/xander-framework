package com.macro.xander.auth.controller;


import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/7 11:18
 * @email ：zhrunxin33@gmail.com
 * @description：获取RSA公钥
 */
@RestController
@Api(tags = "KeyPairController", value = "获取RSA公钥")
@RequestMapping("/rsa")
public class KeyPairController {

    @Autowired
    private KeyPair keyPair;

    @GetMapping("/publicKey")
    public Map<String, Object> getKey(){
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }
}
