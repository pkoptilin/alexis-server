// actions constants
export const LOAD_DATA = 'LOAD_DATA';

export const ADD_WORDGROUP = 'ADD_WORDGROUP';

export const DELETE_WORDGROUP = 'DELETE_WORDGROUP';

export const TOGGLE_STATUS = 'TOGGLE_STATUS';

export const EDIT_WORDGROUP = 'EDIT_WORDGROUP';

// component constants

export const errWordGroupName = 'Please enter a valid word group name';
export const errServerConnection = 'The server connection failed';
export const existingGroupNameErr = 'A word group with this name already exists!';
export const newWordGroupName = 'new group 1';
export const pageTitle = 'Word Groups';

// user const from local storage
export const user = JSON.parse(localStorage.getItem('userInfo'));
