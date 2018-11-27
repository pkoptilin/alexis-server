package com.provectus.formula.alexis.services;

import com.provectus.formula.alexis.DAO.RuWordDAO;
import com.provectus.formula.alexis.models.entities.RuWordEntity;
import com.provectus.formula.alexis.repository.RuWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuWordService implements RuWordDAO {
    @Autowired
    private RuWordRepository ruWordRepository;

    @Override
    public RuWordEntity findById(Long id) {
        return ruWordRepository.getById(id);
    }

    @Override
    public RuWordEntity findByRuWord(String ruWord) {
        return ruWordRepository.getByRuWord(ruWord);
    }

    @Override
    public RuWordEntity findByFileName(String fileName) {
        return findByFileName(fileName);
    }

    @Override
    public RuWordEntity saveRuWord(RuWordEntity ruWordEntity) {
        return ruWordRepository.saveAndFlush(ruWordEntity);
    }

    @Override
    public void deleteRuWord(Long id) {
        ruWordRepository.delById(id);
    }

    @Override
    public List<String> findLikeRuWord(String ruWord) {
        List<String> outWords = ruWordRepository.findAllByRuWordLike(ruWord);
        if (outWords.size() > 10) outWords = outWords.subList(0, 10);
        return outWords;
    }
}
