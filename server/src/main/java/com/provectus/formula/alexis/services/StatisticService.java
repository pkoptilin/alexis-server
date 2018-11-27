package com.provectus.formula.alexis.services;

import com.provectus.formula.alexis.models.entities.ConfigurationEntity;
import com.provectus.formula.alexis.models.entities.GroupEntity;
import com.provectus.formula.alexis.models.entities.StatisticEntity;
import com.provectus.formula.alexis.models.entities.UsersEntity;
import com.provectus.formula.alexis.models.statistic.AmountStatisticReturn;
import com.provectus.formula.alexis.models.statistic.StatisticPageReturn;
import com.provectus.formula.alexis.models.statistic.WordStatisticReturn;
import com.provectus.formula.alexis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticService {

    @Autowired
    StatisticRepository statisticRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    StatisticPaginationRepository statisticPaginationRepository;
    @Autowired
    WordRepository wordRepository;
    @Autowired
    ConfigurationRepository configurationRepository;

    private int OBJECTS_ON_PAGE = 10;

    public AmountStatisticReturn getAmountStatistic(long groupId, String email) {
        UsersEntity user = userRepository.findByEmail(email);
        ConfigurationEntity conf = configurationRepository.getByUserId((long) user.getId());
        int isLearned = conf.getSuccessApproach();

        int learned;
        int inProgress;

        if (groupId == 0) {//all
            learned = statisticRepository.countLearnedStatisticByUserId(user.getId(), isLearned);
            inProgress = statisticRepository.countInProgressStatisticByUserId(user.getId(), isLearned);
        } else {
            GroupEntity group = groupRepository.getOne(groupId);
            if (group == null)
                return new AmountStatisticReturn("no such group");
            learned = statisticRepository.countLearnedStatisticByGroupId(group.getId(), isLearned);
            inProgress = statisticRepository.countInProgressStatisticByGroupId(group.getId(), isLearned);
        }

        return new AmountStatisticReturn("OK", inProgress, learned);

    }

    public StatisticPageReturn getStatisticList(long groupId, String email, int page, boolean learned) {
        UsersEntity user = userRepository.findByEmail(email);
        ConfigurationEntity conf = configurationRepository.getByUserId((long) user.getId());
        int isLearned = conf.getSuccessApproach();
        Page<StatisticEntity> statisticPage;
        page--;

        if (groupId == 0) {//all
            if (learned) {
                statisticPage = statisticPaginationRepository
                        .getLearnedStatisticPageByUserId(user.getId(), isLearned, new PageRequest(page, OBJECTS_ON_PAGE));
            } else {
                statisticPage = statisticPaginationRepository
                        .getInProgressStatisticPageByUserId(user.getId(), isLearned, new PageRequest(page, OBJECTS_ON_PAGE));
            }
        } else {
            GroupEntity group = groupRepository.getOne(groupId);
            if (group == null)
                return new StatisticPageReturn("no such group");
            if (learned) {
                statisticPage = statisticPaginationRepository
                        .getLearnedStatisticPageByGroupId(group.getId(), isLearned, new PageRequest(page, OBJECTS_ON_PAGE));
            } else {
                statisticPage = statisticPaginationRepository
                        .getInProgressStatisticPageByGroupId(group.getId(), isLearned, new PageRequest(page, OBJECTS_ON_PAGE));
            }
        }

        List<StatisticEntity> content = statisticPage.getContent();

        List<WordStatisticReturn> statistic = new ArrayList<>();
        for (int i = 0; i < content.size(); i++) {
            String ruWord = wordRepository.getById(content.get(i).getWordRelationId()).getRuWordEntity().getRuWord();
            statistic.add(new WordStatisticReturn(ruWord, content.get(i).getCorrectAnswers(),
                    content.get(i).getAllAnswers() - content.get(i).getCorrectAnswers()));
        }

        return new StatisticPageReturn("OK", statisticPage.getTotalPages(), statistic);

    }
}
