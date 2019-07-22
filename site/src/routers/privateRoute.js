import React from 'react';
import { connect } from 'react-redux';
import { Route, Redirect } from 'react-router-dom';


const PrivateRoute = ({ auth, component, requiredRoles, ...rest }) => {
  // Authenticated route.
  if (auth.authenticated) {
    // Check if all roles required are present.
    const rolesMet = requiredRoles.map(role => auth.keycloak.hasResourceRole(role));

    if (!rolesMet.includes(false)) {
      return <Route {...rest} component={component} />;
    }
  }

  // Unauthenticated route: redirect to home.
  return <Route {...rest} component={props => <Redirect to={{pathname: '/'}} />} />;
}


//
// Mapping functions.
//
const mapStateToProps = (state) => ({ auth: state.auth });


export default connect(mapStateToProps)(PrivateRoute);
