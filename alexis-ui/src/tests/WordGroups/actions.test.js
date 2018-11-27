import {
  loadData, addWordGroup, deleteWordGroup, toggleStatus, editWordGroup,
} from '../../WordGroups/actions/wordGroups';

import {
  LOAD_DATA, ADD_WORDGROUP, DELETE_WORDGROUP, TOGGLE_STATUS, EDIT_WORDGROUP,
} from '../../WordGroups/constans/constants';

// test loadData
test('should keep data from server in store', () => {
  const action = loadData({
    id: 30,
    name: 'New testgroup',
    activeState: true,
    userId: 1,
  });
  expect(action).toEqual({
    dataSource: {
      id: 30,
      name: 'New testgroup',
      activeState: true,
      userId: 1,
    },
    type: LOAD_DATA,
  });
});

// test addWordGroup
test('should add word group to the store', () => {
  const action = addWordGroup({
    id: 28,
    name: 'New group',
    activeState: true,
    userId: 1,
  });
  expect(action).toEqual(
    {
      newWordGroup:
        {
          id: 28,
          name: 'New group',
          activeState: true,
          userId: 1,
        },
      type: ADD_WORDGROUP,
    },

  );
});

// test deleteWordGroup
test('should delete word group from the store', () => {
  const id = 53;
  const action = deleteWordGroup(id);
  expect(action).toEqual({
    type: DELETE_WORDGROUP,
    id: 53,
  });
});

// test toggleStatus
test('should store changed  status of word group', () => {
  const action = toggleStatus({
    id: 18,
    name: 'Status test',
    activeState: true,
    userId: 1,
  });
  expect(action).toEqual({
    newData: {
      id: 18,
      name: 'Status test',
      activeState: true,
      userId: 1,
    },
    type: TOGGLE_STATUS,
  });
});

// test editWordGroup
test('should store the new name of word group', () => {
  const action = editWordGroup({
    id: 12,
    name: 'WordGroup name',
    activeState: true,
    userId: 1,
  });
  expect(action).toEqual({
    newData: {
      id: 12,
      name: 'WordGroup name',
      activeState: true,
      userId: 1,
    },
    type: EDIT_WORDGROUP,
  });
});
