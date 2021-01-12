package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static junit.framework.TestCase.assertEquals;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testInt() {

        int a=3;
        //assertThat(a, 4);
        assertEquals(a, 3);
    }

}
