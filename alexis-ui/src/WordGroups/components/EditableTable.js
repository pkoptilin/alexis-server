import React from 'react';
import 'antd/dist/antd.css';
import {
  Table, Input, Button, Popconfirm, Form, Icon, Divider, notification,
} from 'antd';
import axios from 'axios';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
// actions

import {
  loadData, addWordGroup, deleteWordGroup, toggleStatus, editWordGroup,
} from '../actions/wordGroups';
// constants
import {
  errWordGroupName, newWordGroupName, errServerConnection, existingGroupNameErr,
} from '../constans/constants';
import { mainUrl } from '../../Base/api/auth/constants';
import { wordGroupsApi } from '../../Base/api/wordGroups/wordGroupsApi';

const FormItem = Form.Item;
const EditableContext = React.createContext();
const EditableRow = ({ form, index, ...props }) => (
  <EditableContext.Provider value={form}>
    <tr {...props} />
  </EditableContext.Provider>
);
const EditableFormRow = Form.create()(EditableRow);
class EditableCell extends React.Component {
    handleOnKeyDown = (e) => {
      if (e.key === 'Escape') {
        this.props.cancel();
        e.preventDefault();
      }
    };

    render() {
      const {
        editing,
        dataIndex,
        title,
        inputType,
        record,
        index,
        ...restProps
      } = this.props;
      return (
        <EditableContext.Consumer>
          {(form) => {
            const { getFieldDecorator } = form;
            return (
              <td {...restProps}>
                {editing ? (
                  <FormItem style={{ margin: 0 }}>
                    {getFieldDecorator(dataIndex, {
                      rules: [{
                        required: true,
                        message: errWordGroupName,
                        whitespace: true,
                        pattern: '^[-_A-Za-z 0-9]+$',
                        min: 1,
                        max: 30,
                      }],
                      initialValue: record[dataIndex],
                    })(<Input
                      onPressEnter={() => this.props.save(form, record.id, record.activeState)}
                      onKeyDown={this.handleOnKeyDown}
                    />)}
                  </FormItem>

                ) : restProps.children}
              </td>

            );
          }}
        </EditableContext.Consumer>
      );
    }
}

export class EditableTable extends React.Component {
  constructor(props) {
    super(props);

    const statusIcons = {
      enabledIcon: <Icon type="smile" className="status-icon active-status-icon" />,
      disabledIcon: <Icon type="frown" className="status-icon disabled-status-icon" />,
    };
    this.columns = [
      {
        title: 'Status',
        dataIndex: 'activeState',
        className: 'wordsStatus',
        editable: false,
        render: (text, record) => (
          <div>
            {
                            record.activeState === true ? (
                              statusIcons.enabledIcon
                            ) : (
                              statusIcons.disabledIcon
                            )
                        }
          </div>
        ),
        filters: [{
          text: 'Show enabled',
          value: true,
        }, {
          text: 'Show disabled',
          value: false,
        }],
        onFilter: (value, record) => record.activeState.toString().indexOf(value) === 0,
      },
      {
        title: 'Word Group',
        dataIndex: 'name',
        editable: true,
        className: 'group-name-col',
        sorter: (a, b) => b.name.localeCompare(a.name),
        filterDropdown: ({
          setSelectedKeys, selectedKeys, confirm, clearFilters,
        }) => (
          <div className="custom-filter-dropdown">
            <Input
              ref={ele => this.searchInput = ele}
              placeholder="Search Word Group"
              value={selectedKeys[0]}
              onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
              onPressEnter={this.handleSearch(selectedKeys, confirm)}
            />
            <Button id="search input" type="primary" onClick={this.handleSearch(selectedKeys, confirm)}>


Search
            </Button>
            <Button onClick={this.handleReset(clearFilters)}>


Reset
            </Button>
          </div>
        ),
        filterIcon: filtered => <Icon type="search" style={{ color: filtered ? '#108ee9' : '#aaa' }} />,
        onFilter: (value, record) => record.name.toLowerCase().includes(value.toLowerCase()),
        onFilterDropdownVisibleChange: (visible) => {
          if (visible) {
            setTimeout(() => {
              this.searchInput.focus();
            });
          }
        },
        render: (text, record) => {
          const { searchText } = this.state;
          return searchText ? (
            <span>
              <Link
                to={`/wordgroups/${record.id}`}
                className="wordGroup-name"
              >

                {text.split(new RegExp(`(?<=${searchText})|(?=${searchText})`, 'i')).map((fragment, i) => (
                  fragment.toLowerCase() === searchText.toLowerCase()
                      ? <span key={i} className="highlight">{fragment}</span> : fragment // eslint-disable-line
                ))}
              </Link>
            </span>

          )
            : (
              <Link
                to={`/wordgroups/${record.id}`}
                className="wordGroup-name"
              >
                {text}
              </Link>

            );
        },
      },
      {
        title: 'Actions',
        className: 'actions-col-name',
        render: (text, record) => {
          const editable = this.isEditing(record);
          return (
            <div className="actionsCol">
              <span className="changeStatus">
                {
                    record.activeState === true ? (
                      <span>
                        <Popconfirm title="Sure to deactivate?" onConfirm={() => this.toggleGroupStatus(record.id, record.name)}>
                          <a href="javascript:;">


                          Deactivate
                          </a>
                        </Popconfirm>
                        <Divider className="vertical-divider" type="vertical" />
                      </span>
                    ) : (
                      <span>
                        <a onClick={() => this.toggleGroupStatus(record.id, record.name)}>


                          Activate
                        </a>
                        <Divider className="vertical-divider" type="vertical" />
                      </span>
                    )
                }
              </span>
              <span>
                <Popconfirm id="delete-confirm" title="Sure to delete?" onConfirm={() => this.handleDelete(record.id)}>
                  <a id="delete-btn" href="javascript:;">
                    {' '}


Delete
                    {' '}
                  </a>
                </Popconfirm>
                <Divider className="vertical-divider" type="vertical" />
              </span>
              {editable ? (
                <span>
                  <EditableContext.Consumer>
                    {form => (
                      <span>
                        <a
                          href="javascript:;"
                          className="save-btn"
                          onClick={() => this.save(form, record.id, record.activeState)}
                          style={{ marginRight: 8 }}
                        >


                          Save
                        </a>
                        <Divider className="vertical-divider" type="vertical" />
                      </span>
                    )}
                  </EditableContext.Consumer>
                  <Popconfirm
                    title="Sure to cancel?"
                    onConfirm={() => this.cancel(record.id)}
                  >
                    <span>
                      <a>


Cancel
                      </a>
                    </span>
                  </Popconfirm>
                </span>
              ) : (
                <span>
                  {' '}
                  <a className="edit-btn" href="javascript:;" onClick={() => this.edit(record.id)}>


Edit
                  </a>
                </span>
              )}
            </div>
          );
        },
      },
    ];
  }

    state = {
      stateKey: '',
      count: 0,
      pagination: {},
      loading: true,
    };

    // editing word groups
    isEditing = record => record.id === this.state.editingKey;

    edit(id) {
      this.setState({ editingKey: id });
    }

    save = (form, id, activeState) => {
      form.validateFields((error, row) => {
        const newRow = {
          name: row.name.toLowerCase(),
        };
        if (error) {
          return;
        }
        const newData = [...this.props.dataSource];
        const index = newData.findIndex(item => id === item.id);
        if (index > -1) {
          const item = newData[index];
          const sendItem = {
            id: item.id,
            name: item.name.toLowerCase(),
            activeState: item.activeState,
            userId: item.userId,
          };
          newData.splice(index, 1, {
            ...sendItem,
            ...newRow,
          });
          this.setState({ editingKey: '' });

          // making request
          const saveGroupName = async (token) => {
            const response = await axios({
              method: 'post',
              url: `${mainUrl}/home/wordgroups/`,
              data: {
                id,
                name: row.name.toLowerCase(),
                activeState,
              },
              headers: {
                'Content-Type': 'application/json',
                Authorization: token,
              },
            });
            return response.status;
          };
          const user = JSON.parse(localStorage.getItem('userInfo'));
          saveGroupName(user.token).then((res) => {
            if (res) {
              this.props.editWordGroup(newData);
            }
          }).catch((error) => {
            notification.open({
              type: 'error',
              message: existingGroupNameErr,
            });
          });
        } else {
          newData.push(newRow);
          this.setState({ editingKey: '' });
          this.props.editWordGroup(newData);
        }
      });
    };

    cancel = () => {
      this.setState({ editingKey: '' });
    };

    // searching wordgroups

    handleSearch = (selectedKeys, confirm) => () => {
      confirm();
      this.setState({ searchText: selectedKeys[0] });
    };

    handleReset = clearFilters => () => {
      clearFilters();
      this.setState({ searchText: '' });
    };

    // deleting wordgroups

    handleDelete = (id) => {
      document.querySelector(`[data-row-key="${id}"]`).classList.add('remove-row');
      setTimeout(() => {
        const user = JSON.parse(localStorage.getItem('userInfo'));
        axios(
          {
            method: 'delete',
            url: `${mainUrl}/home/wordgroups/${id}/`,
            headers:
                      {
                        'Content-Type': 'application/json',
                        Authorization: user.token,
                      },
            data: {
            },
          },
        ).then((response) => {
          this.props.deleteWordGroup(id);
        })
          .catch((error) => {
            notification.open({
              type: 'error',
              message: errServerConnection,
            });
          });
      }, 200);
    };

    // adding new row

    handleAdd = () => {
      // --adding number to new group due to the count
      const obj = [...this.props.dataSource];
      const namesArr = [];
      const toArr = () => {
        for (const value of obj.values()) {
          namesArr.push(value.name.toLowerCase());
        }
      };
      toArr();
      const newGroupsArr = namesArr.filter(name => name.indexOf('new group') + 1);
      const newWGArr = newGroupsArr.filter(name => name.length > 9);
      const lastNum = newWGArr.map(
        (name) => {
          const str = name.split(' ');
          return str[str.length - 1];
        },
      );
      const maxArrNum = (Math.max(...lastNum));
      const newCount = maxArrNum + 1;
      const nameGroup = `new group ${newCount}`;

      const naming = () => {
        if (newGroupsArr.length !== 0) {
          return nameGroup;
        }
        return newWordGroupName;
      };

        //---

        // adding new group to the server
      const addGroupReq = async (token) => {
        const response = await axios({
          method: 'put',
          url: `${mainUrl}/home/wordgroups/`,
          data: {
            name: naming().toLowerCase(),
            activeState: true,
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
      addGroupReq(user.token).then((res) => {
        const newWordGroup = res;
        this.props.addWordGroup(newWordGroup);
      }).catch((error) => {
        notification.open({
          type: 'error',
          message: errServerConnection,
        });
      });
    };


    // changing the status of word group
    toggleGroupStatus(id, name) {
      this.setState({ stateKey: id });

      const newData = [...this.props.dataSource];
      const index = newData.findIndex(item => id === item.id);
      const item = newData[index];
      item.activeState = !item.activeState;
      newData.splice(index, 1, {
        ...item,
      });
      this.setState({ stateKey: '' });

      const user = JSON.parse(localStorage.getItem('userInfo'));
      axios(
        {
          method: 'post',
          url: `${mainUrl}/home/wordgroups/`,
          headers:
                    {
                      'Content-Type': 'application/json',
                      Authorization: user.token,
                    },
          data: {
            id,
            name,
            activeState: item.activeState,
          },
        },
      ).then((response) => {
        this.props.toggleStatus(newData);
      })
        .catch((error) => {
          notification.open({
            type: 'error',
            message: errServerConnection,
          });
        });
    }

    handleTableChange = (pagination, filters, sorter) => {
      const pager = { ...this.state.pagination };
      pager.current = pagination.current;
      this.setState({
        pagination: pager,
      });
    }

    // load data from server

    loadWordGroups = () => {
      const user = JSON.parse(localStorage.getItem('userInfo'));
      wordGroupsApi(user.token).then((data) => {
        const dataNew = data;
        const pagination = { ...this.state.pagination };
        this.setState({
          loading: false,
          pagination,
        });
        this.props.loadData(dataNew);
      }).catch((error) => {
        notification.open({
          type: 'error',
          message: errServerConnection,
        });
        this.setState({
          loading: false,
        });
      });
    };


    componentDidMount() {
      this.loadWordGroups();
    }

    render() {
      const { dataSource } = this.props;
      const components = {
        body: {
          row: EditableFormRow,
          cell: EditableCell,
        },
      };

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
            save: this.save,
            escFunction: this.escFunction,
            editing: this.isEditing(record),
            cancel: this.cancel,
          }),
        };
      });
      return (
        <div className="wordGroups-table">
          <Button
            className="addGroupBtn"
            id="addGroup-Btn"
            onClick={() => this.handleAdd()}
            type="primary"
          >


                + Add new word group
          </Button>
          <Table
            className="WordGroupTable"
            components={components}
            columns={columns}
            rowKey={record => record.id}
            rowClassName={() => 'editable-row'}
            dataSource={dataSource}
            pagination={{ pageSize: 10 }}
            loading={this.state.loading}
            onChange={this.handleTableChange}
          />
        </div>
      );
    }
}

const mapDispatchToProps = dispatch => ({
  loadData: (dataNew) => {
    dispatch(loadData(dataNew));
  },
  addWordGroup: (newWordGroup) => {
    dispatch(addWordGroup(newWordGroup));
  },
  deleteWordGroup: (id) => {
    dispatch(deleteWordGroup(id));
  },
  toggleStatus: (newData) => {
    dispatch(toggleStatus(newData));
  },
  editWordGroup: (newData) => {
    dispatch(editWordGroup(newData));
  },
});

const mapStateToProps = state => ({
  dataSource: state.wordGroups.dataSource,
});

export default connect(mapStateToProps, mapDispatchToProps)(EditableTable);
