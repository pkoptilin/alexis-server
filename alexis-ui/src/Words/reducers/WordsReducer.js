import {
  LOAD_WORDS_DATA, ADD_WORD, DELETE_WORD, CLEAR_ALL_WORDS,
} from '../constants/constants';

const initialState = {
  dataSource: [],
};

export default (state = initialState, action) => {
  switch (action.type) {
    case LOAD_WORDS_DATA: {
      return Object.assign({}, state, {
        dataSource: action.dataSource,
      });
    }
    case ADD_WORD: {
      return Object.assign({}, state, {
        dataSource: [action.newWord, ...state.dataSource],
      });
    }
    case DELETE_WORD: {
      return Object.assign({}, state, {
        dataSource: [...state.dataSource.filter(item => item.id !== action.id)],
      });
    }
    case CLEAR_ALL_WORDS: {
      return Object.assign({}, state, {
        dataSource: [],
      });
    }
    default:
      return state;
  }
};
