package com.provectus.formula.alexis.mvc;

import com.provectus.formula.alexis.Application;
import com.provectus.formula.alexis.models.entities.UsersEntity;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ConfigurationControllerTest extends MockControllerTest{

    @Before
    public void setUp() {
        this.mockMvc = build();
    }

    @Test
    @WithMockUser("ConfigurationControllerTest001@example.com")
    public void testGetUserSettings() throws Exception {
        givenUser("ConfigurationControllerTest001@example.com", "001", null);
        this.mockMvc.perform(get("/api/alexa/configuration"))
                .andExpect(status().isOk())
                .andExpect( jsonPath("failApproach", is(1)) )
                .andExpect( jsonPath("successApproach", is(3)) )
                .andExpect( jsonPath("defaultGroupId", nullValue()) )
                .andDo(
                        document("user-configuration",
                                responseFields(
                                        fieldWithPath("failApproach").description("Fail Approach"),
                                        fieldWithPath("successApproach").description("Success Approach"),
                                        fieldWithPath("defaultGroupId").description("Default Group Id")
                                )
                        ));

    }

    @Test
    @WithMockUser("ConfigurationControllerTest002@example.com")
    public void testUpdateUserSettings() throws Exception {
        givenUser("ConfigurationControllerTest002@example.com", "002", null);

        Map<String, Object> params = new HashMap<>();
        params.put("failApproach", 3);
        params.put("successApproach", 4);
        params.put("defaultGroupId", 1);

        this.mockMvc.perform(
                post("/api/alexa/configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(params))
        )
                .andExpect(status().isOk())

                .andDo(document("user-update-config",
                        requestFields(
                                fieldWithPath("failApproach").description("Fail Approach"),
                                fieldWithPath("successApproach").description("Success Approach"),
                                fieldWithPath("defaultGroupId").description("Default Group Id")
                        )));
    }

    @Test
    @WithMockUser("ConfigurationControllerTest003@example.com")
    public void testFindByUserAndState() throws Exception {
        givenUser("ConfigurationControllerTest003@example.com", "003", null);

        this.mockMvc.perform(get("/api/alexa/configuration/groups"))
                .andExpect(status().isOk())
//                .andExpect( jsonPath("$[0]", nullValue()) )
                .andDo(
                        document("user-groups"
//                                , responseFields(
//                                        fieldWithPath("failApproach").description("Fail Approach"),
//                                        fieldWithPath("successApproach").description("Success Approach"),
//                                        fieldWithPath("defaultGroupId").description("Default Group Id")
//                                )
                        ));
    }

    @Test
    @WithMockUser("ConfigurationControllerTest004@example.com")
    public void testGetUserSettingsForAlexa() throws Exception {
        UsersEntity mockUser = givenUser("ConfigurationControllerTest004@example.com", "004", "ConfigurationControllerTest004_AWS");

        this.mockMvc.perform(get("/api/skill/configuration")
                    .param("awsId", mockUser.getAwsid()) )
                .andExpect(status().isOk())
                .andExpect( jsonPath("failApproach", is(1)) )
                .andExpect( jsonPath("successApproach", is(3)) )
                .andExpect( jsonPath("defaultGroupId", nullValue()) )
                .andDo(
                        document("user-alexa-settings"
                                , responseFields(
                                        fieldWithPath("failApproach").description("Fail Approach"),
                                        fieldWithPath("successApproach").description("Success Approach"),
                                        fieldWithPath("defaultGroupId").description("Default Group Id")
                                )
                        ));
    }

}
