package com.provectus.formula.alexis.mvc;

import com.provectus.formula.alexis.models.entities.GroupEntity;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StatisticControllerTest extends MockControllerTest{

    @Test
    @WithMockUser("StatisticControllerTest001@example.com")
    public void test_inprogressStatistic() throws Exception {
        GroupEntity group = givenData("001");

        mockMvc.perform(get("/api/alexa/quiz/statistic/inprogress/"+group.getId()+"?pageNumber=1"))
        .andExpect(status().isOk())
                .andExpect(jsonPath("numberOfPages", is(1)))
                .andExpect(jsonPath("words", notNullValue()))
                .andExpect(jsonPath("words[0].ruWord", is("rus")))
                .andExpect(jsonPath("words[0].correct", is(0)))
                .andExpect(jsonPath("words[0].wrong", is(0)))
        .andDo(document("stat-progress",
                responseFields(
                        fieldWithPath("numberOfPages").description("Number Of Pages"),
                        fieldWithPath("words").description("Words List"),
                        fieldWithPath("words[].ruWord").description("Ru Word"),
                        fieldWithPath("words[].correct").description("Correct Result"),
                        fieldWithPath("words[].wrong").description("Wrong Result")
                )));
    }

    @Test
    @WithMockUser("StatisticControllerTest002@example.com")
    public void test_learnedStatistic() throws Exception {
        GroupEntity group = givenData("002");
        mockMvc.perform(get("/api/alexa/quiz/statistic/learned/"+group.getId()+"?pageNumber=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("numberOfPages", is(0)))
                .andExpect(jsonPath("words", notNullValue()))
                .andDo(document("stat-learned",responseFields(
                        fieldWithPath("numberOfPages").description("In Progress"),
                        fieldWithPath("words").description("Learned")
                )))
        ;


    }

    @Test
    @WithMockUser("StatisticControllerTest003@example.com")
    public void test_amountStatistic() throws Exception {
        GroupEntity group = givenData("003");
        mockMvc.perform(get("/api/alexa/quiz/statistic/amount/"+group.getId()+"?pageNumber=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("inprogress", is(1)))
                .andExpect(jsonPath("learned", is(0)))
        .andDo(document("stat-amount",responseFields(
                fieldWithPath("inprogress").description("In Progress"),
                fieldWithPath("learned").description("Learned")
                )))
        ;

    }

    private GroupEntity givenData(String counter) throws Exception {
        givenUser("StatisticControllerTest"+counter+"@example.com", counter, "AWS_StatisticControllerTest"+counter);
        givenGroup();
        GroupEntity testGroup = getGroup("testGroup", "StatisticControllerTest"+counter+"@example.com");
        givenWord(testGroup.getId());
        return testGroup;

    }
}
