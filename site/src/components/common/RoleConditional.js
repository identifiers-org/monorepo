import React from 'react';
import { connect } from 'react-redux';


const RoleConditional = ({ auth, children, requiredRoles, fallbackComponent }) => {
  console.log('auth', auth);

  if (auth.authenticated) {
    const rolesMet = requiredRoles.map(role => auth.keycloak.hasResourceRole(role));

    console.log('rolesMet', rolesMet);

    if (!rolesMet.includes(false)) {
      const child = React.Children.only(children);

      return child;
    }
  }

  // Roles not met / unauthenticated.
  return fallbackComponent ? fallbackComponent : null;
}


//
// Mapping functions.
//
const mapStateToProps = (state) => ({ auth: state.auth });


export default connect(mapStateToProps)(RoleConditional);
