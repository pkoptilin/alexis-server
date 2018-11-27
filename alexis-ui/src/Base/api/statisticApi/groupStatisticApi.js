import axios from 'axios';
import { mainUrl } from '../auth/constants';

const groupStatistApi = async (token, idGroup) => {
    console.log(token)
  const response = await axios({
    method: 'get',
    url: `${mainUrl}/api/alexa/quiz/statistic/amount/${idGroup}`,
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


export default groupStatistApi;
