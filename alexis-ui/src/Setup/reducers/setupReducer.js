import {
  LOAD_ACTIVE_WORDGROUPS, GET_CONFIG, SET_APPROACH, SET_DEFAULT_WGROUP, SET_S_APPROACH,
} from '../constans/setup';

const initialState = {
  activeWordGroups: [],
  userFailApproaches: [],
  defaultWordGroup: [],
  userSuccessApproach: [],
};

export default (state = initialState, action) => {
  switch (action.type) {
    case LOAD_ACTIVE_WORDGROUPS: {
      return Object.assign({}, state, {
        activeWordGroups: action.resData,
      });
    }
    case GET_CONFIG: {
      return Object.assign({}, state, {
        // userFailApproaches: action.resConfig.approach,
        defaultWordGroup: action.resConfig.defaultGroupId,
        userFailApproaches: action.resConfig.failApproach,
        userSuccessApproach: action.resConfig.successApproach,
      });
    }
    case SET_APPROACH: {
      return Object.assign({}, state, {
        userFailApproaches: action.approach,
      });
    }
    case SET_S_APPROACH: {
      return Object.assign({}, state, {
        userSuccessApproach: action.successApproach,
      });
    }
    case SET_DEFAULT_WGROUP: {
      return Object.assign({}, state, {
        defaultWordGroup: action.wGroup,
      });
    }
    default:
      return state;
  }
};
