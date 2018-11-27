import React from 'react';
import { connect } from 'react-redux';
import { history } from '../../Base/routers/AppRouter';
import { logout, clearState } from '../actions/auth';
import { LogoutText } from '../constants/constanst';


class Logout extends React.Component {
  logoutBtn = () => {
    history.push('/');
    const { logout } = this.props;
    logout();
    localStorage.removeItem('userInfo');
    const { clearState } = this.props;
    clearState();
  };

  render() {
    return (
      <button
        type="submit"
        onClick={this.logoutBtn}
        className="ant-btn ant-btn-primary"
      >
        {LogoutText}
      </button>
    );
  }
}


const mapStateToProps = state => ({
  userInfo: state.userInfo,
  dataSource: state.wordGroups.dataSource,
});

const mapDispatchToProps = dispatch => ({
  logout: () => {
    dispatch(logout());
  },
  clearState: () => {
    dispatch(clearState());
  },
});

export default connect(mapStateToProps, mapDispatchToProps)(Logout);
