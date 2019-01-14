package com.provectus.formula.alexis.DAO;

import com.provectus.formula.alexis.models.WordReturn;
import com.provectus.formula.alexis.models.entities.EnWordEntity;
import com.provectus.formula.alexis.models.entities.RuWordEntity;
import com.provectus.formula.alexis.models.entities.WordEntity;

import java.util.List;

public interface WordDAO {

    WordEntity findById(Long id);

    int countWordsInCurrentGroup(long wordId, WordReturn word);

    int countRuWordInGroup(long idRuWord);

    int countEnWordInGroup(long idEnWord);

    void deleteWordsFromGroup(long wordGroupId, long wordId);

    long insertWordsInGroup(long idGroup, EnWordEntity enWord, RuWordEntity ruWord);

    void deleteAllWordsFromGroup(long wordGroupId);

    // List<WordReturn> getWordsByWordGroup( Long groupId);

    List<WordReturn> getWordsByWordGroup(Long groupId, Long userId);

    WordReturn saveWordToWordGroup(WordReturn word, Long groupId);

    void deleteWordFromWordGroup(Long idGroup, Long wordId);

}
