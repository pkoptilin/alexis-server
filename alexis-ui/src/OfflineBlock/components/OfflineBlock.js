import React from 'react';
import { intertenGoesWrong, upps } from '../constants/constans';

export default () => (
  <div className="offline">
    <p className="offline__message">
      {upps}
    </p>
    <p className="offline__message">
      {intertenGoesWrong}
    </p>
  </div>
);
