import {
  createStore, combineReducers, applyMiddleware, compose,
} from 'redux';
import thunk from 'redux-thunk';
import userReducer from '../../Login/reducers/auth';
import wordGroupsReducer from '../../WordGroups/reducers/wordGroupsReducer';
import WordsReducer from '../../Words/reducers/WordsReducer';
import alexisPassReducer from '../../AlexisPassword/reducers/alexisPassword';
import setupReducer from '../../Setup/reducers/setupReducer';
import pieReduser from '../../Statistics/reducers/pieReducer'
/* eslint-disable no-underscore-dangle */
const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
/* eslint-enable */
export default () => {
  const store = createStore(
    combineReducers({
      userInfo: userReducer,
      wordGroups: wordGroupsReducer,
      words: WordsReducer,
      alexisPass: alexisPassReducer,
      setup: setupReducer,
      pie: pieReduser,
    }),
    composeEnhancers(applyMiddleware(thunk)),
  );

  return store;
};
