import React from 'react';

import {
  Form, Icon, Input, Button, Table, Popconfirm, notification, AutoComplete, Spin,
} from 'antd';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { history } from '../../Base/routers/AppRouter';
import {
  loadWordsData, addWord, deleteWord, clearWordsState,
} from '../actions/wordsActions';
import {
  errServerConnection,
} from '../../WordGroups/constans/constants';
import { searchWords } from '../utils/search';
import { EngWordValidErr, RusWordValidErr, existingWordErr } from '../constants/constants';
import { mainUrl } from '../../Base/api/auth/constants';

const FormItem = Form.Item;

function hasErrors(fieldsError) {
  return Object.keys(fieldsError).some(field => fieldsError[field]);
}

export class WordsTable extends React.Component {
  constructor(props) {
    super(props);
    this.columns = [
      {
        title: <span className="words-col-names">
          <span className="col-lang">
            English
            {' '}
          </span>
          <span>
            Words
          </span>
               </span>,
        dataIndex: 'enWord',
        className: 'engWord-col',
        filterDropdown: ({
          setSelectedKeys, selectedKeys, confirm, clearFilters,
        }) => (
          <div className="custom-filter-dropdown">
            <Input
              ref={ele => this.searchInput = ele}
              placeholder="Search English word"
              value={selectedKeys[0]}
              onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
              onPressEnter={this.handleSearch(selectedKeys, confirm)}
            />
            <Button id="search input" type="primary" onClick={this.handleSearch(selectedKeys, confirm)}>
              Search
            </Button>
            <Button onClick={this.handleResetSearch(clearFilters)}>
              Reset
            </Button>
          </div>
        ),
        filterIcon: filtered => <Icon type="search" style={{ color: filtered ? '#108ee9' : '#aaa' }} />,
        onFilter: (value, record) => record.enWord.toLowerCase().includes(value.toLowerCase()),
        onFilterDropdownVisibleChange: (visible) => {
          if (visible) {
            setTimeout(() => {
              this.searchInput.focus();
            });
          }
        },
        render: (text, record) => {
          const { searchText } = this.state;
          return searchText
            ? searchWords(text, searchText)
            : (
              <div>
                <span>
                  { text }
                </span>
              </div>

            );
        },
        defaultSortOrder: 'ascend',
        sorter: (a, b) => a.enWord.localeCompare(b.enWord),
      },
      {
        title:
  <span className="words-col-names">
    <span className="col-lang">


Russian
      {' '}
    </span>
    <span>


Words
    </span>
  </span>,
        dataIndex: 'ruWord',
        className: 'rus-name-col',
        filterDropdown: ({
          setSelectedKeys, selectedKeys, confirm, clearFilters,
        }) => (
          <div className="custom-filter-dropdown">
            <Input
              ref={ele => this.searchInput = ele}
              placeholder="Search Russian word"
              value={selectedKeys[0]}
              onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
              onPressEnter={this.handleSearch(selectedKeys, confirm)}
            />
            <Button id="search input" type="primary" onClick={this.handleSearch(selectedKeys, confirm)}>


Search
            </Button>
            <Button onClick={this.handleResetSearch(clearFilters)}>


Reset
            </Button>
          </div>
        ),
        filterIcon: filtered => <Icon type="search" style={{ color: filtered ? '#108ee9' : '#aaa' }} />,
        onFilter: (value, record) => record.ruWord.toLowerCase().includes(value.toLowerCase()),
        onFilterDropdownVisibleChange: (visible) => {
          if (visible) {
            setTimeout(() => {
              this.searchInput.focus();
            });
          }
        },
        render: (text, record) => {
          const { searchText } = this.state;
          return searchText ? searchWords(text, searchText)
            : (
              <div>
                <span>
                  { text }
                </span>
              </div>

            );
        },
        sorter: (a, b) => a.ruWord.localeCompare(b.ruWord),
      },
      {
        title: '',
        className: 'remove-word-col-name',
        render: (text, record) => (
          <div className="removeWordCol">
            <Popconfirm
              title="Sure to delete?"
              onConfirm={() => this.removeWord(record.id)}
            >
              <Icon className="remove-word-icon" type="close-circle" theme="filled" />
            </Popconfirm>
          </div>
        ),
      },
    ];
  }

    state = {
      loading: true,
      pagination: {},
      rusRelWords: [],
      enRelWords: [],
      currentWordGroupName: '',
    };

    // english autocomplete
    handleEngAutoComplete = (value) => {
      this.setState({
        enRelWords: [],
      });
      const lang = 'en';

      if (value) {
        const sendVal = value.toString().toLowerCase();

        const autoCompReq = async (token) => {
          const response = await axios({
            method: 'get',
            url: `${mainUrl}/api/words/suggestion/${lang}/${sendVal}`,
            data: {
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
        autoCompReq(user.token).then((res) => {
          const resEnWords = res;
          this.setState({
            enRelWords: !value ? [] : resEnWords,
          });
        }).catch((error) => {
          console.log(error);
        });
        this.setState({
          enRelWords: [],
        });
      }
    };

    // russian autocomplete
    handleRusAutoComplete = (value) => {
      this.setState({
        rusRelWords: [],
      });
      const lang = 'ru';
      if (value) {
        const sendVal = value.toString().toLowerCase();
        const autoCompReq = async (token) => {
          const response = await axios({
            method: 'get',
            url: `${mainUrl}/api/words/suggestion/${lang}/${sendVal}`,
            data: {
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
        autoCompReq(user.token).then((res) => {
          const resRuWords = res;
          this.setState({
            rusRelWords: !value ? [] : resRuWords,
          });
        }).catch((error) => {
          console.log(error);
        });
        this.setState({
          rusRelWords: [],
        });
      }
    };


    clearWordsState() {
      this.setState({
        enRelWords: [],
        rusRelWords: [],
      });
    }


    // adding new word to group
    handleAddWord = (e) => {
      e.preventDefault();
      const wordGroupsId = this.props.match.params.id;
      this.props.form.validateFields((err, values) => {
        const newWord = { ...values };
        const englishWord = newWord.enWord.toLowerCase();
        const russianWord = newWord.ruWord.toLowerCase();
        const addWordReq = async (token) => {
          const response = await axios({
            method: 'put',
            url: `${mainUrl}/home/wordgroups/${wordGroupsId}/words`,
            data: {
              enWord: englishWord,
              ruWord: russianWord,
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
        addWordReq(user.token).then((res) => {
          const resWord = res;
          const newAddedWord = {
            id: resWord.id,
            enWord: resWord.enWord,
            ruWord: resWord.ruWord,
            groupId: wordGroupsId,
          };
          this.props.addWord(newAddedWord);
          this.handleReset();
          this.props.form.validateFields();
          this.clearWordsState();
        }).catch((error) => {
          if (error.response.status === 400) {
            notification.open({
              type: 'error',
              message: existingWordErr,
            });
          }
          if (error.response.status !== 400) {
            notification.open({
              type: 'error',
              message: errServerConnection,
            });
          }
        });
      });
    };

    // reset fields after submitting new word
    handleReset = () => {
      this.props.form.resetFields();
    };

    // delete word from word group
    removeWord = (id) => {
      const wordGroupsId = this.props.match.params.id;
      const row = document.querySelector(`[data-row-key="${id}"]`).classList.add('remove-row');

      setTimeout(() => {
        const user = JSON.parse(localStorage.getItem('userInfo'));
        axios(
          {
            method: 'delete',
            url: `${mainUrl}/home/wordgroups/${wordGroupsId}/words/${id}`,
            headers:
                      {
                        'Content-Type': 'application/json',
                        Authorization: user.token,
                      },
            data: {
            },
          },
        ).then((response) => {
          this.props.deleteWord(id);
        })
          .catch((error) => {
            notification.open({
              type: 'error',
              message: errServerConnection,
            });
          });
      }, 200);
    };

    // searching words

    handleSearch = (selectedKeys, confirm) => () => {
      confirm();
      this.setState({ searchText: selectedKeys[0] });
    };

    handleResetSearch = clearFilters => () => {
      clearFilters();
      this.setState({ searchText: '' });
    };

    // loading words for this wordgroup from server
    loadWords = () => {
      const wordGroupsId = this.props.match.params.id;
      const wordsApi = async (token) => {
        const response = await axios({
          method: 'get',
          url: `${mainUrl}/home/wordgroups/${wordGroupsId}/words`,
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
      wordsApi(user.token).then((data) => {
        const dataNew = data;
        const pagination = { ...this.state.pagination };
        this.setState({
          loading: false,
          pagination,
        });
        this.props.loadWordsData(dataNew);
      }).catch((error) => {
        if (error.response.status === 400) {
          history.push('/wordgroups');
        }
        if (error.response.status > 400) {
          notification.open({
            type: 'error',
            message: errServerConnection,
          });
        }
        this.setState({
          loading: false,
        });
      });
    };

    handleTableChange = (pagination, filters, sorter) => {
      const pager = { ...this.state.pagination };
      pager.current = pagination.current;
      this.setState({
        pagination: pager,
      });
      this.loadWords({
        results: pagination.pageSize,
        page: pagination.current,
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...filters,
      });
    };

    getWordGroupName = () => {
      const wordGroupId = Number(this.props.match.params.id);
      const getWordGrName = async (token) => {
        const response = await axios({
          method: 'get',
          url: `${mainUrl}/home/wordgroups/${wordGroupId}`,
          data: {
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
      getWordGrName(user.token).then((res, error) => {
        const resName = res.name;
        this.setState({
          currentWordGroupName: resName,
        });
        if (error) {
          console.log(error);
        }
      });
    };

    componentDidMount() {
      this.props.form.validateFields();
      const { clearWordsState } = this.props;
      clearWordsState();
      this.loadWords();
      this.getWordGroupName();
    }

    render() {
      const {
        getFieldDecorator, getFieldsError, getFieldError, isFieldTouched,
      } = this.props.form;
      const { enRelWords } = this.state;
      const { rusRelWords } = this.state;
      const { words } = this.props;
      const WName = this.state.currentWordGroupName;
      const columns = this.columns.map((col) => {
        if (!col.editable) {
          return col;
        }
        return {
          ...col,
          onCell: record => ({
            record,
            dataIndex: col.dataIndex,
            title: col.title,
          }),
        };
      });

      const enWordError = isFieldTouched('enWord') && getFieldError('enWord');
      const ruWordError = isFieldTouched('ruWord') && getFieldError('ruWord');
      return (
        <div className="words-table">
          <Link to="/wordgroups">
            <Button
              type="primary"
              htmlType="submit"
              className="goBackBtn"
            >
              <Icon className="goBack-arr" type="arrow-left" theme="outlined" />
            </Button>
          </Link>
          <Spin className="wordstable-spin" spinning={this.state.loading}>
            <p className="word-gr-name">
              {WName}
            </p>
            <Form className="words-table-form" layout="inline" onSubmit={this.handleAddWord}>
              <div className="form-inputs-container">
                <FormItem
                  className="eng-wrap-input"
                  validateStatus={enWordError ? 'error' : ''}
                  help={enWordError || ''}
                >
                  {getFieldDecorator('enWord', {
                    rules: [{
                      required: true,
                      whitespace: true,
                      pattern: '^[A-Za-z -]+$',
                      message: EngWordValidErr,
                      min: 1,
                      max: 36,
                    }],
                  })(
                    <AutoComplete
                      id="eng-ac"
                      className="eng-com"
                      dataSource={enRelWords}
                      onSearch={this.handleEngAutoComplete}
                    >
                      <Input
                        className="wordInput engWordInput"
                        prefix={<Icon type="search" theme="outlined" />}
                        placeholder="English Word"
                      />
                    </AutoComplete>,
                  )}
                </FormItem>
                <FormItem
                  validateStatus={ruWordError ? 'error' : ''}
                  help={ruWordError || ''}
                >
                  {getFieldDecorator('ruWord', {
                    rules: [{
                      required: true,
                      whitespace: true,
                      pattern: '^[А-Яа-яЁё -]+$',
                      message: RusWordValidErr,
                      min: 1,
                      max: 36,
                    }],
                  })(
                    <AutoComplete
                      dataSource={rusRelWords}
                      onSearch={this.handleRusAutoComplete}
                      className="ru-com"
                    >
                      <Input
                        className="wordInput ruWordInput"
                        prefix={<Icon type="search" theme="outlined" />}
                        placeholder="Russian Word"
                      />
                    </AutoComplete>
                    ,
                  )}
                </FormItem>
                <FormItem>
                  <Button
                    type="primary"
                    htmlType="submit"
                    disabled={hasErrors(getFieldsError())}
                    className="addWordsBtn"
                  >
                    <Icon type="plus" theme="outlined" />


                    Add Word
                  </Button>
                </FormItem>
              </div>
              <Table
                className="wordsInGroups-table"
                columns={columns}
                rowClassName={() => 'words-editable-row'}
                rowKey={record => record.id}
                dataSource={words}
                bordered
                pagination={{ pageSize: 10 }}
                onChange={this.handleTableChange}
              />
            </Form>
          </Spin>
        </div>
      );
    }
}

const WrappedWordsTable = Form.create()(WordsTable);

const mapDispatchToProps = dispatch => ({
  loadWordsData: (dataNew) => {
    dispatch(loadWordsData(dataNew));
  },
  addWord: (newWord) => {
    dispatch(addWord(newWord));
  },
  deleteWord: (id) => {
    dispatch(deleteWord(id));
  },
  clearWordsState: () => {
    dispatch(clearWordsState());
  },
});

const mapStateToProps = state => ({
  words: state.words.dataSource,
});

export default connect(mapStateToProps, mapDispatchToProps)(WrappedWordsTable);
