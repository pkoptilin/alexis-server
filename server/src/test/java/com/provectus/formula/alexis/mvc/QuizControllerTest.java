package com.provectus.formula.alexis.mvc;

import com.provectus.formula.alexis.models.WordReturn;
import com.provectus.formula.alexis.models.entities.GroupEntity;
import com.provectus.formula.alexis.models.entities.UsersEntity;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QuizControllerTest extends MockControllerTest{

    @Test
    @WithMockUser("QuizControllerTest001@example.com")
    public void test_updateStatistic() throws Exception {
        UsersEntity user = givenUser("QuizControllerTest001@example.com", "001", "AWS_QuizControllerTest001");
        givenGroup();
        GroupEntity testGroup = getGroup("testGroup", "QuizControllerTest001@example.com");
        WordReturn wordReturn = givenWord(testGroup.getId());

        Map<String,Object> answerParent = new HashMap<>();
        List<Map<String,Object>> answers = new ArrayList<>();
        Map<String,Object> answer = new HashMap<>();
        answers.add(answer);

        answer.put("rusWordId",wordReturn.getId());
        answer.put("answerWord",wordReturn.getEnWord());
        answer.put("answer",true);

        answerParent.put("alexaId","AWS_QuizControllerTest001");
        answerParent.put("answers", answers);


        this.mockMvc.perform( post("/api/alexa/quiz/groups/"+testGroup.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(answerParent))
        )
                .andExpect(status().isOk())
                .andDo(
                        document("quiz-update",
                                requestFields(
                                        fieldWithPath("alexaId").description("Alexa User Id"),
                                        fieldWithPath("answers").description("Answers List"),
                                        fieldWithPath("answers[].rusWordId").description("Rus Word Id"),
                                        fieldWithPath("answers[].answerWord").description("English Answer"),
                                        fieldWithPath("answers[].answer").description("Answer Status")
                                ),
                                responseFields(
                                        fieldWithPath("[].wordId").description("Word Id"),
                                        fieldWithPath("[].word").description("Russian word"),
                                        fieldWithPath("[].audioFileName").description("Word Audio"),
                                        fieldWithPath("[].answers").description("List of answers")

                                )
                        )
                );

    }
}
