package com.provectus.formula.alexis.services;

import com.provectus.formula.alexis.DAO.EnWordDAO;
import com.provectus.formula.alexis.models.entities.EnWordEntity;
import com.provectus.formula.alexis.repository.EnWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnWordService implements EnWordDAO {
    @Autowired
    private EnWordRepository enWordRepository;

    @Override
    public EnWordEntity findById(Long id) {
        return enWordRepository.getById(id);
    }

    @Override
    public EnWordEntity findByEnWord(String enWord) {
        return enWordRepository.getByEnWord(enWord);
    }

    @Override
    public void deleteEnWord(Long id) {
        enWordRepository.delById(id);
    }

    @Override
    public EnWordEntity saveEnWord(EnWordEntity enWordEntity) {
        return enWordRepository.saveAndFlush(enWordEntity);
    }

    @Override
    public List<String> findLikeEnWord(String enWord) {
        List<String> outWords = enWordRepository.findAllByEnWordLike(enWord);
        if (outWords.size() > 10) outWords = outWords.subList(0, 10);
        return outWords;
    }
}
