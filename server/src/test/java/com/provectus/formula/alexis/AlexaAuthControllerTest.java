package com.provectus.formula.alexis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.provectus.formula.alexis.controlers.AlexaController;
import com.provectus.formula.alexis.models.entities.UsersEntity;
import com.provectus.formula.alexis.repository.UserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AlexaAuthControllerTest {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlexaController controller;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    @WithMockUser
    public void testSuccessUserOTP() throws Exception {
        this.mockMvc.perform(get("/alexa/userOtp"))
                .andExpect(status().isOk())
                .andDo(document("alexa-get-code-success"));
    }

    @Test
    @WithAnonymousUser
    public void testFailedUserOTP() throws Exception {
        this.mockMvc.perform(get("/alexa/userOtp"))
                .andExpect(status().isForbidden())
                .andDo(document("alexa-get-code-unauthorized"));

    }

    @Test
    @WithMockUser(username = "test@mail.com", roles={"ADMIN"})
    public void testLinkUserSuccess() throws Exception {

        UsersEntity user = new UsersEntity("test@mail.com", "111111", "test");
        userRepository.save(user);

        String mockOtp = controller.getOneTimePassword().getBody();
        String mockId = "12345";

        Map<String, Object> params = new HashMap<>();
        params.put("userId", mockId);
        params.put("userOTP", mockOtp);

        this.mockMvc.perform(post("/alexa/linkUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andDo(document("alexa-link-user",
                        requestFields(fieldWithPath("userId").description("The user Id received from Alexa"),
                                fieldWithPath("userOTP").description("Code user created from UI")
                        )));
    }


    @Test
    public void testLinkUserFailed() throws Exception {
        String mockId = "12345";
        String mockOtp = "123456";

        Map<String, Object> params = new HashMap<>();
        params.put("userId", mockId);
        params.put("userOTP", mockOtp);

        this.mockMvc.perform(post("/alexa/linkUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andDo(document("alexa-link-user-failed"));
    }
}
