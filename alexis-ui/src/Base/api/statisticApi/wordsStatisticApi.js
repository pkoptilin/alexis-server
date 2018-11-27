import axios from 'axios';
import { mainUrl } from '../auth/constants';

const wordsStatisticApi = async (token, idGroup, statusWords, pageNumber = 1) => {
  const response = await axios({
    method: 'get',
    url: `${mainUrl}/api/alexa/quiz/statistic/${statusWords}/${idGroup}?pageNumber=${pageNumber}`,
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


export default wordsStatisticApi;
