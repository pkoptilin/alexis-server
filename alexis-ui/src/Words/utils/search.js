import React from 'react';

export const searchWords = (text, searchText) => (
  <span>
    {text.split(new RegExp(`(^(?=${searchText}))|(?=${searchText})`, 'i')).map((fragment, i) => (
      fragment.toLowerCase() === searchText.toLowerCase()
                ? <span key={i} className="highlight">{fragment}</span> : fragment // eslint-disable-line
    ))}
  </span>

);
