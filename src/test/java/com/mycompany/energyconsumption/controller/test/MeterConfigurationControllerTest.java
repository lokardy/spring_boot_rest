package com.mycompany.energyconsumption.controller.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.mycompany.energyconsumption.controller.MeterController;

//@RunWith(SpringRunner.class)
//@WebMvcTest(MeterController.class)
//@AutoConfigureRestDocs(outputDir = "target/snippets")
public class MeterConfigurationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test() {
    	
    }
}
