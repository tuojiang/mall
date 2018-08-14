package com.mmall.util;

import com.google.common.collect.Lists;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

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
        /**对象的所有字段全部列入*/
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        /**取消默认转换timestamps形式*/
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        /**忽略空Bean转json的错误*/
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        /**所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss*/
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
        /**忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误*/
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
    }

    /**
     * 将对象序列化为String的方法
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error", e);
            return null;
        }
    }

    /**
     * 返回默认的格式化序列化对象
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            log.warn("Parse Object to String error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, TypeReference typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (IOException e) {
            log.warn("Parse Object to String error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClass) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClass);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            log.warn("Parse Object to String error", e);
            return null;
        }
    }

    public static void main(String[] args) {
        User user = new User();
        user.setId(2);
        user.setEmail("bobo@mmall.com");
        User u2 = new User();
        u2.setId(2);
        u2.setEmail("geelyu2@happymmall.com");

        String json = JsonUtil.obj2String(user);
        String json2 = JsonUtil.obj2StringPretty(user);
//        String json = "{\"id\":666,\"id\":\"boboan\"}";
        User user1 = JsonUtil.string2Obj(json,User.class);
        log.info("userJson:{}",json2);

        List<User> userList = Lists.newArrayList();
        userList.add(user);
        userList.add(user1);
        String json3 =JsonUtil.obj2StringPretty(userList);
        log.info(json3);
    }
}
