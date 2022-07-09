package com.platform.testing.common;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @program: TokenDb
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-19 19:30
 **/
@Component
public class TokenDb {
    private Map<String, TokenDto> tokenMap = new HashMap<>();

    public Integer getOnlineUserSize() {
        return tokenMap.size();
    }

    public TokenDto getUserInfo(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return tokenMap.get(token);
    }

    public void addUserInfo(String token, TokenDto tokenDTo) {
        if (tokenDTo == null) {
            return;
        }
        tokenMap.put(token, tokenDTo);
    }

    public TokenDto removeUserInfo(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return tokenMap.remove(token);
    }

    public boolean isLogin(String token) {
        return tokenMap.get(token) != null;
    }

    //移除token
    public TokenDto removeTokenDto(String token){

        if(Objects.isNull(token)){
            return null;
        }

        return tokenMap.remove(token);
    }



}
