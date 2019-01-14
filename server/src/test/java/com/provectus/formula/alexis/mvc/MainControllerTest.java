package com.provectus.formula.alexis.mvc;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MainControllerTest extends MockControllerTest{


    @Test
    public void testUserRegistration() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("email", "test@test.com");
        params.put("password", "test");
        params.put("name", "Test");

        this.mockMvc.perform(
                    post("/user_registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(this.objectMapper.writeValueAsString(params))
                    )
                .andExpect(status().isOk())

                .andDo(document("user-registration",
                        requestFields(
                                fieldWithPath("email").description("The user email to login"),
                                fieldWithPath("password").description("User password"),
                                fieldWithPath("name").description("User name")
                        )));

    }

    @Test
    @WithMockUser(username = "test2@test.com", roles={"ADMIN"})
    public void testUserDetails() throws Exception {
        givenUser("test2@test.com", "Test", null);
        this.mockMvc.perform(
                get("/home")
        )
                .andExpect(status().isOk())
                .andExpect( jsonPath("email", is("test2@test.com")) )
                .andExpect( jsonPath("name", is("Test")) )
                .andExpect( jsonPath("awsExist", is(false)) )
                .andDo(document("user-details",
                        responseFields(
                                fieldWithPath("email").description("The user email to login"),
                                fieldWithPath("name").description("User name"),
                                fieldWithPath("awsExist").description("Is Alexa Linked")
                        )));

    }

}
