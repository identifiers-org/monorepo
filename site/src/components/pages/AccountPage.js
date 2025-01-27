import React from 'react';
import { connect } from 'react-redux';

// Components.
import PageTitle from '../common/PageTitle';


const AccountPage = ({ auth }) => {
  if (!auth.keycloak.tokenParsed) {
    return ('');
  }

  return (
    <div>
      <PageTitle
        icon="icon-user"
        title="Account details"
      />

      <div className="row">
        <div className="col">
          <h4>Name</h4><p>{auth.keycloak.tokenParsed.name}</p>
        </div>
      </div>
    </div>
  );
}

//
// Redux mappings.
//
const mapStateToProps = (state) => ({
  auth: state.auth
});


export default connect(mapStateToProps)(AccountPage);
