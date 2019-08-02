import React from 'react';
import { connect } from 'react-redux';

// Components.
import PageTitle from '../common/PageTitle';


class ResourceRegistrationRequestPage extends React.Component  {
  constructor(props) {
    super(props);
  }


  render() {
    return (
      <>
        <PageTitle
          icon="icon-list"
          title="Request resource form"
          description="Please complete this form to register a new resource in an existing prefix. Completing all
                       fields will enable a swift processing of your request."
        />

        <div className="container py-3">
        </div>
      </>
    );
  }
}

// Redux Mappings.
const mapStateToProps = (state) => ({
});

const mapDispatchToProps = (dispatch) => ({
});

export default connect (mapStateToProps, mapDispatchToProps)(ResourceRegistrationRequestPage);
