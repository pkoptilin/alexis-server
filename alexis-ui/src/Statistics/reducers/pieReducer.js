import { REDRAW, CHANGE_TO_WRONG, CHANGE_TO_SUCCESS, LOAD_WORDS, SELECT_GROUP } from '../constants/constants';
import {
  backgroundColorFalseDefault, inprogressForUrl, learnedForUrl, backgroundColorSuccess, inProcessTitleTable, succesTitleTable, backgroundColorFalse, backgroundColorSuccessDefault } from '../constants/constants'
const defaultUserState = {
  ColorFalse: backgroundColorFalseDefault,
  ColorSuccess: backgroundColorSuccess,
  inprogress: 0,
  learned: 0,
  titleTable: succesTitleTable,
  acitveFilter: learnedForUrl,
  wordsTable: [],
  activeGroupId: '',
  pagination: {},
  defaultSelectValue: '',
};

export default (state = defaultUserState, action) => {
  const { inprogress, learned, defaultSelectValue, activeGroupId, wordsTable } = action;
  switch (action.type) {
    case REDRAW: {
      return Object.assign({}, state, {
        inprogress,
        learned,
      });
    }
    case CHANGE_TO_WRONG: {
      return Object.assign({}, state, {
        ColorSuccess: backgroundColorSuccessDefault,
        ColorFalse: backgroundColorFalse,
        titleTable: inProcessTitleTable,
        acitveFilter: inprogressForUrl,
      });
    }
    case CHANGE_TO_SUCCESS: {
      return Object.assign({}, state, {
        ColorFalse: backgroundColorFalseDefault,
        ColorSuccess: backgroundColorSuccess,
        titleTable: succesTitleTable,
        acitveFilter: learnedForUrl,
      });
    }
    case SELECT_GROUP: {
      return Object.assign({}, state, {
        defaultSelectValue,
        activeGroupId,
      });
    }
    case LOAD_WORDS: {
      return Object.assign({}, state, {
        wordsTable,
      });
    }
    default:
      return state;
  }
};
