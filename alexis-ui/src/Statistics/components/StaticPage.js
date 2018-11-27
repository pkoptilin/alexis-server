import React from 'react';
import {
  Row, Col, Select, Table, notification, Spin
} from 'antd';
import { connect } from 'react-redux';
import { Pie } from 'react-chartjs-2';
import uuidv4 from 'uuid/v4';
import { configApi } from '../../Base/api/setup/setupApi';
import { pageTitle,tableWordTitle, wordSuccessTitle, wordInprogressTitle, learnedForUrl, inprogressForUrl, backgroundColorFalse, backgroundColorSuccess, wordtitle, successTitle, titleFail, labelSucces, labelInprocess, noGroupsTest } from '../constants/constants';
import { wordGroupsApi } from '../../Base/api/wordGroups/wordGroupsApi';
import groupStatistApi from '../../Base/api/statisticApi/groupStatisticApi';
import wordsStatisticApi from '../../Base/api/statisticApi/wordsStatisticApi';
import { loadData } from '../../WordGroups/actions/wordGroups';
import { reDrawPie, changeToInprogress, changeToLearned, selectGroup, loadWords } from '../actions/pieActions';

class StatisticPage extends React.Component {
  state = {
    pagination: {},
    loading: false,
    tableLoding: true,
    selectLoading: true
  };

  componentDidMount = async () => {
    const user = JSON.parse(localStorage.getItem('userInfo'));
    const defaultSetup = await configApi(user.token);
    const { loadData, selectGroup, pie } = this.props;
    const groupList = await wordGroupsApi(user.token);
    const defauldName = groupList.find(item => item.id === defaultSetup.defaultGroupId);
    if (defauldName) {
      loadData(groupList);
      selectGroup({ defaultSelectValue: defauldName.name, activeGroupId: defauldName.id });
      this.statisctiAmount(user.token, defaultSetup.defaultGroupId);
      this.handlewordsTable(user.token, defaultSetup.defaultGroupId, pie.acitveFilter);
    } else if (groupList[0]) {
      loadData(groupList);
      selectGroup({ defaultSelectValue: groupList[0].name, activeGroupId: groupList[0].id });
      this.statisctiAmount(user.token, groupList[0].id);
      this.handlewordsTable(user.token, groupList[0].id, pie.acitveFilter);
    } else {
      this.setState(() => ({
        selectLoading: false,
        tableLoding: false,
      }));
    }
  }
  componentWillUnmount = () => {
    const {loadWords, reDrawPie, selectGroup} = this.props;
    loadWords({});
    reDrawPie({});
    selectGroup({});
  }

  handleTableChange = (pagination) => {
    const user = JSON.parse(localStorage.getItem('userInfo'));
    const pager = { ...this.state.pagination };
    const { pie } = this.props
    pager.current = pagination.current;
    this.setState({
      pagination: pager,
    });
    this.handlewordsTable(user.token, pie.activeGroupId, pie.acitveFilter, pagination.current);
  }

  handleChange = (value) => {
    const { dataSource, selectGroup, pie } = this.props;
    const pagination = { ...this.state.pagination };
    pagination.current = 1;
    const user = JSON.parse(localStorage.getItem('userInfo'));
    const getIndex = dataSource.findIndex(elemet => elemet.name === value);
    const idGroup = dataSource[getIndex].id;
    this.statisctiAmount(user.token, idGroup);
    this.handlewordsTable(user.token, idGroup, pie.acitveFilter);
    selectGroup({ defaultSelectValue: value, activeGroupId: idGroup });
    this.setState(() => ({
      pagination,
    }));
  }

  statisctiAmount = async (token, idGroup) => {
    const { reDrawPie } = this.props;
    const result = await groupStatistApi(token, idGroup);
    const { inprogress, learned } = result;
    reDrawPie({ inprogress, learned });
  };

  handlewordsTable = async (token, groupId, statusWords, page) => {
    this.setState(() => ({
      tableLoding: true,
    }));
    const { loadWords, pie } = this.props
    const pagination = { ...this.state.pagination };
    const result = await wordsStatisticApi(token, groupId, statusWords, page);
    const wordsWithKey = result.words.map((word) => ({...word, key: uuidv4()}))
    pagination.total = result.numberOfPages * 10,
    loadWords({ wordsTable: wordsWithKey })
    this.setState(() => ({
      pagination,
      tableLoding: false,
      selectLoading: false,
    }));
  };


  changeColor = (dataset) => {
    const user = JSON.parse(localStorage.getItem('userInfo'));
    const { changeToInprogress, changeToLearned, pie } = this.props;
    if (!dataset[0]._index) {
      changeToLearned();
      this.handlewordsTable(user.token, pie.activeGroupId, learnedForUrl);
    } else {
      changeToInprogress();
      this.handlewordsTable(user.token, pie.activeGroupId, inprogressForUrl);

    }
  }

  render() {
    const { dataSource, pie } = this.props;
    const data = {
      labels: [
        labelSucces,
        labelInprocess,
      ],
      datasets: [{
        data: [pie.learned, pie.inprogress],
        backgroundColor: [
          pie.ColorSuccess,
          pie.ColorFalse,
        ],
        hoverBackgroundColor: [
          backgroundColorSuccess,
          backgroundColorFalse,
        ],
      }],
    };

    const Option = Select.Option;
    const columns = [{
      title: tableWordTitle,
      dataIndex: wordtitle,
      key: wordtitle,
    }, {
      title: wordSuccessTitle,
      dataIndex: successTitle,
      key: successTitle,
    }, {
      title: wordInprogressTitle,
      dataIndex: titleFail,
      key: titleFail,
    }];
    console.log(pie.defaultSelectValue)
    return (
      <div className="page static-page">
        <h1 className="page__title">
          {pageTitle}
        </h1>
        <div>
          <Row>
            <Col sm={{ span: 24 }} lg={{ span: 12}} >
            <div className="static-select"><Spin spinning={this.state.selectLoading}><Select
              showSearch
              value={pie.defaultSelectValue ? pie.defaultSelectValue: noGroupsTest}
              onChange={this.handleChange}
              placeholder="Select users"
            >
              {dataSource.map(group => (
                <Option value={group.name} key={group.id} className="f-letter-up-case">
                  {group.name}
                </Option>
              ))}
            </Select></Spin></div>
              
              <Pie data={data} getElementAtEvent={this.changeColor} />
            </Col>
            <Col sm={{ span: 24 }} lg={{ span: 12 }} >
              <h2 className="table-title">
                {pie.titleTable}
              </h2>
              <Table dataSource={pie.wordsTable} columns={columns} pagination={this.state.pagination} loading={this.state.tableLoding} 
                onChange={this.handleTableChange}
              />
            </Col>
          </Row>
        </div>
      </div>
    );
  }
}

const mapDispatchToProps = dispatch => ({
  loadData: (dataNew) => {
    dispatch(loadData(dataNew));
  },
  reDrawPie: (data) => {
    dispatch(reDrawPie(data));
  },
  changeToInprogress: () => {
    dispatch(changeToInprogress());
  },
  changeToLearned: () => {
    dispatch(changeToLearned());
  },
  selectGroup: (data) => {
    dispatch(selectGroup(data));
  },
  loadWords: (data) => {
    dispatch(loadWords(data));
  },
});

const mapStateToProps = state => ({
  dataSource: state.wordGroups.dataSource,
  pie: state.pie,
});

export default connect(mapStateToProps, mapDispatchToProps)(StatisticPage);
