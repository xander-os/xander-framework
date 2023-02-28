package com.xander.seckill;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macro.xander.common.api.CommonResult;
import com.macro.xander.mapper.UmsMemberMapper;
import com.macro.xander.model.UmsMember;
import com.macro.xander.model.UmsMemberExample;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/28 16:22
 * @email ：zhrunxin33@gmail.com
 * @description：创建用于压测的数据(Token会过期)
 */
@SpringBootTest(classes = Example01Application.class)
public class CreateTokenTxt {
    // 获取token地址
    private static final String oAuthUrl = "http://localhost:8201/xander-auth/oauth/token";
    // 文件地址
    private static final String fileUrl = "./token.txt";
    // 最大限购数量
    private static final Integer quantity = 2;

    private static Map<String,Object> params = new HashMap<>();
    @Autowired
    private UmsMemberMapper umsMemberMapper;

    static {
        params.put("client_id","portal-app");
        params.put("client_secret","123456");
        params.put("grant_type","password");
        params.put("password","123456");
    }

    @Test
    public void generate() {
        //登录，生成token
        File file = new File(fileUrl);
        if (file.exists()) {
            file.delete();
        }
        // 查询压测的会员信息
        UmsMemberExample umsMemberExample = new UmsMemberExample();
        umsMemberExample.createCriteria().andIdGreaterThan(10l);
        List<UmsMember> umsMembers = umsMemberMapper.selectByExample(umsMemberExample);
        Assertions.assertTrue(CollectionUtil.isNotEmpty(umsMembers));
        umsMembers = CollectionUtil.sub(umsMembers,0,10);
        List<Member> members = new ArrayList<>();
        try {
            for (UmsMember umsMember : umsMembers) {
                params.put("username",umsMember.getUsername());
                String post = HttpUtil.post(oAuthUrl, params);
                ObjectMapper objectMapper = new ObjectMapper();
                CommonResult<HashMap<String,String>> result = objectMapper.readValue(post, CommonResult.class);
                String token = result.getData().get("token");
                Member member = new Member();
                member.setCount(quantity.toString());
                member.setToken(token);
                members.add(member);
            }
            // 生成每一行如下
            // goodsId,promotionId,memberReceiveAddressId,ProductSkuId,count,token
            File result = FileUtil.appendUtf8Lines(members, file);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        params.put("username","zhengsan");
        String post = HttpUtil.post(oAuthUrl, params);
        ObjectMapper objectMapper = new ObjectMapper();
        CommonResult<HashMap<String,String>> result = objectMapper.readValue(post, CommonResult.class);
        System.out.println(result.getData().get("token"));
    }

    public static class Member{
        private String goodsId;
        private String promotionId;
        private String memberReceiveAddressId;
        private String ProductSkuId;
        private String count;
        private String token;

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getPromotionId() {
            return promotionId;
        }

        public void setPromotionId(String promotionId) {
            this.promotionId = promotionId;
        }

        public String getMemberReceiveAddressId() {
            return memberReceiveAddressId;
        }

        public void setMemberReceiveAddressId(String memberReceiveAddressId) {
            this.memberReceiveAddressId = memberReceiveAddressId;
        }

        public String getProductSkuId() {
            return ProductSkuId;
        }

        public void setProductSkuId(String productSkuId) {
            ProductSkuId = productSkuId;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return goodsId +
                    "," + promotionId +
                    "," + memberReceiveAddressId +
                    "," + ProductSkuId +
                    "," + count +
                    "," + token;
        }
    }
}
