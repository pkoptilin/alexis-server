import { REDRAW, CHANGE_TO_WRONG, CHANGE_TO_SUCCESS, LOAD_WORDS, SELECT_GROUP } from '../../../Statistics/constants/constants';
import { reDrawPie, changeToInprogress, changeToLearned, selectGroup, loadWords } from '../../../Statistics/actions/pieActions'


test('should get number for pie', () => {
  const action = reDrawPie({ inprogress: 50, learned: 20 });
  expect(action).toEqual({
    type: REDRAW,
    inprogress: 50,
    learned: 20,
  });
});

test('should change to ingrogress', () => {
  const action = changeToInprogress();
  expect(action).toEqual({
    type: CHANGE_TO_WRONG,
  });
});

test('should change to learn', () => {
    const action = changeToLearned();
    expect(action).toEqual({
      type: CHANGE_TO_SUCCESS,
    });
  });

test('should change group', () => {
  const action = selectGroup({ defaultSelectValue: 'hello', activeGroupId: 15 });
  expect(action).toEqual({
    type: SELECT_GROUP,
    defaultSelectValue: 'hello',
    activeGroupId: 15,
  });
});

test('should change group', () => {
    const action = loadWords({ wordsTable: [{id: 344, title: 'hello'}] });
    expect(action).toEqual({
      type: LOAD_WORDS,
      wordsTable: [{ id: 344, title: 'hello' }],
    });
  });
