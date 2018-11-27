import React from 'react';
import {
  Form, Icon, Input, Button, message, notification,
} from 'antd';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import {
  ErroLoginPopUp, ErrorEmailInput, ErrorPasswordInput, PlaceholderEmail, PlaceholderPassword, latinLettersOnly,
} from '../constants/constanst';
import { login } from '../actions/auth';
import { history } from '../../Base/routers/AppRouter';
import {
  LoginTextBnt, RegisterNowText, LoginText, Or,
} from '../constants/constanst';
import { loginApi } from '../../Base/api/auth/authApi';

const FormItem = Form.Item;

class NormalLoginForm extends React.Component {
    state = {
      email: '',
      password: '',
      isCapsLockOn: false,
    }

    handleChangeEmail = (event) => {
      this.setState({ email: event.target.value });
    }

    checkCapsLock = (event) => {
      if (event.getModifierState('CapsLock')) {
        if (!this.state.isCapsLockOn) {
          this.setState({ isCapsLockOn: true });
          notification.open({
            message: 'CapsLock is on',
            duration: 0,
            description: 'Please notice that CapsLock is on, and letters will be uppercase',
          });
        }
      } else {
        this.setState({ isCapsLockOn: false });
        notification.destroy();
      }
    }

    handleChangePass = (event) => {
      this.setState({ password: event.target.value });
    }

    handleSubmit = (e) => {
      e.preventDefault();

      const { login, form } = this.props;

      form.validateFields((err, values) => {
        if (!err) {
          const user = {
            ...this.state,
          };

          const basicAuth = `Basic ${btoa(`${user.email}:${user.password}`)}`;

          loginApi(basicAuth).then((userInfo) => {
            localStorage.setItem('userInfo', JSON.stringify({ ...userInfo, token: basicAuth }));
            login({ ...userInfo, email: user.email });
            history.push('/wordgroups');
          }).catch((error) => {
            message.error(ErroLoginPopUp);
          });
        }
      });
    }

    checkCyrilLetters = (rule, value, callback) => {
      const cyrillicPattern = /[\u0400-\u04FF]/;
      if (cyrillicPattern.test(value)) {
        callback(latinLettersOnly);
      }
      callback();
    }

    render() {
      const { form } = this.props;
      return (
        <Form onSubmit={this.handleSubmit} className="login-form">
          <h4 className="login-form__title">
            { LoginText }
          </h4>
          <FormItem>
            {form.getFieldDecorator('userName', {
              rules: [{ required: true, message: ErrorEmailInput }, {
                validator: this.checkCyrilLetters,
              }],
            })(
              <Input prefix={<Icon type="user" />} placeholder={PlaceholderEmail} onChange={this.handleChangeEmail} />,
            )}
          </FormItem>
          <FormItem>
            {form.getFieldDecorator('password', {
              rules: [{ required: true, message: ErrorPasswordInput }, {
                validator: this.checkCyrilLetters,
              }],
            })(
              <Input prefix={<Icon type="lock" />} type="password" placeholder={PlaceholderPassword} onChange={this.handleChangePass} onKeyDown={this.checkCapsLock} />,
            )}
          </FormItem>
          <FormItem>
            <Button type="primary" htmlType="submit" className="login-form-button">
              { LoginTextBnt }
            </Button>
            {Or}
            <Link to="/registration">
              { RegisterNowText }
            </Link>
          </FormItem>
        </Form>
      );
    }
}

const WrappedNormalLoginForm = Form.create()(NormalLoginForm);

const mapDispatchToProps = dispatch => ({
  login: (name) => {
    dispatch(login(name));
  },
});

const mapStateToProps = state => ({
  userInfo: state.userInfo,
});

export default connect(mapStateToProps, mapDispatchToProps)(WrappedNormalLoginForm);
