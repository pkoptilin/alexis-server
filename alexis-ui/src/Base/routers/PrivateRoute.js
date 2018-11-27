import React from 'react';
import { connect } from 'react-redux';
import { Route, Redirect } from 'react-router-dom';
import Header from '../../Header/components/header';
import Footer from '../../Footer/components/Footer';

export const PrivateRoute = ({ component: Component, ...rest }) => {
  const isAuthenticated = !!JSON.parse(localStorage.getItem('userInfo'));
  return (
    <Route
      {...rest}
      component={props => (
        isAuthenticated ? (
          <div className="main-wrapper">
            <Header />
            <main className="mainBlock">
              <Component {...props} />
            </main>
            <Footer />
          </div>
        ) : (
          <Redirect to="/" />
        )
      )}
    />
  );
};

export default connect()(PrivateRoute);
