package com.provectus.formula.alexis.services;

import com.provectus.formula.alexis.DAO.GroupDAO;
import com.provectus.formula.alexis.models.GroupReturn;
import com.provectus.formula.alexis.models.entities.ConfigurationEntity;
import com.provectus.formula.alexis.models.entities.GroupEntity;
import com.provectus.formula.alexis.models.entities.WordEntity;
import com.provectus.formula.alexis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService implements GroupDAO {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private EnWordRepository enWordRepository;
    @Autowired
    private RuWordRepository ruWordRepository;
    @Autowired
    private ConfigurationRepository configurationRepository;


    @Override
    public GroupEntity findGroupById(Long aLong) {
        return groupRepository.getOne(aLong);
    }

    @Override
    public GroupReturn findGroupByIdAndUserId(Long groupId, Long userId) {
        GroupReturn outGroup = new GroupReturn();
        GroupEntity findGroup = groupRepository.findByIdAndUserId(groupId, userId);
        if (findGroup != null) {
            outGroup.setId(findGroup.getId());
            outGroup.setName(findGroup.getName());
            outGroup.setActiveState(findGroup.getActiveState());
            outGroup.setUserId(findGroup.getUserId());
        }
        return outGroup;
    }

    @Override
    public List<GroupEntity> findGroupsByUserId(Long userId) {
        return groupRepository.findWordGroupByUserId(userId);
    }

    ///maybe move in configuration controller
    @Override
    public List<GroupEntity> findGroupsByUserIdAndActiveState(Long userId, boolean activeState) {
        return groupRepository.findWordGroupByUserIdAndActiveState(userId, activeState);
    }


    @Override
    public GroupEntity findGroupByNameAndUserId(String name, Long userId) {
        return groupRepository.findWordGroupByNameAndUserId(name, userId);
    }

    @Override
    public GroupReturn saveGroup(GroupEntity wordGroup, Long iduser) {

        GroupReturn outGroup = new GroupReturn();
        //
        if ((groupRepository.findWordGroupByNameAndUserId(wordGroup.getName(), iduser))
                != null) {
            throw new WordGroupDuplicateException(wordGroup.getName());
        } else {
            wordGroup.setUserId(iduser);
            GroupEntity saveGroup = groupRepository.saveAndFlush(wordGroup);
            outGroup.setId(saveGroup.getId());
            outGroup.setName(saveGroup.getName());
            outGroup.setActiveState(saveGroup.getActiveState());
            outGroup.setUserId(saveGroup.getUserId());

            return outGroup;
        }
    }

    @Override
    public void deleteGroup(Long id, Long iduser) {
        if (iduser.equals(findGroupById(id).getUserId())) {

            List<WordEntity> delWords = findGroupById(id).getWords();
            System.out.println(delWords.toString());
            wordRepository.deleteAllWordsFromGroup(id);
            ;
            for (int i = 0; i < delWords.size(); i++) {

                if (wordRepository.countEnWordInGroup(delWords.get(i).getEnWordEntity().getId()) < 1) {
                    enWordRepository.delById(delWords.get(i).getEnWordEntity().getId());
                }
                if (wordRepository.countRuWordInGroup(delWords.get(i).getRuWordEntity().getId()) < 1) {
                    ruWordRepository.delById(delWords.get(i).getRuWordEntity().getId());
                }
            }
            Long idDefoultGroup = configurationRepository.getByUserId(iduser).getDefaultGroupId();
            if (id.equals(idDefoultGroup)) {
                ConfigurationEntity currentConfiguration = configurationRepository.getByUserId(iduser);
                currentConfiguration.setDefaultGroupId(null);
                configurationRepository.saveAndFlush(currentConfiguration);
            }
            groupRepository.delById(id);
        }
    }

    @Override
    public void updateGroup(GroupEntity wordGroup, Long iduser) {

        if (groupRepository.findWordGroupByNameAndUserId(wordGroup.getName(), iduser) != null &&
                !groupRepository.findWordGroupByNameAndUserId(wordGroup.getName(), iduser).getId()
                        .equals(wordGroup.getId()))
            throw new WordGroupDuplicateException(wordGroup.getName());

        if (groupRepository.findWordGroupByNameAndUserId(wordGroup.getName(), iduser) == null || (
                groupRepository.findWordGroupByNameAndUserId(wordGroup.getName(), iduser) != null &&
                        groupRepository.findWordGroupByNameAndUserId(wordGroup.getName(), iduser).getId()
                                .equals(wordGroup.getId()))) {
            wordGroup.setUserId(iduser);
            if (wordGroup.getId() < 1) wordGroup.setId(groupRepository.findWordGroupByName(
                    wordGroup.getName()).getId());
            {
                groupRepository.saveAndFlush(wordGroup);
            }
        }


    }

    @Override
    public GroupEntity findGroupByIdAndWords(Long userId, WordEntity word) {
        return groupRepository.findGroupByIdAndWords(userId, word);
    }

    public List<GroupReturn> GroupToReturn(List<GroupEntity> returnGroup) {
        List<GroupReturn> outGroup = new ArrayList<>();
        for (int i = 0; i < returnGroup.size(); i++) {
            GroupReturn tmpGroup = new GroupReturn();
            tmpGroup.setId(returnGroup.get(i).getId());
            tmpGroup.setName(returnGroup.get(i).getName());
            tmpGroup.setActiveState(returnGroup.get(i).getActiveState());
            tmpGroup.setUserId(returnGroup.get(i).getUserId());
            outGroup.add(tmpGroup);
        }
        return outGroup;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public class WordGroupDuplicateException extends RuntimeException {
        public WordGroupDuplicateException(String wordGroupName) {
            super("Word Group duplicate: " + wordGroupName);
        }
    }
}
