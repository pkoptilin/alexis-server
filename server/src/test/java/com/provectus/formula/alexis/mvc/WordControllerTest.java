package com.provectus.formula.alexis.mvc;

import com.provectus.formula.alexis.models.WordReturn;
import com.provectus.formula.alexis.models.entities.GroupEntity;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WordControllerTest extends MockControllerTest{

    @Test
    @WithMockUser("WordControllerTest001@example.com")
    public void test_saveWordToWordGroup() throws Exception {
        givenUser("WordControllerTest001@example.com", "001", "AWS_WordControllerTest001");
        givenGroup();
        Map<String, Object> word = new HashMap<>();
        word.put("enWord","eng");
        word.put("ruWord","rus");

        GroupEntity testGroup = getGroup("testGroup", "WordControllerTest001@example.com");
        this.mockMvc.perform( put("/home/wordgroups/"+ testGroup.getId()+"/words")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(word))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("enWord", is("eng")))
                .andExpect(jsonPath("ruWord", is("rus")))
                .andExpect(jsonPath("fileName", endsWith(".mp3")))
                .andDo(
                        document("word-save",
                        requestFields(
                                fieldWithPath("enWord").description("English Word"),
                                fieldWithPath("ruWord").description("Russian Word")
                        ),
                        responseFields(
                                fieldWithPath("id").description("ID"),
                                fieldWithPath("enWord").description("English Word"),
                                fieldWithPath("ruWord").description("Russian Word"),
                                fieldWithPath("fileName").description("Audio File Name")

                        )
                ));
    }
    @Test
    @WithMockUser("WordControllerTest002@example.com")
    public void test_getWordsByWordGroup() throws Exception {
        givenUser("WordControllerTest002@example.com", "002", "AWS_WordControllerTest002");
        givenGroup();
        GroupEntity testGroup = getGroup("testGroup", "WordControllerTest002@example.com");
        givenWord(testGroup.getId());

        this.mockMvc.perform( get("/home/wordgroups/"+ testGroup.getId()+"/words"))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].enWord", is("eng")))
                .andExpect(jsonPath("$[0].ruWord", is("rus")))
                .andExpect(jsonPath("$[0].fileName", endsWith(".mp3")))
            .andDo(
                    document("word-get-list",
                            responseFields(
                                    fieldWithPath("[].id").description("ID"),
                                    fieldWithPath("[].enWord").description("English Word"),
                                    fieldWithPath("[].ruWord").description("Russian Word"),
                                    fieldWithPath("[].fileName").description("Audio File Name")
                    ))
            )
        ;
    }

    @Test
    @WithMockUser("WordControllerTest003@example.com")
    public void test_deleteWordFromWordGroup() throws Exception {
        givenUser("WordControllerTest003@example.com", "003", "AWS_WordControllerTest003");
        givenGroup();
        GroupEntity testGroup = getGroup("testGroup", "WordControllerTest003@example.com");
        WordReturn wordReturn = givenWord(testGroup.getId());
        this.mockMvc.perform( delete("/home/wordgroups/"+testGroup.getId()+"/words/" + wordReturn.getId()))
                .andExpect(status().isOk())
                .andDo(
                        document("word-delete")
                );

    }

    @Test
    @WithMockUser("WordControllerTest004@example.com")
    public void test_getRelevantWordsLike() throws Exception {
        givenUser("WordControllerTest004@example.com", "004", "AWS_WordControllerTest004");
        givenGroup();
        GroupEntity testGroup = getGroup("testGroup", "WordControllerTest004@example.com");
        givenWord(testGroup.getId());

        this.mockMvc.perform( get("/api/words/suggestion/en/en"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", is("eng")))
                .andDo(
                        document("word-relevant",
                                responseFields(
                                        fieldWithPath("[]").description("List of relevant words")
                                ))
                );

    }
}
