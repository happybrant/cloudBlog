package com.kongfu.frontend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FrontendApplicationTests {

    @Test
    void contextLoads() {
        String test = "/心得体会/C#精通";
        System.out.println(test.split("/")[3]);
    }
}
