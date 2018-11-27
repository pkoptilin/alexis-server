import { login, logout } from '../../../Login/actions/auth';
import { LOGIN, LOGOUT } from '../../../Login/constants/constanst';

// test login
test('should sava user info', () => {
  const action = login({ name: 'Alex', awsExist: false, email: 'test@rt.com' });
  expect(action).toEqual({
    type: LOGIN,
    name: 'Alex',
    awsExist: false,
    email: 'test@rt.com',
  });
});

// test logout
test('should logout user info', () => {
  const action = logout();
  expect(action).toEqual({
    type: LOGOUT,
  });
});
