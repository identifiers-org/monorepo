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

// Mapping
const mapStateToProps = (state) => ({
  namespacePrefix: state.resourceRegistrationRequestForm.namespacePrefix,
  institutionName: state.resourceRegistrationRequestForm.institutionName,
  institutionDescription: state.resourceRegistrationRequestForm.institutionDescription,
  institutionHomeUrl: state.resourceRegistrationRequestForm.institutionHomeUrl,
  institutionLocation: state.resourceRegistrationRequestForm.institutionLocation,
  institutionIsProvider: state.resourceRegistrationRequestForm.institutionIsProvider,
  providerName: state.resourceRegistrationRequestForm.providerName,
  providerDescription: state.resourceRegistrationRequestForm.providerDescription,
  providerCode: state.resourceRegistrationRequestForm.providerCode,
  providerHomeUrl: state.resourceRegistrationRequestForm.providerHomeUrl,
  providerUrlPattern: state.resourceRegistrationRequestForm.providerUrlPattern,
  providerLocation: state.resourceRegistrationRequestForm.providerLocation,
  requesterName: state.resourceRegistrationRequestForm.requesterName,
  requesterEmail: state.resourceRegistrationRequestForm.requesterEmail,
  locationList: state.locationList
});

const mapDispatchToProps = (dispatch) => ({
  setValue: (id, value) => dispatch(setRegistrationRequestFieldField('PREFIX', id, 'value', value)),
  validate: (id) => dispatch(setRegistrationRequestFieldField('PREFIX', id, 'requestedValidate', true)),
  setValidation: (id, validation) => dispatch(setValidation('PREFIX', id, validation)),
  reset: (id) => dispatch(reset('PREFIX', id))
});

export default connect (mapStateToProps, mapDispatchToProps)(ResourceRegistrationRequestPage);
