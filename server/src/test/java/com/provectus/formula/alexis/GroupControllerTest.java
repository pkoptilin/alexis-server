package com.provectus.formula.alexis;

import com.provectus.formula.alexis.models.entities.GroupEntity;
import com.provectus.formula.alexis.models.entities.UsersEntity;
import com.provectus.formula.alexis.repository.GroupRepository;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Properties;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GroupRepository groupRepository;

    @After
    public void tearDown() {
        groupRepository.deleteAll();
    }

    @Test
    public void testGetSpeaker() throws Exception {
        // Given
        GroupEntity group = new GroupEntity();
        UsersEntity user = new UsersEntity("t2@mail.com", "111111", "test");
        Properties userRepository;
        //   userRepository.save(user);
        group.setName("groupName");
        group.setActiveState(true);
        //   group.setUserId();


        groupRepository.save(group);
/*
        // When
        ResultActions resultActions = mockMvc.perform(get("/speakers/{id}", speaker.getId()))
                .andDo(print());

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("name", is("Roman")))
                .andExpect(jsonPath("company", is("Lohika")));

*/
    }
}