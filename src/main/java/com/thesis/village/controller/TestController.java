package com.thesis.village.controller;

import com.thesis.village.model.Test;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.ResolverUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author yh
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class TestController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/users")
    public ResponseEntity<Test> test() {
        // 创建一个Test对象，并设置name和password为"123"
        Test test = new Test();
        test.setName("123");
        test.setPassword("123");
        System.out.println("i am test");
        // 将Test对象添加到列表中，因为前端如果期望接收列表形式的数据，这里模拟返回一个包含设置好值的对象的列表
        List<Test> testList = new ArrayList<>();
//        testList.add(test);
//        log.info("test: {}", test);
        return new ResponseEntity<>(test, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<Object> getTestData() {
        // 创建一个简单的测试数据对象
        // 这里你可以返回任何数据，这里以返回一个简单的Map为例
        // 你可以根据实际需求进行修改
        Object testData = new Object() {
            public String message = "Hello, this is a test API!";
            public int status = 200;
        };

        // 返回数据以及HTTP状态码200（成功）
        return new ResponseEntity<>(testData, HttpStatus.OK);
    }
}