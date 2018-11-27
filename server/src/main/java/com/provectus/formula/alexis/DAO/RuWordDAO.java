package com.provectus.formula.alexis.DAO;

import com.provectus.formula.alexis.models.entities.RuWordEntity;

import java.util.List;

public interface RuWordDAO {

    RuWordEntity findById(Long id);

    RuWordEntity findByRuWord(String ruWord);

    RuWordEntity findByFileName(String fileName);

    RuWordEntity saveRuWord(RuWordEntity ruWordEntity);

    void deleteRuWord(Long id);

    List<String> findLikeRuWord(String ruWord);

}
