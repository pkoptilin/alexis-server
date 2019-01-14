package com.provectus.formula.alexis.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.provectus.formula.alexis.Application;
import com.provectus.formula.alexis.models.WordReturn;
import com.provectus.formula.alexis.models.entities.GroupEntity;
import com.provectus.formula.alexis.models.entities.UsersEntity;
import com.provectus.formula.alexis.repository.GroupRepository;
import com.provectus.formula.alexis.repository.UserRepository;
import com.provectus.formula.alexis.services.TextToSpeechService;
import com.provectus.formula.alexis.services.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("rest-test")
@Transactional
public abstract class MockControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;

    @Autowired
    TextToSpeechService textToSpeechService;

    @Before
    public void setUp() {
        this.mockMvc = this.build();
    }

    protected RestDocumentationResultHandler document(String identifer, Snippet... snippets){
        return org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document(identifer,
                preprocessRequest(prettyPrint(), removeHeaders("Host","Content-Length")),
                preprocessResponse(prettyPrint(), removeHeaders( "Content-Length" ) ),
                snippets
        );
    }
    protected MockMvc build() {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(springSecurity())
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        Mockito.when(textToSpeechService.convertToSpeech(Mockito.anyString())).thenReturn("test.mp3");
        return mockMvc;
    }

    protected UsersEntity givenUser(String email, String name, String awsID){
        UsersEntity mockUser = new UsersEntity(email, "123456", name, awsID);
        userService.registerUser(mockUser);
        if (awsID!=null) {
            userService.linkAlexaId(mockUser.getEmail(), mockUser.getAwsid());
        }
        return userRepository.findByEmail(email);
    }

    protected GroupEntity getGroup(String groupName, String email){
        GroupEntity group = groupRepository.findWordGroupByNameAndUserId(groupName, getUser(email).getId());
        return cloneObj(group);
    }
    protected UsersEntity getUser(String email){
        return userRepository.findByEmail(email);
    }

    protected Map<String,Object> givenGroupStructure(){
        Map<String,Object> params = new HashMap<>();

        List<Map<String,Object>> paramWords = new ArrayList<>();
//        Map<String,Object> word = new HashMap<>();
//        paramWords.add(word);
//        Map<String,Object> enWordEntity = new HashMap<>();
//        Map<String,Object> ruWordEntity = new HashMap<>();
        params.put("name","testGroup");
        params.put("activeState", true);
        params.put("words", paramWords);

//        word.put("enWordEntity", enWordEntity);
//        word.put("ruWordEntity", ruWordEntity);

//        enWordEntity.put("enWord", "english");
//        ruWordEntity.put("ruWord", "russian");
//        ruWordEntity.put("fileName", "russian.wav");

        return params;
    }
    protected void givenGroup() throws Exception {
        Map<String, Object> groupStructure = givenGroupStructure();
        this.mockMvc.perform( put("/home/wordgroups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(groupStructure))
        )
                .andExpect(status().isOk());
    }

    protected WordReturn givenWord(Long groupId) throws Exception {
        Map<String, Object> word = new HashMap<>();
        word.put("enWord","eng");
        word.put("ruWord","rus");
        ResultActions resultActions = this.mockMvc.perform(put("/home/wordgroups/" + groupId + "/words")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(word))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", notNullValue()));
        WordReturn wordReturn = objectMapper.readValue(
                resultActions.andReturn().getResponse().getContentAsByteArray(),
                WordReturn.class);
        return wordReturn;
    }

    protected <T> T cloneObj(T obj){
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(obj);
            T result = (T) objectMapper.readValue(bytes, obj.getClass());
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
