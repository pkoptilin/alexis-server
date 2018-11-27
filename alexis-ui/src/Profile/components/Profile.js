import React from 'react';
import { connect } from 'react-redux';
import AlexisPassword from '../../AlexisPassword/components/AlexisPassword';
import { loginApi } from '../../Base/api/auth/authApi';
import { login } from '../../Login/actions/auth';
import {
  name, email, status, online, offline, pageTitle,
} from '../constants/constants';

class Profile extends React.Component {
  componentDidMount() {
    this.getLoginIndo = setInterval(this.checkstatus, 5000);
  }

  componentWillUnmount() {
    clearInterval(this.getLoginIndo);
  }

  checkstatus = async () => {
    const user = JSON.parse(localStorage.getItem('userInfo'));
    const { login } = this.props;
    try {
      const res = await loginApi(user.token);
      login({ ...user, awsExist: res.awsExist });
      localStorage.setItem('userInfo', JSON.stringify({ ...user, awsExist: res.awsExist }));
    } catch (err) {
      notification.open({
        type: 'error',
        message: errServerConnection,
      });
    }
  }

  render() {
    const { userInfo } = this.props;

    return (
      <div className="profile page">
        <h1 className="page__title">
          {pageTitle}
        </h1>
        <div className="profile__info">
          <p className="profile__user-name">
            <span>
              {name}
            </span>
            {userInfo.name}
          </p>
          <p className="profile__user-email">
            <span>
              {email}
            </span>
            {userInfo.email}
          </p>
          <p className="profile__status">
            {status}
            {userInfo.awsExist ? (
              <span className="profile__status-online">
                {online}
              </span>
            ) : (
              <span className="profile__status-offline">
                {offline}
              </span>
            )}
          </p>
          <p />
          <AlexisPassword isOnline={userInfo.awsExist} />
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  userInfo: state.userInfo,
});

const mapDispatchToProps = dispatch => ({
  login: (userInfo) => {
    dispatch(login(userInfo));
  },
});

export default connect(mapStateToProps, mapDispatchToProps)(Profile);
