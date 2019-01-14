package com.provectus.formula.alexis.services;

import com.provectus.formula.alexis.DAO.WordDAO;
import com.provectus.formula.alexis.models.WordReturn;
import com.provectus.formula.alexis.models.entities.EnWordEntity;
import com.provectus.formula.alexis.models.entities.GroupEntity;
import com.provectus.formula.alexis.models.entities.RuWordEntity;
import com.provectus.formula.alexis.models.entities.WordEntity;
import com.provectus.formula.alexis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@Service
public class WordService implements WordDAO {
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private EnWordRepository enWordRepository;
    @Autowired
    private RuWordRepository ruWordRepository;
    @Autowired
    private StatisticRepository statisticRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private TextToSpeechService textToSpeechService;

    @Override
    public WordEntity findById(Long id) {
        return wordRepository.getById(id);
    }

    @Override
    public int countWordsInCurrentGroup(long idGroup, WordReturn word) {

        long idEnWord = -1;
        long idRuWord = -1;
        if (enWordRepository.getByEnWord(word.getEnWord()) != null &&
                ruWordRepository.getByRuWord(word.getRuWord()) != null) {
            idEnWord = enWordRepository.getByEnWord(word.getEnWord()).getId();
            idRuWord = ruWordRepository.getByRuWord(word.getRuWord()).getId();

            return wordRepository.countWordsInCurrentGroup(idGroup, idEnWord, idRuWord);
        } else return -1;
    }


    @Override
    public int countRuWordInGroup(long idRuWord) {
        return wordRepository.countRuWordInGroup(idRuWord);
    }

    @Override
    public int countEnWordInGroup(long idEnWord) {
        return wordRepository.countEnWordInGroup(idEnWord);
    }

    @Override
    public void deleteWordsFromGroup(long wordGroupId, long wordId) {
        Long idEnWord = wordRepository.getById(wordId).getEnWordEntity().getId();
        Long idRuWord = wordRepository.getById(wordId).getRuWordEntity().getId();
        wordRepository.deleteWordsFromGroup(wordGroupId, wordId);
        if (wordRepository.countEnWordInGroup(idEnWord) < 1) {
            enWordRepository.delById(idEnWord);
        }
        if (wordRepository.countRuWordInGroup(idRuWord) < 1) {
            ruWordRepository.delById(idRuWord);
        }
        statisticRepository.deleteStatisticByWordRelationId(wordId);
    }

    @Override
    public long insertWordsInGroup(long idGroup, EnWordEntity enWord, RuWordEntity ruWord) {
        GroupEntity groupEntity = groupRepository.getOne(idGroup);
        WordEntity wordEntity = new WordEntity();
        wordEntity.setEnWordEntity( enWord );
        wordEntity.setRuWordEntity( ruWord );
        wordEntity.setWordGroup( groupEntity );
        WordEntity result = wordRepository.save(wordEntity);

//        wordRepository.insertWordsInGroup(idGroup, idEnWord, idRuWord);
//        wordRepository.flush();
//        long wordID = wordRepository.idWordsInCurrentGroup(idGroup, idEnWord, idRuWord);
        statisticRepository.insertStatisticInWord(result.getId());
        statisticRepository.flush();

        groupEntity.getWords().add( wordEntity );
        groupRepository.save( groupEntity );

        return result.getId();
    }

    @Override
    public void deleteAllWordsFromGroup(long wordGroupId) {
        List<WordEntity> tmpWords = groupRepository.getOne(wordGroupId).getWords();
        for (int i = 0; i < tmpWords.size(); i++) {
            statisticRepository.deleteStatisticByWordRelationId(tmpWords.get(i).getId());
        }
        wordRepository.deleteAllWordsFromGroup(wordGroupId);
    }

    @Override
    public List<WordReturn> getWordsByWordGroup(Long groupId, Long userId) {
        GroupEntity tmpGroup = groupRepository.findByIdAndUserId(groupId, userId);
        if (tmpGroup != null) {
            List<WordEntity> tmpWords = tmpGroup.getWords();

            List<WordReturn> outWords = new ArrayList<>();

            for (int i = 0; i < tmpWords.size(); i++) {
                WordReturn outWord = new WordReturn();
                outWord.setId(tmpWords.get(i).getId());
                outWord.setEnWord(tmpWords.get(i).getEnWordEntity().getEnWord());
                outWord.setRuWord(tmpWords.get(i).getRuWordEntity().getRuWord());
                outWord.setFileName(tmpWords.get(i).getRuWordEntity().getFileName());
                outWords.add(outWord);
            }
            return outWords;
        } else {
            throw new errGroupException();
        }
    }

    @Override
    public synchronized WordReturn saveWordToWordGroup(WordReturn word, Long groupId) {

        if (countWordsInCurrentGroup(groupId, word) < 1) {

            EnWordEntity enWordSave;
            RuWordEntity ruWordSave;
            if (enWordRepository.getByEnWord(word.getEnWord()) == null) {
                EnWordEntity enWordEntity = new EnWordEntity();
                enWordEntity.setEnWord(word.getEnWord());
                enWordSave = enWordRepository.saveAndFlush(enWordEntity);
            } else {
                enWordSave = enWordRepository.getByEnWord(word.getEnWord());
            }
            if (ruWordRepository.getByRuWord(word.getRuWord()) == null) {
                RuWordEntity ruWordEntity = new RuWordEntity();
                ruWordEntity.setRuWord(word.getRuWord());

                // generate mp3 and filename

                String fileName = textToSpeechService.convertToSpeech(ruWordEntity.getRuWord());
                ruWordEntity.setFileName(fileName);
                word.setFileName(fileName);

                ruWordSave = ruWordRepository.saveAndFlush(ruWordEntity);
            } else {
                RuWordEntity tmpwrd;
                tmpwrd = ruWordRepository.getByRuWord(word.getRuWord());
                ruWordSave = tmpwrd;
                word.setFileName(tmpwrd.getFileName());
            }


            word.setId(insertWordsInGroup(groupId, enWordSave, ruWordSave));

            return word;
        } else {
            throw new wordExistsException();
        }

    }

    @Override
    public void deleteWordFromWordGroup(Long idGroup, Long wordId) {
        WordEntity delWord = findById(wordId);
        deleteWordsFromGroup(idGroup, wordId);
        if (countEnWordInGroup(delWord.getEnWordEntity().getId()) < 1) {
            enWordRepository.deleteById(delWord.getEnWordEntity().getId());
        }
        if (countRuWordInGroup(delWord.getRuWordEntity().getId()) < 1) {
            ruWordRepository.deleteById(delWord.getRuWordEntity().getId());
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public class wordExistsException extends RuntimeException {
        public wordExistsException() {
            super("This words is contains in current group");
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public class errGroupException extends RuntimeException {
        public errGroupException() {
            super("Group is not belong current user");
        }
    }


}
