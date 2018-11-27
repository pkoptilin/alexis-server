import axios from 'axios';
import { mainUrl } from '../auth/constants';
import { user } from './constants';

// load data from server
const wordGroupsApi = async (token) => {
  const response = await axios({
    method: 'get',
    url: `${mainUrl}/home/wordgroups/`,
    data: {},
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
  });

  if (response.status <= 400) {
    return response.data;
  }
  throw new Error(response.status);
};

export { wordGroupsApi };
