import {
  LOAD_WORDS_DATA, ADD_WORD, DELETE_WORD, CLEAR_ALL_WORDS,
} from '../constants/constants';

export const loadWordsData = dataSource => ({
  type: LOAD_WORDS_DATA,
  dataSource,
});

export const addWord = newWord => ({
  type: ADD_WORD,
  newWord,
});


export const deleteWord = id => ({
  type: DELETE_WORD,
  id,
});

export const clearWordsState = () => ({
  type: CLEAR_ALL_WORDS,
});
