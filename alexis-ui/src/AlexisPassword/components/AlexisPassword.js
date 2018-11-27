import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button, notification } from 'antd';
import alexisPasswordApi from '../../Base/api/alexisPasswordApi/alexisPasswordApi';
import { getAlexisPass, okText, errServerConnection } from '../constants/constants';
import { getAlexisLogin } from '../actions/alexisPassword';

class AlexisPassword extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      password: '',
      visible: false,
    };
  }

  componentWillUnmount() {
    clearInterval(this.awsStatus);
  }

    getAlexisPass = async () => {
      const user = JSON.parse(localStorage.getItem('userInfo'));
      const { alexisPass, getAlexisLogin } = this.props;
      try {
        const res = await alexisPasswordApi(user.token);
        this.setState({
          visible: true,
        });
        getAlexisLogin({ alexisPassword: res });
      } catch (err) {
        notification.open({
          type: 'error',
          message: errServerConnection,
        });
      }
      this.awsStatus = setInterval(() => {
        const { isOnline } = this.props;
        if (isOnline) {
          this.setState({ visible: false });
        }
      }, 5000);
      this.btn.focus();
    }

    handleOk = (e) => {
      this.setState({
        visible: false,
      });
    }

    handleCancel = (e) => {
      this.setState({
        visible: false,
        password: '',
      });
    }

    render() {
      const { isOnline, alexisPass } = this.props;
      return (
        <div className="alexis-pass">

          <Button type="primary" disabled={!!isOnline} onClick={this.getAlexisPass}>
            {getAlexisPass}
          </Button>
          <Modal
            title={getAlexisPass}
            visible={this.state.visible}
            onOk={this.handleOk}
            onCancel={this.handleCancel}
            footer={[<button key="submit" ref={(ref) => { this.btn = ref; }} className="ant-btn ant-btn-primary" onClick={this.handleOk}>
              {okText}
                     </button>]}
          >
            <h4 className="alexis-pass__code">
              {alexisPass.alexisPassword}
            </h4>
          </Modal>
        </div>
      );
    }
}

const mapStateToProps = state => ({
  userInfo: state.userInfo,
  alexisPass: state.alexisPass,
});

const mapDispatchToProps = dispatch => ({
  getAlexisLogin: (name) => {
    dispatch(getAlexisLogin(name));
  },
});

export default connect(mapStateToProps, mapDispatchToProps)(AlexisPassword);
