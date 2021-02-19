package com.toptal.junit.using.spring;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class AreaControllerIntegrationTest {

    /*
    @ContextConfiguration(classes = {TestConfig.class})—this tells the test case where all the bean definitions reside.

    Now instead of @InjectMocks we use:
    @Autowired
    AreaController areaController;
     */
    @Autowired
    AreaController areaController;

    MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = standaloneSetup(areaController).build();
    }

    @Test
    public void calculateAreaTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/area?type=RECTANGLE&param1=6&param2=5")).andExpect(status().isOk())
                .andExpect(content().string("30.0"));
    }
}
