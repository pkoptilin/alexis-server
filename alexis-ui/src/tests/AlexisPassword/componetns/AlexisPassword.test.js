import { getAlexisLogin } from '../../../AlexisPassword/actions/alexisPassword';
import { GETPASSWORD } from '../../../AlexisPassword/constants/constants';

// test login
test('number shoild be equel in Alexis pass', () => {
  const action = getAlexisLogin({ alexisPassword: 12345 });
  expect(action).toEqual({
    type: GETPASSWORD,
    alexisPassword: 12345,
  });
});
