import {
  wGroupMessage,
} from '../constans/setup';

export const findObjectByKey = (array, key, value) => {
  if (value !== null) {
    for (let i = 0; i < array.length; i++) {
      if (array[i][key] === value) {
        return array[i].wordGroupName;
      }
    }
  }
  if (value === null) {
    return wGroupMessage;
  }
  return null;
};
