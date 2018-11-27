package com.provectus.formula.alexis.services;

import com.provectus.formula.alexis.models.entities.ConfigurationEntity;
import com.provectus.formula.alexis.models.entities.GroupEntity;
import com.provectus.formula.alexis.models.entities.UsersEntity;
import com.provectus.formula.alexis.models.quiz.AlexaAnswer;
import com.provectus.formula.alexis.models.quiz.NextWord;
import com.provectus.formula.alexis.models.quiz.NextWordsReturn;
import com.provectus.formula.alexis.models.quiz.Quiz;
import com.provectus.formula.alexis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    WordRepository wordRepository;

    @Autowired
    RuWordRepository ruWordRepository;

    @Autowired
    ConfigurationRepository configurationRepository;

    @Autowired
    StatisticRepository statisticRepository;


    public NextWordsReturn nextWords(String groupName, AlexaAnswer alexaAnswer) {
        List<UsersEntity> byAwsid = userRepository.findByAwsid(alexaAnswer.getAlexaId());
        if (byAwsid.size() != 0) {
            UsersEntity user = byAwsid.get(0);
            GroupEntity group = null;

            if (groupName.equals("default")) {
                ConfigurationEntity configuration = configurationRepository.getByUserId((long) user.getId());
                //check default group and presence of words in default group
                if (wordRepository.countWordsInGroup(configuration.getDefaultGroupId()) != 0) {
                    groupName = groupRepository.getOne(configuration.getDefaultGroupId()).getName();
                } else {
                    groupName = "all";
                }
            }

            //check incoming data and get word group
            if (groupName.equals("all")) {//group = null for all
                if (wordRepository.countAllUsersWords((long) user.getId()) == 0) {
                    return new NextWordsReturn("no groups or no words in the groups");
                }

            } else {
                group = groupRepository.findWordGroupByNameAndUserId(groupName, (long) user.getId());
                if (group == null || wordRepository.countWordsInGroup(group.getId()) == 0) {
                    return new NextWordsReturn("no such group or no words in the group");
                }
            }

            //create answer with next words
            Quiz quiz = new Quiz(statisticRepository, wordRepository, user, group, alexaAnswer.getAnswers());
            quiz.cleanQuiz();
            quiz.updateQuizStatistic();
            List<NextWord> nextWords = quiz.getNextWords();
            return new NextWordsReturn("OK", nextWords);
        }
        return new NextWordsReturn("no such user");
    }
}

