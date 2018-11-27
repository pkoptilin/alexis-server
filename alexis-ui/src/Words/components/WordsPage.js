import React from 'react';
import { pageTitle } from '../constants/constants';
// components
import WrappedWordsTable from './Words';

const WordsPage = props => (
  <div className="wordsTable page">
    <h1 className="page__title">
      {pageTitle}
    </h1>
    <WrappedWordsTable {...props} />
  </div>
);

export default WordsPage;
