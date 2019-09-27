package cn.qp.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author BaoZi
 * @date 2019/9/27 11:10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void whenQuerySuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user")
                .param("username","jojo")
                .param("age","18")
                .param("ageTo","60")
                .param("size", "15")
                .param("page", "3")
                .param("sort", "age,desc")
                .contentType(MediaType.APPLICATION_JSON_UTF8)) //设置请求头的contentType
                .andExpect(MockMvcResultMatchers.status().isOk()) //期望返回成功信息
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3));//期望返回的集合数量为3
    }
}
