import axios from 'axios';
import { mainUrl } from './constants';
// login

const loginApi = async (basicAuth) => {
  const response = await axios({
    method: 'get',
    url: `${mainUrl}/home`,
    data: {},
    headers: {
      Authorization: basicAuth,
    },
  });
  if (response.status <= 400) {
    return response.data;
  }
  throw new Error(response.status);
};

// registration
const registrationApi = async (user) => {
  const response = await axios.post(`${mainUrl}/user_registration`, { ...user });
  return response.status;
};

export { loginApi, registrationApi };
