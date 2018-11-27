import React from 'react';

// components
import WordTable from './WordTable';
import Path from './path';
import { pageTitle } from '../constans/constants';

const Wordgroups = () => (
  <div className="wordTableContainer page">
    <h1 className="page__title">
      {pageTitle}
    </h1>
    <WordTable />
  </div>
);
export default Wordgroups;
