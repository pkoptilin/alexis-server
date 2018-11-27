import {
  LOAD_DATA, ADD_WORDGROUP, DELETE_WORDGROUP, TOGGLE_STATUS, EDIT_WORDGROUP,
} from '../constans/constants';

export const loadData = dataSource => ({
  type: LOAD_DATA,
  dataSource,
});

export const addWordGroup = newWordGroup => ({
  type: ADD_WORDGROUP,
  newWordGroup,
});


export const deleteWordGroup = id => ({
  type: DELETE_WORDGROUP,
  id,
});

export const toggleStatus = newData => ({
  type: TOGGLE_STATUS,
  newData,
});

export const editWordGroup = newData => ({
  type: EDIT_WORDGROUP,
  newData,
});
