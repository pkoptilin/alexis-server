import { LOGIN, LOGOUT } from '../constants/constanst';

const defaultUserState = {
  name: '',
  image: '',
  email: '',
  awsExist: '',
};

export default (state = defaultUserState, action) => {
  const {
    name, image, email, awsExist,
  } = action;
  switch (action.type) {
    case LOGIN: {
      return Object.assign({}, state, {
        name,
        image,
        email,
        awsExist,
      });
    }
    case LOGOUT: {
      return {};
    }
    default:
      return state;
  }
};
