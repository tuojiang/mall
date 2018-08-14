package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @program: mmall
 * @Date: 2018/8/14
 * @Author: chandler
 * @Description:
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
//        objectMapper.configure()
    }
}
