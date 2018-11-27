import React from 'react';
import Adapter from 'enzyme-adapter-react-16';
import { configure, shallow } from 'enzyme';
import {
  Form,
} from 'antd';
import { WordsTable } from '../../Words/components/Words';
import {
  loadWordsData, addWord, deleteWord, clearWordsState,
} from '../../Words/actions/wordsActions';
import {
  LOAD_WORDS_DATA, ADD_WORD, DELETE_WORD,
} from '../../Words/constants/constants';


configure({ adapter: new Adapter() });


// test loadWordsData
test('should keep words data from server in store', () => {
  const action = loadWordsData({
    id: 52,
    enWord: 'Bear',
    ruWord: 'Медведь',
  });
  expect(action).toEqual({
    dataSource: {
      id: 52,
      enWord: 'Bear',
      ruWord: 'Медведь',

    },
    type: LOAD_WORDS_DATA,
  });
});

// test addWord
test('should add word to store', () => {
  const action = addWord({
    id: 71,
    enWord: 'City',
    ruWord: 'Город',
  });
  expect(action).toEqual({
    newWord: {
      id: 71,
      enWord: 'City',
      ruWord: 'Город',

    },
    type: ADD_WORD,
  });
});

// test deleteWord
test('should delete word from store', () => {
  const id = 58;
  const action = deleteWord(id);
  expect(action).toEqual({
    id: 58,
    type: DELETE_WORD,

  });
});

test('words input autocomplete test', () => {
  class LocalStorageMock {
    constructor() {
      this.store = {
        userInfo: {
          token: '',
        },
      };
    }

    clear() {
      this.store = {};
    }

    getItem(key) {
      return this.store[key] || null;
    }

    setItem(key, value) {
      this.store[key] = value.toString();
    }

    removeItem(key) {
      delete this.store[key];
    }
  }

  global.localStorage = new LocalStorageMock();
  expect(localStorage.getItem).toBeCalledWith('userInfo');
  localStorage.setItem('userInfo', JSON.stringify({ token: 'Basic c2FkYWRAZ21haWwuY29tOjExMTExMQ==' }));
  const user = JSON.parse(localStorage.getItem('userInfo'));
  expect(user.token).toBe('Basic c2FkYWRAZ21haWwuY29tOjExMTExMQ==');

  const props = {
    match: {
      params: {
        name: 'Word Group Name',
      },
    },
    clearWordsState,
  };

  const WrappedWordsTable = Form.create()(WordsTable);

  const wrapper = shallow(
    <WrappedWordsTable
      {...props}
    />,
  );

  wrapper.dive().instance().componentDidMount();
  const Wordstable = wrapper.find(WordsTable).dive().find('.words-table');
  const WordsTableForm = Wordstable.find('.wordstable-spin').find('.words-table-form');
  expect(WordsTableForm.find('.engWordInput')).toHaveLength(1);
  expect(WordsTableForm.find('.ruWordInput')).toHaveLength(1);
  expect(WordsTableForm.find('.eng-com')).toHaveLength(1);
  expect(WordsTableForm.find('.ru-com')).toHaveLength(1);
  WordsTableForm.find('.eng-com').prop('onSearch')(() => { onSearch(); });
  WordsTableForm.find('.ru-com').prop('onSearch')(() => { onSearch(); });
});
