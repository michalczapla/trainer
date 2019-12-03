package com.czaplon.trainer;

import com.czaplon.trainer.controller.WorkoutController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkoutController.class)
@WebAppConfiguration
public class WorkoutControllerTests {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void shouldUpdateWorkoutData() throws Exception{
//        ResultActions result = this.mockMvc.perform(get("/delete/xyz"));
//
//        result
//                .andExpect(status().isOk())
//                .andExpect(redirectedUrl("/"));
//
//    }

}
