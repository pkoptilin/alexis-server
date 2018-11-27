import { GETPASSWORD } from '../constants/constants';

export const getAlexisLogin = ({ alexisPassword }) => ({
  type: GETPASSWORD,
  alexisPassword,
});
