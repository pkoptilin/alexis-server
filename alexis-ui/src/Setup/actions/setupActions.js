import {
  LOAD_ACTIVE_WORDGROUPS, GET_CONFIG, SET_APPROACH, SET_DEFAULT_WGROUP, SET_S_APPROACH,
} from '../constans/setup';

export const loadActiveWordGroups = resData => ({
  type: LOAD_ACTIVE_WORDGROUPS,
  resData,
});

export const getSetupConfig = resConfig => ({
  type: GET_CONFIG,
  resConfig,
});

export const setApproach = approach => ({
  type: SET_APPROACH,
  approach,
});

export const setSuccessApproach = successApproach => ({
  type: SET_S_APPROACH,
  successApproach,
});

export const setDefaultWGroup = wGroup => ({
  type: SET_DEFAULT_WGROUP,
  wGroup,
});
