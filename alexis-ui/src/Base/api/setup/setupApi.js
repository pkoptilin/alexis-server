import axios from 'axios';
import { mainUrl } from '../auth/constants';

const configApi = async (token) => {
  const response = await axios({
    method: 'get',
    url: `${mainUrl}/api/alexa/configuration`,
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
export { configApi };
