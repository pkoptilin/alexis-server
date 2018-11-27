import React from 'react';
import {
  loadActiveWordGroups, getSetupConfig, setApproach, setDefaultWGroup, setSuccessApproach,
} from '../../Setup/actions/setupActions';
import {
  LOAD_ACTIVE_WORDGROUPS, GET_CONFIG, SET_APPROACH, SET_DEFAULT_WGROUP, SET_S_APPROACH,
} from '../../Setup/constans/setup';

// test loadActiveWordGroups
test('should get active word groups from the server', () => {
  const action = loadActiveWordGroups({
    id: 30,
    name: 'New test group',
    activeState: true,
    userId: 1,
  });
  expect(action).toEqual({
    resData: {
      id: 30,
      name: 'New test group',
      activeState: true,
      userId: 1,

    },
    type: LOAD_ACTIVE_WORDGROUPS,
  });
});

// test getSetupConfig
test('should get setup configuration from the server', () => {
  const action = getSetupConfig({
    defaultGroupId: 32,
    failApproach: 1,
  });
  expect(action).toEqual({
    resConfig: {
      defaultGroupId: 32,
      failApproach: 1,
    },
    type: GET_CONFIG,
  });
});

// test setApproach
test('should update user Fail Approaches', () => {
  const approach = 3;
  const action = setApproach(approach);
  expect(action).toEqual({
    type: SET_APPROACH,
    approach: 3,
  });
});

// test setSuccessApproach
test('should update user Success Approaches', () => {
  const successApproach = 1;
  const action = setSuccessApproach(successApproach);
  expect(action).toEqual({
    type: SET_S_APPROACH,
    successApproach: 1,
  });
});

// test setDefaultWGroup
test('should update Default Word Group', () => {
  const wGroup = 6;
  const action = setDefaultWGroup(wGroup);
  expect(action).toEqual({
    type: SET_DEFAULT_WGROUP,
    wGroup: 6,
  });
});
