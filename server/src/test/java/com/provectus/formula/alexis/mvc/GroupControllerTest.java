package com.provectus.formula.alexis.mvc;

import com.provectus.formula.alexis.models.entities.GroupEntity;
import com.provectus.formula.alexis.models.entities.UsersEntity;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GroupControllerTest extends MockControllerTest {

    @Test
    @WithMockUser("GroupControllerTest001@example.com")
    public void test_saveWordGroup() throws Exception {

        givenUser("GroupControllerTest001@example.com", "001", null);
        UsersEntity user = givenUser("GroupControllerTest001@example.com", "001", null);

        Map<String, Object> params = givenGroupStructure();

        this.mockMvc.perform( put("/home/wordgroups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(params))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("testGroup")) )
                .andExpect(jsonPath("activeState", is(true)) )
                .andExpect(jsonPath("userId", is(user.getId().intValue())) )
                .andDo(document("group-save",
                        requestFields(
                                fieldWithPath("name").description("Group name"),
                                fieldWithPath("activeState").description("State"),
                                fieldWithPath("words").description("List words")
//                                fieldWithPath("words[].enWordEntity").description("English Word"),
//                                fieldWithPath("words[].enWordEntity.enWord").description("Word"),
//                                fieldWithPath("words[].ruWordEntity").description("Russian Word"),
//                                fieldWithPath("words[].ruWordEntity.ruWord").description("Word"),
//                                fieldWithPath("words[].ruWordEntity.fileName").description("Word File")

                        ),
                        responseFields(
                                fieldWithPath("id").description("Group Id"),
                                fieldWithPath("name").description("Group name"),
                                fieldWithPath("activeState").description("State").type(Boolean.TYPE),
                                fieldWithPath("userId").description("User Id").type(Long.TYPE)

                        )));
    }

    @Test
    @WithMockUser("GroupControllerTest002@example.com")
    public void test_findByUser() throws Exception {
        givenUser("GroupControllerTest002@example.com", "002", null);

        givenGroup();

        this.mockMvc.perform( get("/home/wordgroups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", is("testGroup")))
                .andExpect(jsonPath("$[0].activeState", is(true)))
                .andExpect(jsonPath("$[0].userId", notNullValue()))
                .andDo(
                        document("group-list",
                                responseFields(
                                        fieldWithPath("[0].id").description("Group Id"),
                                        fieldWithPath("[0].name").description("Group name"),
                                        fieldWithPath("[0].activeState").description("State"),
                                        fieldWithPath("[0].userId").description("User Id")
                                ))
                );
    }

    @Test
    @WithMockUser("GroupControllerTest003@example.com")
    public void test_findByIdAndUser() throws Exception {
        givenUser("GroupControllerTest003@example.com", "003", null);
        givenGroup();
        GroupEntity testGroup = getGroup("testGroup", "GroupControllerTest003@example.com");
        this.mockMvc.perform( get("/home/wordgroups/"+testGroup.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", notNullValue()))
                .andExpect(jsonPath("name", is("testGroup")))
                .andExpect(jsonPath("activeState", is(true)))
                .andExpect(jsonPath("userId", notNullValue()))
                .andDo(
                        document("group-find-by-id",
                                responseFields(
                                        fieldWithPath("id").description("Group Id"),
                                        fieldWithPath("name").description("Group name"),
                                        fieldWithPath("activeState").description("State"),
                                        fieldWithPath("userId").description("User Id")
                                ))
                );
    }

    @Test
    @WithMockUser("GroupControllerTest004@example.com")
    public void test_delete() throws Exception {
        givenUser("GroupControllerTest004@example.com", "004", null);
        givenGroup();
        GroupEntity testGroup = getGroup("testGroup", "GroupControllerTest004@example.com");
        this.mockMvc.perform( delete("/home/wordgroups/"+testGroup.getId()))
                .andExpect(status().isOk())
                .andDo(
                        document("group-delete"));
    }

    @Test
    @WithMockUser("GroupControllerTest005@example.com")
    public void test_update() throws Exception {
        UsersEntity user = givenUser("GroupControllerTest005@example.com", "004", null);
        givenGroup();
        GroupEntity testGroup = getGroup("testGroup", "GroupControllerTest005@example.com");
        Map<String,Object> params = givenGroupStructure();
        params.put("id",testGroup.getId());
        params.put("activeState", false);


        this.mockMvc.perform( post("/home/wordgroups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(params))
        )
                .andExpect(status().isOk())
                .andDo(document("group-update",
                        requestFields(
                                fieldWithPath("id").description("Group Id"),
                                fieldWithPath("name").description("Group name"),
                                fieldWithPath("activeState").description("State"),
                                fieldWithPath("words").description("List words")
//                                fieldWithPath("words[].enWordEntity").description("English Word"),
//                                fieldWithPath("words[].enWordEntity.enWord").description("Word"),
//                                fieldWithPath("words[].ruWordEntity").description("Russian Word"),
//                                fieldWithPath("words[].ruWordEntity.ruWord").description("Word"),
//                                fieldWithPath("words[].ruWordEntity.fileName").description("Word File")

                        )));
    }

}
