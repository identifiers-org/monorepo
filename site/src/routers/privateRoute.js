import React from 'react';
import { connect } from 'react-redux';
import { Route, Redirect } from 'react-router-dom';


const PrivateRoute = ({ auth, component, ...rest }) => {
  // Authenticated route.
  if (auth.authenticated) {
    return <Route {...rest} component={component} />;
  } else {
    // Unauthenticated route: redirect to home.
    return <Route {...rest} component={props => <Redirect to={{pathname: '/'}} />} />;
  }
}


//
// Mapping functions.
//
const mapStateToProps = (state) => ({ auth: state.auth });


export default connect(mapStateToProps)(PrivateRoute);
