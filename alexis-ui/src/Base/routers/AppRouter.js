import React from 'react';
import {
  Router, Switch, Route,
} from 'react-router-dom';
import createHistory from 'history/createBrowserHistory';


// components
import Private from './PrivateRoute';
// pages
import Wordgroups from '../../WordGroups/components/index';
import WordsPage from '../../Words/components/WordsPage';
import SetupPage from '../../Setup/components/SetupPage';
import Profile from '../../Profile/components/Profile';
import StatisticPage from '../../Statistics/components/StaticPage';
import LoginPage from '../../Login/components/LoginPage';
import RegistrationPage from '../../Login/components/RegistrationPage';
import NotFoundPage from '../../NotFoundPage/components/NotFoundPage';

export const history = createHistory();

//  ROUTERS
const AppRouter = () => (
  <Router history={history}>
    <div className="wrapper">
      <Switch>
        <Route path="/" component={LoginPage} exact />
        <Route path="/registration" component={RegistrationPage} exact />
        <Private path="/wordgroups" component={Wordgroups} exact />
        <Private path="/wordgroups/:id" component={WordsPage} exact />
        <Private path="/setup" component={SetupPage} exact />
        <Private path="/statistics" component={StatisticPage} exact />
        <Private path="/profile" component={Profile} exact />
        <Route component={NotFoundPage} />
      </Switch>
    </div>
  </Router>
);

export default AppRouter;
