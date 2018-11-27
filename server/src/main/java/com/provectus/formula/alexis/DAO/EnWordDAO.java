package com.provectus.formula.alexis.DAO;

import com.provectus.formula.alexis.models.entities.EnWordEntity;

import java.util.List;

public interface EnWordDAO {

    EnWordEntity findById(Long id);

    EnWordEntity findByEnWord(String enWord);

    void deleteEnWord(Long id);

    EnWordEntity saveEnWord(EnWordEntity enWordEntity);

    List<String> findLikeEnWord(String enWord);

}
