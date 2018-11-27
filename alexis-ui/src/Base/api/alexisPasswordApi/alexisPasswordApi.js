import axios from 'axios';
import { mainUrl } from '../auth/constants';

const alexisPasswordApi = async (token) => {
  const response = await axios({
    method: 'get',
    url: `${mainUrl}/alexa/userOtp/`,
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


export default alexisPasswordApi;
