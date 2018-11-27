package com.provectus.formula.alexis.DAO;

import com.provectus.formula.alexis.models.GroupReturn;
import com.provectus.formula.alexis.models.entities.GroupEntity;
import com.provectus.formula.alexis.models.entities.WordEntity;

import java.util.List;

public interface GroupDAO {

    GroupEntity findGroupById(Long aLong);

    GroupReturn findGroupByIdAndUserId(Long groupId, Long userId);

    List<GroupEntity> findGroupsByUserId(Long userId);

    ///maybe move in setings controller
    List<GroupEntity> findGroupsByUserIdAndActiveState(Long userId, boolean activeState);

    GroupEntity findGroupByNameAndUserId(String name, Long userId);

    GroupReturn saveGroup(GroupEntity wordGroup, Long userId);

    void deleteGroup(Long id, Long iduser);

    void updateGroup(GroupEntity wordGroup, Long iduser);

    GroupEntity findGroupByIdAndWords(Long id, WordEntity word);

    List<GroupReturn> GroupToReturn(List<GroupEntity> returnGroup);

}
