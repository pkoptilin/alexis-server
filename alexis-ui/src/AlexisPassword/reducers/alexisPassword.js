import { GETPASSWORD } from '../constants/constants';

const defaultUserState = {
  alexisPassword: '',
};

export default (state = defaultUserState, action) => {
  const {
    alexisPassword,
  } = action;
  switch (action.type) {
    case GETPASSWORD: {
      return Object.assign({}, state, {
        alexisPassword,
      });
    }
    default:
      return state;
  }
};
