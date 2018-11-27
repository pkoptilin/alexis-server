import { LOGIN, LOGOUT, CLEAR_ALL } from '../constants/constanst';

export const login = ({ name, email, awsExist }) => ({
  type: LOGIN,
  name,
  email,
  awsExist,
});

export const logout = () => ({
  type: LOGOUT,
});

export const clearState = () => ({
  type: CLEAR_ALL,
});
