import React from 'react';
import { connect } from 'react-redux';

import { Collapse } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEdit, faLeaf, faSitemap, faCube, faUser, faCheck } from '@fortawesome/free-solid-svg-icons';

import { prefixRegistrationRequestAmend } from '../../actions/PrefixRegistrationSession';

import { setPrefixRegistrationSessionAmendField } from '../../actions/PrefixRegistrationSessionAmend';


class PrefixRegistrationSessionAmendForm extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      name: this.props.prefixRegistrationSessionAmend.name,
      description: this.props.prefixRegistrationSessionAmend.description,
      requestedPrefix: this.props.prefixRegistrationSessionAmend.requestedPrefix,
      sampleId: this.props.prefixRegistrationSessionAmend.sampleId,
      idRegexPattern: this.props.prefixRegistrationSessionAmend.idRegexPattern,
      supportingReferences: this.props.prefixRegistrationSessionAmend.supportingReferences,
      additionalInformation: this.props.prefixRegistrationSessionAmend.additionalInformation,
      institutionName: this.props.prefixRegistrationSessionAmend.institutionName,
      institutionDescription: this.props.prefixRegistrationSessionAmend.institutionDescription,
      institutionHomeUrl: this.props.prefixRegistrationSessionAmend.institutionHomeUrl,
      institutionLocation: this.props.prefixRegistrationSessionAmend.institutionLocation,
      providerName: this.props.prefixRegistrationSessionAmend.providerName,
      providerDescription: this.props.prefixRegistrationSessionAmend.providerDescription,
      providerHomeUrl: this.props.prefixRegistrationSessionAmend.providerHomeUrl,
      providerCode: this.props.prefixRegistrationSessionAmend.providerCode,
      providerUrlPattern: this.props.prefixRegistrationSessionAmend.providerUrlPattern,
      providerLocation: this.props.prefixRegistrationSessionAmend.providerLocation,
      requesterName: this.props.prefixRegistrationSessionAmend.requesterName,
      requesterEmail: this.props.prefixRegistrationSessionAmend.requesterEmail,
      fieldsChanged: new Set()
    }
  }


  handleAmend = async () => {
    console.log('AMEND!');
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

    // Highlight the field if it is different than the original request in the session.
    if (prefixRegistrationRequest[field] !== value) {
      this.setState(prevState => ({fieldsChanged: prevState.fieldsChanged.add(field)}));
    } else {
      this.setState(prevState => {
        prevState.fieldsChanged.delete(field);
        return {fieldsChanged: prevState.fieldsChanged}
      });
    }
  }


  fieldChanged = (field) => this.state.fieldsChanged.has(field);


  render() {
    const {
      props: {
        isOpen,
        locationList,
        prefixRegistrationSessionAmend
      },
      fieldChanged,
      handleAmend,
      handleChangeField
    } = this;

    return (
      <Collapse className="bg-gray pt-3 pb-2 px-4 rounded-lg" isOpen={isOpen}>
        <div className="row">
          <div className="col mb-3">
            <h4><FontAwesomeIcon icon={faEdit} /> New amend</h4>
          </div>
        </div>

{/* ======================================== TABLE FORM FOR REGISTRATION AMEND ======================================== */}
        <div className="row no-gutters align-items-center bg-light rounded p-2 mb-1">
          <div className="col col-sm-4 col-lg-3 col-xl-2">
            <p><FontAwesomeIcon icon={faLeaf} /> <strong>Prefix details</strong></p>
          </div>
          <div className="col col-sm-8 col-lg-9 col-xl-10">
            <table className="table table-sm m-0 table-borderless table-striped">
              <tbody>
                <tr>
                  {/* TODO: */}
                  {/* ESTO HAY QUE PASARLO A UN COMPONENTE */}
                  <td className="w-25 pl-2 font-weight-bold align-middle">
                    <div className="row">
                      <div className="col">
                        Resource name
                      </div>
                    </div>
                  </td>
                  <td className="w-50">
                    <div className="input-group input-group-sm">
                      <input
                        type="text"
                        className={`form-control ${ fieldChanged('name') && 'border-warning border-2' }`}
                        value={prefixRegistrationSessionAmend.name}
                        onChange={(e) => handleChangeField('name', e.target.value)}
                      />
                      {
                        fieldChanged('name') && (
                          <div className="input-group-append">
                            <button
                              className="btn btn-secondary"
                              onClick={() => handleChangeField('name', undefined)}
                            >
                              Revert
                            </button>
                          </div>
                        )
                      }
                    </div>
                  </td>
                  <td className="w-25 align-middle">
                    {
                      fieldChanged('name') && (
                        <div className="col">
                          <span class="badge badge-secondary text-warning">Modified</span>
                        </div>
                      )
                    }
                  </td>
                </tr>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">Description</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <textarea
                        rows="5"
                        className="form-control"
                        value={prefixRegistrationSessionAmend.description}
                      />
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">Requested prefix</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <input
                        type="text"
                        className="form-control"
                        value={prefixRegistrationSessionAmend.requestedPrefix}
                      />
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">Regex pattern</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <input
                        type="text"
                        className="form-control"
                        value={prefixRegistrationSessionAmend.idRegexPattern}
                      />
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">supporting references</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <textarea
                        rows="5"
                        className="form-control"
                        value={prefixRegistrationSessionAmend.supportingReferences}
                      />
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">Additional information</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <input
                        type="text"
                        className="form-control"
                        value={prefixRegistrationSessionAmend.additionalInformation}
                      />
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div className="row no-gutters align-items-center bg-light rounded p-2 mb-1">
          <div className="col col-sm-4 col-lg-3 col-xl-2">
            <p><FontAwesomeIcon icon={faSitemap} /> <strong>Institution details</strong></p>
          </div>
          <div className="col col-sm-8 col-lg-9 col-xl-10">
            <table className="table table-sm m-0 table-borderless table-striped">
              <tbody>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">Name</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <input
                        type="text"
                        className="form-control"
                        value={prefixRegistrationSessionAmend.institutionName}
                      />
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">description</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <textarea
                        rows="5"
                        className="form-control"
                        value={prefixRegistrationSessionAmend.institutionDescription}
                      />
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">Home URL</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <input
                        type="text"
                        className="form-control"
                        value={prefixRegistrationSessionAmend.institutionHomeUrl}
                      />
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">Location</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
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
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div className="row no-gutters align-items-center bg-light rounded p-2 mb-1">
          <div className="col col-sm-4 col-lg-3 col-xl-2">
            <p><FontAwesomeIcon icon={faCube} /> <strong>Provider details</strong></p>
          </div>
          <div className="col col-sm-8 col-lg-9 col-xl-10">
            <table className="table table-sm m-0 table-borderless table-striped">
              <tbody>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">Name</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <input
                        type="text"
                        className="form-control"
                        value={prefixRegistrationSessionAmend.providerName}
                      />
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">description</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <textarea
                        rows="5"
                        className="form-control small"
                        value={prefixRegistrationSessionAmend.providerDescription}
                      />
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">Home URL</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <input
                        type="text"
                        className="form-control"
                        value={prefixRegistrationSessionAmend.providerHomeUrl}
                      />
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">Provider code</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <input
                        type="text"
                        className="form-control"
                        value={prefixRegistrationSessionAmend.providerCode}
                      />
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">URL Pattern</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <input
                        type="text"
                        className="form-control"
                        value={prefixRegistrationSessionAmend.providerUrlPattern}
                      />
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">Location</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
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
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>


        <div className="row no-gutters align-items-center bg-light rounded p-2 mb-1">
          <div className="col col-sm-4 col-lg-3 col-xl-2">
            <p><FontAwesomeIcon icon={faUser} /> <strong>Requester details</strong></p>
          </div>
          <div className="col col-sm-8 col-lg-9 col-xl-10">
            <table className="table table-sm m-0 table-borderless table-striped">
              <tbody>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">Full name</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <input
                        type="text"
                        className="form-control"
                        value={prefixRegistrationSessionAmend.requesterName}
                      />
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="w-25 pl-2 font-weight-bold">Email</td>
                  <td className="w-75">
                    <div className="input-group input-group-sm">
                      <input
                        type="text"
                        className="form-control"
                        value={prefixRegistrationSessionAmend.requesterEmail}
                      />
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div className="row">
          <div className="col mt-2">
              <a
                className={`btn btn-warning btn-block`}
                href="#!"
                onClick={handleAmend}
              >
                <FontAwesomeIcon icon={faEdit} /> Confirm amend
              </a>
            </div>
        </div>

{/* ======================================== TABLE FORM FOR REGISTRATION AMEND ======================================== */}

      </Collapse>
    );
  }
}

// *****************************************************************************************
// TODO - VOY POR QUE EL STATE AMEND SE RELLENE DIRECTAMENTE CON EL REQUEST. <--------------
// *****************************************************************************************

// Mapping
const mapStateToProps = (state) => ({
  prefixRegistrationSession: state.curatorDashboard.prefixRegistrationSession,
  prefixRegistrationSessionAmend: state.curatorDashboard.prefixRegistrationSessionAmend,
  locationList: state.locationList
});

const mapDispatchToProps = (dispatch) => ({
  prefixRegistrationRequestAmend: (id, prefixRegistrationRequest, additionalInformation) => dispatch(prefixRegistrationRequestAmend(id, prefixRegistrationRequest, additionalInformation)),
  setPrefixRegistrationSessionAmendField: (field, value) => dispatch(setPrefixRegistrationSessionAmendField(field, value))
});

export default connect (mapStateToProps, mapDispatchToProps)(PrefixRegistrationSessionAmendForm);

