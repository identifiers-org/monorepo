import React from 'react';
import { connect } from 'react-redux';

// Actions.
import { prefixRegistrationRequestAmend } from '../../actions/CurationDashboardPage/PrefixRegistrationSession';
import { setRegistrationSessionAmendField } from '../../actions/CurationDashboardPage/RegistrationSessionAmend';

// Components.
import ReversibleField from '../common/ReversibleField';

// Utils.
import { successToast, infoToast, swalError, swalSuccess, failureToast } from '../../utils/swalDialogs';
import validators from '../../utils/validators';


class PrefixRegistrationSessionAmendForm extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      fieldsChanged: new Set()
    }
  }


  handleClickAmend = async () => {
    const { id, handleFormSubmit, prefixRegistrationRequestAmend, prefixRegistrationSessionAmend } = this.props;

    const response = await prefixRegistrationRequestAmend(id, prefixRegistrationSessionAmend);

    if (response.status === 200) {
      await swalSuccess.fire({
        title: 'Prefix Registration Request amended succesfully'
      });

      handleFormSubmit();
    } else {
      await swalError.fire({
        title: 'Error',
        text: 'Could not amend Prefix Registration Request'
      });
    }
  }

  handleClickValidate = async () => {
    const { prefixRegistrationSessionAmend } = this.props;
    const fieldsToValidate = [...this.state.fieldsChanged];

    if (fieldsToValidate.length === 0) {
      infoToast('No fields have changed');
      return;
    }

    // TODO: This has to be refactored to merge with the request prefix form. Take validators to a global place and use
    // them everywhere.
    const validations = await Promise.all(fieldsToValidate
      .map(field => validators[field](prefixRegistrationSessionAmend[field], prefixRegistrationSessionAmend, 'prefix'))
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
      setPrefixRegistrationSessionAmendField,
      prefixRegistrationSession: { prefixRegistrationRequest }
    } = this.props;

    if (typeof value === 'undefined') {
      value = prefixRegistrationRequest[field];
    }

    setPrefixRegistrationSessionAmendField(field, value);

    // Add/remove field to changed list if modified/reset, so it is validated or not when clicked on the perform
    // validation button.
    if (prefixRegistrationRequest[field] !== value) {
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
        prefixRegistrationSession: { prefixRegistrationRequest },
        prefixRegistrationSessionAmend
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
            <p><i className="icon icon-common icon-leaf" /> <strong>Prefix details</strong></p>
          </div>
          <div className="col col-sm-8 col-lg-9 col-xl-10">
            <table className="table table-sm m-0 table-borderless table-striped">
              <tbody>

                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold align-middle">Resource name</td>
                  <td className="w-75">
                    <ReversibleField fieldName="name" defaultValue={prefixRegistrationRequest.name} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>

                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold align-middle">Description</td>
                  <td className="w-75">
                    <ReversibleField fieldName="description" defaultValue={prefixRegistrationRequest.description} handleChangeField={handleChangeField}>
                      <textarea rows="5" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Requested prefix</td>
                  <td className="w-75">
                    <ReversibleField fieldName="requestedPrefix" defaultValue={prefixRegistrationRequest.requestedPrefix} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Sample ID</td>
                  <td className="w-75">
                    <ReversibleField fieldName="sampleId" defaultValue={prefixRegistrationRequest.sampleId} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Regex pattern</td>
                  <td className="w-75">
                    <ReversibleField fieldName="idRegexPattern" defaultValue={prefixRegistrationRequest.idRegexPattern} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Namespace in LUI</td>
                  <td className="w-75">
                    <ReversibleField fieldName="namespaceEmbeddedInLui" defaultValue={prefixRegistrationRequest.namespaceEmbeddedInLui} handleChangeField={handleChangeField}>
                      <input type="checkbox" className="form-check-input" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">supporting references</td>
                  <td className="w-75">
                    <ReversibleField fieldName="supportingReferences" defaultValue={prefixRegistrationRequest.supportingReferences} handleChangeField={handleChangeField}>
                      <textarea rows={5} />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Additional information</td>
                  <td className="w-75">
                    <ReversibleField fieldName="additionalInformation" defaultValue={prefixRegistrationRequest.additionalInformation} handleChangeField={handleChangeField}>
                      <textarea rows={5} />
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
                    <ReversibleField fieldName="institutionName" defaultValue={prefixRegistrationRequest.institutionName} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">description</td>
                  <td className="w-75">
                    <ReversibleField fieldName="institutionDescription" defaultValue={prefixRegistrationRequest.institutionDescription} handleChangeField={handleChangeField}>
                      <textarea rows={5} />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Home URL</td>
                  <td className="w-75">
                    <ReversibleField fieldName="institutionHomeUrl" defaultValue={prefixRegistrationRequest.institutionHomeUrl} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Location</td>
                  <td className="w-75">
                    <ReversibleField fieldName="institutionLocation" defaultValue={prefixRegistrationRequest.institutionLocation} handleChangeField={handleChangeField}>
                      <select
                        className="form-control"
                        value={prefixRegistrationSessionAmend.institutionLocation}
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
                    <ReversibleField fieldName="providerName" defaultValue={prefixRegistrationRequest.providerName} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">description</td>
                  <td className="w-75">
                    <ReversibleField fieldName="providerDescription" defaultValue={prefixRegistrationRequest.providerDescription} handleChangeField={handleChangeField}>
                      <textarea rows={5} />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Home URL</td>
                  <td className="w-75">
                    <ReversibleField fieldName="providerHomeUrl" defaultValue={prefixRegistrationRequest.providerHomeUrl} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Provider code</td>
                  <td className="w-75">
                    <ReversibleField fieldName="providerCode" defaultValue={prefixRegistrationRequest.providerCode} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">URL Pattern</td>
                  <td className="w-75">
                    <ReversibleField fieldName="providerUrlPattern" defaultValue={prefixRegistrationRequest.providerUrlPattern} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Location</td>
                  <td className="w-75">
                    <ReversibleField fieldName="providerLocation" defaultValue={prefixRegistrationRequest.providerLocation} handleChangeField={handleChangeField}>
                      <select
                        className="form-control"
                        value={prefixRegistrationSessionAmend.providerLocation}
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
                    <ReversibleField fieldName="requesterName" defaultValue={prefixRegistrationRequest.requesterName} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 align-middle pl-2 font-weight-bold">Email</td>
                  <td className="w-75">
                    <ReversibleField fieldName="requesterEmail" defaultValue={prefixRegistrationRequest.requesterEmail} handleChangeField={handleChangeField}>
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
  prefixRegistrationSession: state.curationDashboard.prefixRegistrationSession,
  prefixRegistrationSessionAmend: state.curationDashboard.prefixRegistrationSessionAmend,
  locationList: state.locationList
});

const mapDispatchToProps = (dispatch) => ({
  prefixRegistrationRequestAmend: (id, prefixRegistrationRequest, additionalInformation) =>
    dispatch(prefixRegistrationRequestAmend(id, prefixRegistrationRequest, additionalInformation)),

  setPrefixRegistrationSessionAmendField: (field, value) =>
    dispatch(setRegistrationSessionAmendField(field, value, 'prefix')),
});

export default connect (mapStateToProps, mapDispatchToProps)(PrefixRegistrationSessionAmendForm);

