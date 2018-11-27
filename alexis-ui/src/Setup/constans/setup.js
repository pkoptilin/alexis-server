import React from 'react';
// actions constants
export const LOAD_ACTIVE_WORDGROUPS = 'LOAD_ACTIVE_WORDGROUPS';
export const GET_CONFIG = 'GET_CONFIG';
export const SET_APPROACH = 'SET_APPROACH';
export const SET_DEFAULT_WGROUP = 'SET_DEFAULT_WGROUP';
export const SET_S_APPROACH = 'SET_S_APPROACH';

// components constants
export const approaches = ['1', '2', '3'];
export const wGroupMessage = 'Default';
export const mainSetupText = 'You can choose the number of fail and successful approaches, and also the default group';
export const selectClasses = 'select-block-item select-item select-input fail-num-select';
export const selectOnSelectClass = 'select-block-item select-item select-input fail-num-select select-input-selected-color';
export const defaultWordGroupContent = 'The Quiz start group. If the value is set to default - the Quiz will be started for words from all groups';
export const failApproachesContent = 'The acceptable approaches number of incorrectly named word';
export const successApproachesContent = 'The approaches number of correctly named word to consider it learned';
