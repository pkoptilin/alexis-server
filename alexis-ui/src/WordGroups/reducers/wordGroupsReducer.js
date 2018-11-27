import {
  LOAD_DATA, ADD_WORDGROUP, DELETE_WORDGROUP, TOGGLE_STATUS, EDIT_WORDGROUP,
} from '../constans/constants';

import { CLEAR_ALL } from '../../Login/constants/constanst';

const initialState = {
  editingKey: '',
  stateKey: '',
  dataSource: [],
};

export default (state = initialState, action) => {
  switch (action.type) {
    case LOAD_DATA: {
      return Object.assign({}, state, {
        dataSource: action.dataSource,
      });
    }
    case ADD_WORDGROUP: {
      return Object.assign({}, state, {
        dataSource: [action.newWordGroup, ...state.dataSource],
      });
    }
    case DELETE_WORDGROUP: {
      return Object.assign({}, state, {
        dataSource: [...state.dataSource.filter(item => item.id !== action.id)],
      });
    }
    case TOGGLE_STATUS: {
      return Object.assign({}, state, {
        dataSource: action.newData,
      });
    }
    case EDIT_WORDGROUP: {
      return Object.assign({}, state, {
        dataSource: action.newData,
      });
    }
    case CLEAR_ALL: {
      return Object.assign({}, state, {
        dataSource: [],
      });
    }
    default:
      return state;
  }
};
