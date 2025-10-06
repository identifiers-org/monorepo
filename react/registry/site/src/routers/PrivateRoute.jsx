import { connect } from 'react-redux';
import { doSignIn } from "../actions/Auth";
import { Navigate } from "react-router-dom";


const PrivateRoute = (props) => {
  const { auth, doSignIn, children } = props;
  const requiredRoles = props.requiredRoles || [];

  // Authenticated route.
  if (auth.authenticated) {
    // Check if all roles required are present.
    const rolesMet = requiredRoles.every(role => auth.keycloak.hasResourceRole(role));

    if (rolesMet) {
      return children;
    } else {
      return <Navigate to="/" />
    }
  } else {
    doSignIn({redirectUri: window.location.href});
    return <></>
  }
}


//
// Mapping functions.
//
const mapStateToProps = (state) => ({ auth: state.auth });
const mapDispatchToProps = (dispatch) => ({
  doSignIn: () => dispatch(doSignIn())
})
export default connect(mapStateToProps,mapDispatchToProps)(PrivateRoute);
