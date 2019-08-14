import React from 'react';
import { connect } from 'react-redux';

// Actions.
import { resourceRegistrationRequestAmend } from '../../actions/CurationDashboardPage/ResourceRegistrationSession';
import { setRegistrationSessionAmendField } from '../../actions/CurationDashboardPage/RegistrationSessionAmend';

// Components.
import ReversibleField from '../common/ReversibleField';

// Utils.
import { successToast, infoToast, swalError, swalSuccess, failureToast } from '../../utils/swalDialogs';
import validators from '../../utils/validators';


class ResourceRegistrationSessionAmendForm extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      fieldsChanged: new Set()
    }
  }


  handleClickAmend = async () => {
    const { id, handleFormSubmit, resourceRegistrationRequestAmend, resourceRegistrationSessionAmend } = this.props;

    const response = await resourceRegistrationRequestAmend(id, resourceRegistrationSessionAmend);

    if (response.status === 200) {
      await swalSuccess.fire({
        title: 'Resource Registration Request amended succesfully'
      });

      handleFormSubmit();
    } else {
      await swalError.fire({
        title: 'Error',
        text: 'Could not amend Resource Registration Request'
      });
    }
  }

  handleClickValidate = async () => {
    const { resourceRegistrationSessionAmend } = this.props;
    const fieldsToValidate = [...this.state.fieldsChanged];

    if (fieldsToValidate.length === 0) {
      infoToast('No fields have changed');
      return;
    }

    const validations = await Promise.all(fieldsToValidate
      .map(field => validators[field](resourceRegistrationSessionAmend[field], resourceRegistrationSessionAmend, 'resource'))
    );
    const toastMessage = validations
      .filter(validations => !validations.valid)
      .map(validations => validations.errorMessage).join('\n');

    if (toastMessage !== '') {
      failureToast(toastMessage, undefined, 10000);
    } else {
      successToast('All fields OK!');
    }
  }


  //
  // Field manipulation handler. Undefined 'value' field will revert to default.
  //
  handleChangeField = (field, value) => {
    const {
      setResourceRegistrationSessionAmendField,
      resourceRegistrationSession: { resourceRegistrationRequest }
    } = this.props;

    if (typeof value === 'undefined') {
      value = resourceRegistrationRequest[field];
    }

    setResourceRegistrationSessionAmendField(field, value);

    // Add/remove field to changed list if modified/reset, so it is validated or not when clicked on the perform
    // validation button.
    if (resourceRegistrationRequest[field] !== value) {
      this.setState(prevState => ({fieldsChanged: prevState.fieldsChanged.add(field)}));
    } else {
      this.setState(prevState => {
        prevState.fieldsChanged.delete(field);
        return {fieldsChanged: prevState.fieldsChanged}
      });
    }
  }


  render() {
    const {
      props: {
        isOpen,
        locationList,
        resourceRegistrationSession: { resourceRegistrationRequest },
        resourceRegistrationSessionAmend
      },
      fieldChanged,
      handleChangeField,
      handleClickAmend,
      handleClickValidate
    } = this;

    return isOpen ? (
      <div className="bg-gray pt-3 pb-2 px-4">
        <div className="row">
          <div className="col mb-3">
            <h4><i className="icon icon-common icon-edit" /> New amend</h4>
          </div>
        </div>

        {/* ================================== TABLE FORM FOR REGISTRATION AMEND  ================================== */}
        <div className="row no-gutters align-items-center bg-light rounded p-2 mb-1">
          <div className="col col-sm-4 col-lg-3 col-xl-2">
            <p><i className="icon icon-common icon-leaf" /> <strong>Resource details</strong></p>
          </div>
          <div className="col col-sm-8 col-lg-9 col-xl-10">
            <table className="table table-sm m-0 table-borderless table-striped">
              <tbody>

                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold align-middle">Namespace Prefix</td>
                  <td className="w-75">
                    <ReversibleField fieldName="namespacePrefix" defaultValue={resourceRegistrationRequest.namespacePrefix} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div className="row no-gutters align-items-center bg-light rounded p-2 mb-1">
          <div className="col col-sm-4 col-lg-3 col-xl-2">
            <p><i className="icon icon-common icon-sitemap" /> <strong>Institution details</strong></p>
          </div>
          <div className="col col-sm-8 col-lg-9 col-xl-10">
            <table className="table table-sm m-0 table-borderless table-striped">
              <tbody>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Name</td>
                  <td className="w-75">
                    <ReversibleField fieldName="institutionName" defaultValue={resourceRegistrationRequest.institutionName} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">description</td>
                  <td className="w-75">
                    <ReversibleField fieldName="institutionDescription" defaultValue={resourceRegistrationRequest.institutionDescription} handleChangeField={handleChangeField}>
                      <textarea rows={5} />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Home URL</td>
                  <td className="w-75">
                    <ReversibleField fieldName="institutionHomeUrl" defaultValue={resourceRegistrationRequest.institutionHomeUrl} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Location</td>
                  <td className="w-75">
                    <ReversibleField fieldName="institutionLocation" defaultValue={resourceRegistrationRequest.institutionLocation} handleChangeField={handleChangeField}>
                      <select
                        className="form-control"
                        value={resourceRegistrationSessionAmend.institutionLocation}
                      >
                        <option value="" disabled>Select location...</option>
                        {
                          locationList.map(location =>
                            <option
                              value={location.id}
                              key={`option-${location.id}`}
                            >
                              {location.label}
                            </option>
                          )
                        }
                      </select>
                    </ReversibleField>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div className="row no-gutters align-items-center bg-light rounded p-2 mb-1">
          <div className="col col-sm-4 col-lg-3 col-xl-2">
            <p><i className="icon icon-common icon-cube" /> <strong>Provider details</strong></p>
          </div>
          <div className="col col-sm-8 col-lg-9 col-xl-10">
            <table className="table table-sm m-0 table-borderless table-striped">
              <tbody>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Name</td>
                  <td className="w-75">
                    <ReversibleField fieldName="providerName" defaultValue={resourceRegistrationRequest.providerName} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">description</td>
                  <td className="w-75">
                    <ReversibleField fieldName="providerDescription" defaultValue={resourceRegistrationRequest.providerDescription} handleChangeField={handleChangeField}>
                      <textarea rows={5} />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Home URL</td>
                  <td className="w-75">
                    <ReversibleField fieldName="providerHomeUrl" defaultValue={resourceRegistrationRequest.providerHomeUrl} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Provider code</td>
                  <td className="w-75">
                    <ReversibleField fieldName="providerCode" defaultValue={resourceRegistrationRequest.providerCode} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">URL Pattern</td>
                  <td className="w-75">
                    <ReversibleField fieldName="providerUrlPattern" defaultValue={resourceRegistrationRequest.providerUrlPattern} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Sample ID</td>
                  <td className="w-75">
                    <ReversibleField fieldName="sampleId" defaultValue={resourceRegistrationRequest.sampleId} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Location</td>
                  <td className="w-75">
                    <ReversibleField fieldName="providerLocation" defaultValue={resourceRegistrationRequest.providerLocation} handleChangeField={handleChangeField}>
                      <select
                        className="form-control"
                        value={resourceRegistrationSessionAmend.providerLocation}
                        >
                        <option value="" disabled>Select location...</option>
                        {
                          locationList.map(location =>
                            <option
                              value={location.id}
                              key={`option-${location.id}`}
                            >
                              {location.label}
                            </option>
                          )
                        }
                      </select>
                    </ReversibleField>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>


        <div className="row no-gutters align-items-center bg-light rounded p-2 mb-1">
          <div className="col col-sm-4 col-lg-3 col-xl-2">
            <p><i className="icon icon-common icon-user" /> <strong>Requester details</strong></p>
          </div>
          <div className="col col-sm-8 col-lg-9 col-xl-10">
            <table className="table table-sm m-0 table-borderless table-striped">
              <tbody>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Full name</td>
                  <td className="w-75">
                    <ReversibleField fieldName="requesterName" defaultValue={resourceRegistrationRequest.requesterName} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Email</td>
                  <td className="w-75">
                    <ReversibleField fieldName="requesterEmail" defaultValue={resourceRegistrationRequest.requesterEmail} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div className="row">
          <div className="col col-6 mt-2">
            <a
              className="btn btn-warning btn-block mr-1 no-highlight-visited"
              href="#!"
              onClick={handleClickValidate}
            >
              <i className="icon icon-common icon-check" /> Perform validation
            </a>
          </div>

          <div className="col col-6 mt-2">
            <a
              className="btn btn-warning btn-block ml-1 no-highlight-visited"
              href="#!"
              onClick={handleClickAmend}
            >
              <i className="icon icon-common icon-edit" /> Confirm amend
            </a>
          </div>
        </div>

        {/* ================================   TABLE FORM FOR REGISTRATION AMEND  ================================== */}

      </div>
    ) : null;
  }
}


// Mapping
const mapStateToProps = (state) => ({
  resourceRegistrationSession: state.curationDashboard.resourceRegistrationSession,
  resourceRegistrationSessionAmend: state.curationDashboard.resourceRegistrationSessionAmend,
  locationList: state.locationList
});

const mapDispatchToProps = (dispatch) => ({
  resourceRegistrationRequestAmend: (id, resourceRegistrationRequest, additionalInformation) =>
    dispatch(resourceRegistrationRequestAmend(id, resourceRegistrationRequest, additionalInformation)),

  setResourceRegistrationSessionAmendField: (field, value) =>
    dispatch(setRegistrationSessionAmendField(field, value, 'resource')),
});

export default connect (mapStateToProps, mapDispatchToProps)(ResourceRegistrationSessionAmendForm);

