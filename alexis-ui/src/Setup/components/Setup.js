import React from 'react';
import { connect } from 'react-redux';
import {
  Select, Button, Icon, notification, Spin, Popover,
} from 'antd';
import axios from 'axios';
import { mainUrl } from '../../Base/api/auth/constants';
import {
  loadActiveWordGroups, getSetupConfig, setApproach, setDefaultWGroup, setSuccessApproach,
} from '../actions/setupActions';
import {
  approaches, wGroupMessage, mainSetupText, selectClasses, selectOnSelectClass, defaultWordGroupContent, failApproachesContent, successApproachesContent,
} from '../constans/setup';
import { findObjectByKey } from '../utils/setupUtils';

import { configApi } from '../../Base/api/setup/setupApi';
import {
  errServerConnection,
} from '../../WordGroups/constans/constants';

const Option = Select.Option;

export class Setup extends React.Component {
  constructor(props) {
    super(props);
  }

    state = {
      approach: '',
      defaultWordGroup: '',
      successApproach: '',
      successApproachBtnState: true,
      approachBtnState: true,
      wordGroupBtnState: true,
      loading: true,
    };


    componentDidMount() {
      this.loadActiveWordGroups();
      this.getConfig();
    }

    getConfig = async () => {
      const user = JSON.parse(localStorage.getItem('userInfo'));
      try {
        const data = await configApi(user.token);
        const resConfig = data;
        this.setState({
          defaultWordGroup: resConfig.defaultGroupId,
          approach: resConfig.failApproach,
          successApproach: resConfig.successApproach,
          loading: false,
        });
        this.props.getSetupConfig(resConfig);
      } catch (error) {
        notification.open({
          type: 'error',
          message: errServerConnection,
        });
      }
    }

    setVal = (value) => {
      const val = Number(value);
      this.setState({
        approach: val,
        approachBtnState: false,
      });
    };

    setSuccessVal = (value) => {
      const val = Number(value);
      this.setState({
        successApproach: val,
        successApproachBtnState: false,
      });
    };

    setWordGroup = (value, wordGroupInfo) => {
      const wordGroupId = wordGroupInfo.props.wordGroupInfo;
      const wordGroupVal = wordGroupInfo.props.val;
      if (wordGroupVal === null) {
        this.setState({
          defaultWordGroup: wordGroupVal,
          wordGroupBtnState: false,
        });
      } else {
        this.setState({
          defaultWordGroup: wordGroupId,
          wordGroupBtnState: false,
        });
      }
    };

    loadActiveWordGroups = async () => {
      const activeWordGroupsApi = async (token) => {
        const response = await axios({
          method: 'get',
          url: `${mainUrl}/api/alexa/configuration/groups`,
          data: {},
          headers: {
            'Content-Type': 'application/json',
            Authorization: token,
          },
        });

        if (response.status <= 400) {
          return response.data;
        }
        throw new Error(response.status);
      };

      const user = JSON.parse(localStorage.getItem('userInfo'));
      try {
        const data = await activeWordGroupsApi(user.token);
        const resObj = data;
        const resData = [];
        const toArr = () => {
          for (const value of resObj.values()) {
            const wordGroupName = value.name;
            const wordGroupId = value.id;
            const newGroupObj = { wordGroupId, wordGroupName };
            resData.push(newGroupObj);
          }
        };
        toArr();
        this.props.loadActiveWordGroups(resData);
        this.setState({
          aW: resData,
        });
      } catch (error) {
        notification.open({
          type: 'error',
          message: errServerConnection,
        });
      }
    };


    sendConfig = async () => {
      const { defaultWordGroup, userFailApproaches, userSuccessApproach } = this.props;
      const sendConfigApi = async (token) => {
        const response = await axios({
          method: 'post',
          url: `${mainUrl}/api/alexa/configuration `,
          data: {
            failApproach: userFailApproaches,
            defaultGroupId: defaultWordGroup,
            successApproach: userSuccessApproach,
          },
          headers: {
            'Content-Type': 'application/json',
            Authorization: token,
          },
        });

        if (response.status <= 400) {
          return response.data;
        }
        throw new Error(response.status);
      };
      const user = JSON.parse(localStorage.getItem('userInfo'));
      try {
        const data = await sendConfigApi(user.token);
        return data;
      } catch (error) {
        notification.open({
          type: 'error',
          message: errServerConnection,
        });
      }
    }

    saveApproach = () => {
      const approach = this.state.approach;
      const resolveSaveApproach = () => new Promise((resolve, reject) => {
        resolve();
      });
      resolveSaveApproach()
        .then(() => {
          this.props.setApproach(approach);
        })
        .then(
          () => {
            this.sendConfig();
          },
        )
        .then(
          () => {
            this.setState({
              approachBtnState: true,
            });
          },
        )
        .catch(err => notification.open({
          type: 'error',
          message: errServerConnection,
        }));
    };

    saveSuccessApproach = () => {
      const successApproach = this.state.successApproach;
      const resolveSaveSuccessApproach = () => new Promise((resolve, reject) => {
        resolve();
      });
      resolveSaveSuccessApproach()
        .then(() => {
          this.props.setSuccessApproach(successApproach);
        })
        .then(
          () => {
            this.sendConfig();
          },
        )
        .then(
          () => {
            this.setState({
              successApproachBtnState: true,
            });
          },
        )
        .catch(err => notification.open({
          type: 'error',
          message: errServerConnection,
        }));
    }

    saveWordGroup = () => {
      const wGroup = this.state.defaultWordGroup;
      const resolveSaveWordGroup = () => new Promise((resolve, reject) => {
        resolve();
      });
      resolveSaveWordGroup()
        .then(() => {
          this.props.setDefaultWGroup(wGroup);
        })
        .then(
          () => {
            this.sendConfig();
          },
        )
        .then(
          () => {
            this.setState({
              wordGroupBtnState: true,
            });
          },
        )
        .catch(err => notification.open({
          type: 'error',
          message: errServerConnection,
        }));
    };


    render() {
      const { activeWordGroups } = this.props;
      const { defaultWordGroup } = this.props;
      const { userFailApproaches } = this.props;
      const { userSuccessApproach } = this.props;
      const defaultWordGroupName = findObjectByKey(activeWordGroups, 'wordGroupId', defaultWordGroup);
      return (
        <div className="select-block">
          <div className="select-block-text">
            <div className="setup-page-name">
              <Icon id="setup-text-icon" type="setting" theme="outlined" />
              <span id="setup-page-name-text">
SETUP
              </span>
            </div>
            <div>
              <span id="additional-text">
                {mainSetupText}
              </span>
            </div>
          </div>
          <div className="select-wrapper">
            <div className="select-block-item-wrap">
              <div className="select-block-item select-label">
                <span className="label-text">Fail Approaches: </span>
              </div>
              <Spin spinning={this.state.loading}>
                <Select
                  className={this.state.approachBtnState ? selectOnSelectClass : selectClasses}
                  placeholder={userFailApproaches}
                  onChange={this.setVal}
                >
                  {approaches.map(fnum => <Option key={fnum}>{fnum}</Option>)}
                </Select>
              </Spin>

              <Button
                id="save-approach"
                className="save-select-btn"
                type="primary"
                onClick={this.saveApproach}
                disabled={this.state.approachBtnState}
              >
                  Save
              </Button>
              <Popover content={failApproachesContent}>
                <Icon type="question-circle" className="setup-field-icon" />
              </Popover>
            </div>
            <div className="select-block-item-wrap">
              <div className="select-block-item select-label">
                <span className="label-text">Successful Approaches: </span>
              </div>
              <Spin
                spinning={this.state.loading}
              >
                <Select
                  className={this.state.successApproachBtnState ? selectOnSelectClass : selectClasses}
                  placeholder={userSuccessApproach}
                  onChange={this.setSuccessVal}
                >
                  {approaches.map(fnum => <Option key={fnum}>{fnum}</Option>)}
                </Select>
              </Spin>

              <Button
                id="success-approach-btn"
                className="save-select-btn"
                type="primary"
                onClick={this.saveSuccessApproach}
                disabled={this.state.successApproachBtnState}
              >
                Save
              </Button>
              <Popover content={successApproachesContent}>
                <Icon type="question-circle" className="setup-field-icon" />
              </Popover>
            </div>
            <div className="select-block-item-wrap">
              <div className="select-block-item select-label">
                <span className="label-text">
Default Word Group:
                  {' '}
                </span>
              </div>
              <Spin spinning={this.state.loading}>
                <Select
                  className={this.state.wordGroupBtnState ? selectOnSelectClass : selectClasses}
                  onChange={this.setWordGroup}
                  showSearch
                  placeholder={(defaultWordGroupName !== null ? defaultWordGroupName : wGroupMessage)}
                >
                  <Option val="null" key="default" id="defaultField">{wGroupMessage}</Option>
                  {activeWordGroups.map(d => <Option className="setup-options" wordGroupInfo={d.wordGroupId} val="notDefault" key={d.wordGroupName}>{d.wordGroupName}</Option>)}

                </Select>
              </Spin>
              <Button
                id="save-default-group"
                className="save-select-btn"
                type="primary"
                onClick={this.saveWordGroup}
                disabled={this.state.wordGroupBtnState}
              >
                  Save
              </Button>
              <Popover content={defaultWordGroupContent}>
                <Icon type="question-circle" className="setup-field-icon" />
              </Popover>
            </div>
          </div>
        </div>
      );
    }
}

const mapDispatchToProps = dispatch => ({
  loadActiveWordGroups: (resData) => {
    dispatch(loadActiveWordGroups(resData));
  },
  getSetupConfig: (resConfig) => {
    dispatch(getSetupConfig(resConfig));
  },
  setApproach: (approach) => {
    dispatch(setApproach(approach));
  },
  setSuccessApproach: (successApproach) => {
    dispatch(setSuccessApproach(successApproach));
  },
  setDefaultWGroup: (wGroup) => {
    dispatch(setDefaultWGroup(wGroup));
  },
});

const mapStateToProps = state => ({
  setup: state.setup,
  activeWordGroups: state.setup.activeWordGroups,
  defaultWordGroup: state.setup.defaultWordGroup,
  userFailApproaches: state.setup.userFailApproaches,
  userSuccessApproach: state.setup.userSuccessApproach,
});

export default connect(mapStateToProps, mapDispatchToProps)(Setup);
