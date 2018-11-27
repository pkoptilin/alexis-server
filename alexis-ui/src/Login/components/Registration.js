import React from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import {
  Form, Input, Button, message, notification,
} from 'antd';
import { history } from '../../Base/routers/AppRouter';
import { login } from '../actions/auth';
import { registrationApi } from '../../Base/api/auth/authApi';

import {
  RegistrationText, NicknameText, RegisterBtnText, BackToLoginText, SuccsedRegistrationPopUp, ErroUserEmailExist, ErrorInputName, WrongPasswordTwo, EmailNotValid, ErrorEmailInput, ErrorPasswordInput, ErrorConfirmPassword, ErrorPasswordlength, ErrorNiknamelength, latinLettersOnly,
} from '../constants/constanst';

const FormItem = Form.Item;

class RegistrationForm extends React.Component {
    state = {
      name: '',
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

    handleChangeName = (event) => {
      this.setState({ name: event.target.value });
    }

    handleSubmit = (e) => {
      e.preventDefault();

      const { form } = this.props;

      const user = {
        ...this.state,
      };

      form.validateFieldsAndScroll((err) => {
        if (!err) {
          registrationApi(user).then((res) => {
            if (res) {
              message.success(SuccsedRegistrationPopUp);
              history.push('/');
            }
          }).catch(() => {
            message.error(ErroUserEmailExist);
          });
        }
      });
    };

    compareToFirstPassword = (rule, value, callback) => {
      const { form } = this.props;
      if (value && value !== form.getFieldValue('password')) {
        callback(WrongPasswordTwo);
      } else {
        callback();
      }
    }

    checkLatinLettersOnly = (rule, value, callback) => {
      const checkLatinLetters = /[^a-zA-Z0-9\s]/;
      if (checkLatinLetters.test(value)) {
        callback(latinLettersOnly);
      }
      callback();
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
        <Form onSubmit={this.handleSubmit}>

          <h4 className="login-form__title">
            {RegistrationText}
          </h4>
          <FormItem
            label={(
              <span>
                {NicknameText}
              </span>
                    )}
          >
            {form.getFieldDecorator('nickname', {
              rules: [{ required: true, message: ErrorInputName, whitespace: true }, {
                max: 25,
                message: ErrorNiknamelength,
              }, {
                validator: this.checkLatinLettersOnly,
              }],
            })(
              <Input onChange={this.handleChangeName} />,
            )}
          </FormItem>
          <FormItem
            label="E-mail"
          >
            {form.getFieldDecorator('email', {
              rules: [{
                type: 'email', message: EmailNotValid,
              }, {
                required: true, message: ErrorEmailInput,
              }, {
                validator: this.checkCyrilLetters,
              }],
            })(
              <Input name="email" onChange={this.handleChangeEmail} />,
            )}
          </FormItem>
          <FormItem
            label="Password"
          >
            {form.getFieldDecorator('password', {
              rules: [{
                required: true, message: ErrorPasswordInput,
              }, {
                min: 6,
                message: ErrorPasswordlength,
              }, {
                validator: this.validateToNextPassword,
              }, {
                validator: this.checkCyrilLetters,
              }],
            })(
              <Input type="password" name="password" onChange={this.handleChangePass} onKeyDown={this.checkCapsLock} />,
            )}
          </FormItem>
          <FormItem
            label="Confirm Password"
          >
            {form.getFieldDecorator('confirm', {
              rules: [{
                required: true, message: ErrorConfirmPassword,
              }, {
                min: 6,
                message: ErrorPasswordlength,
              }, {
                validator: this.compareToFirstPassword,
              }],
            })(
              <Input type="password" onKeyDown={this.checkCapsLock} />,
            )}
          </FormItem>
          <FormItem>
            <div className="space-between">
              <Link to="/">
                { BackToLoginText }
              </Link>
              <Button type="primary" htmlType="submit">
                {RegisterBtnText}
              </Button>
            </div>
          </FormItem>
        </Form>
      );
    }
}
const mapStateToProps = state => ({
  userInfo: state.userInfo,
});
const mapDispatchToProps = dispatch => ({
  login: (name) => {
    dispatch(login(name));
  },
});

const WrappedRegistrationForm = Form.create()(RegistrationForm);

export default connect(mapStateToProps, mapDispatchToProps)(WrappedRegistrationForm);
