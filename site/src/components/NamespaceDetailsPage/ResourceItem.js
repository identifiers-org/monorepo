import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';

// Components.
import ReversibleField from '../common/ReversibleField';
import RoleConditional from '../common/RoleConditional';

// Utils.
import { swalConfirmation } from '../../utils/swalDialogs';


class ResourceItem extends React.Component {
  constructor(props) {
    super(props);

    const params = new URLSearchParams(props.location.search);

    this.state = {
      editResource: params.get('editResource') === 'true' || false,
      resourceId: undefined,
      resourceFieldsChanged: new Set(),
      newResource: this.props.newResource
    }
  }


  //
  // Field manipulation handler. Undefined 'value' field will revert to default.
  //
  handleChangeField = (field, value) => {
    console.log('change field', field, value);
  }


  handleClickCommitChangesButton = async () => {
    console.log('commit changes');
  }

  handleClickDiscardChangesButton = async () => {
    const result = await swalConfirmation.fire({
      title: 'Are you sure?',
      text: 'All data changed will be lost',
      confirmButtonText: 'Discard changes',
      cancelButtonText: 'Continue editing'
    });

    if (result.value) {
      this.setState({editResource: false});
    }
  }

  handleClickValidateChangesButton = () => {
    console.log('validate changes');
  }

  handleClickEditButton = () => {
    this.setState({editResource: true});
  }



  render() {
    const {
      handleChangeField,
      handleClickCommitChangesButton,
      handleClickDiscardChangesButton,
      handleClickEditButton,
      handleClickValidateChangesButton,
      props: { locationList, resource },
      state: { editResource }
    } = this;

    const providerCodeLabel = resource.providerCode === 'CURATOR_REVIEW' ? 'Empty provider code' : resource.providerCode;
    const locationCountryCode = resource ? resource.location._links.self.href.split('/').pop() : undefined;

    return (
      <>
        {editResource ? (
          <div className="row">
            <div className="col">
            <RoleConditional
              requiredRoles={['editResource']}
            >
              <>
                <button
                  className="btn btn-sm btn-warning edit-button mb-3 mr-2"
                  onClick={handleClickValidateChangesButton}
                >
                  <i className="icon icon-common icon-tasks mr-1" />Perform validation
                </button>
                <button
                  className="btn btn-sm btn-success edit-button mb-3 mr-2"
                  onClick={handleClickCommitChangesButton}
                >
                  <i className="icon icon-common icon-check" /> Commit changes
                </button>
                <button
                  className="btn btn-sm btn-danger edit-button mb-3"
                  onClick={handleClickDiscardChangesButton}
                >
                  <i className="icon icon-common icon-times" /> Discard changes
                </button>
              </>
            </RoleConditional>
          </div>
        </div>
        ) : (
          <div className="row">
            <div className="col">
              <RoleConditional
                requiredRoles={['editResource']}
              >
                <button
                  className="btn btn-sm btn-success edit-button mb-3"
                  onClick={handleClickEditButton}
                >
                  <i className="icon icon-common icon-edit mr-1"/>Edit resource
                </button>
              </RoleConditional>
            </div>
          </div>
        )}

        <div className="row">
          <div className="col overflow-y-scroll">
            <table className="table table-sm table-striped table-borderless">
              <tbody>
                <tr>
                  <td
                    rowSpan="6"
                    className={`w-20 align-middle ${resource.official ? 'bg-warning' : 'bg-primary text-white'}`}
                  >
                    {editResource ? (
                      <RoleConditional
                        requiredRoles={['editResource']}
                        fallbackComponent={<p className="font-weight-bold text-center n-0">{providerCodeLabel}</p>}
                      >
                        <>
                          <p className="text-center font-weight-bold m-0">Provider code</p>
                          <ReversibleField fieldName="providerCode" defaultValue={resource.providerCode} handleChangeField={handleChangeField}>
                            <input type="text" />
                          </ReversibleField>
                        </>
                      </RoleConditional>
                    ) : (
                      <p className="font-weight-bold text-center n-0">{providerCodeLabel}</p>
                    )}
                    <p className="text-center m-0">{resource.mirId}</p>
                    <p className="font-weight-bold text-center m-0">{resource.official ? 'Primary' : ''}</p>
                  </td>
                  <td className="w-15 px-3">
                    Description</td>
                  <td className="resourceitem-table__wide">
                    {editResource ? (
                      <RoleConditional
                        requiredRoles={['editResource']}
                        fallbackComponent={resource.description}
                      >
                        <ReversibleField fieldName="description" defaultValue={resource.description} handleChangeField={handleChangeField}>
                          <textarea rows="5" />
                        </ReversibleField>
                      </RoleConditional>
                    ) : (
                      resource.description
                    )}
                  </td>
                </tr>
                <tr>
                  <td className="px-3">Access URL</td>
                  <td>
                    {editResource ? (
                      <RoleConditional
                        requiredRoles={['editResource']}
                        fallbackComponent={resource.urlPattern}
                      >
                        <ReversibleField fieldName="urlPattern" defaultValue={resource.urlPattern} handleChangeField={handleChangeField}>
                          <input type="text" />
                        </ReversibleField>
                      </RoleConditional>
                    ) : (
                      resource.urlPattern
                    )}
                  </td>
                </tr>
                <tr>
                  <td className="px-3">Institution</td>
                  <td>{resource.institution ? resource.institution.name : 'None'}</td>
                </tr>
                <tr>
                  <td className="px-3">Website</td>
                  <td>
                  {editResource ? (
                      <RoleConditional
                        requiredRoles={['editResource']}
                        fallbackComponent={resource.resourceHomeUrl}
                      >
                        <ReversibleField fieldName="resourceHomeUrl" defaultValue={resource.resourceHomeUrl} handleChangeField={handleChangeField}>
                          <input type="text" />
                        </ReversibleField>
                      </RoleConditional>
                    ) : (
                      resource.resourceHomeUrl
                    )}
                  </td>
                </tr>
                <tr>
                  <td className="px-3">Location</td>
                  <td>
                    {editResource ? (
                      <RoleConditional
                        requiredRoles={['editResource']}
                        fallbackComponent={resource.location.countryName}
                      >
                        <ReversibleField fieldName="institutionLocation" defaultValue={locationCountryCode} handleChangeField={handleChangeField}>
                          <select
                            className="form-control"
                            value={resource.location}
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
                      </RoleConditional>
                    ) : (
                      resource.location.countryName
                    )}
                  </td>
                </tr>
                <tr>
                  <td className="px-3">Sample Id</td>
                  <td>
                  {editResource ? (
                      <RoleConditional
                        requiredRoles={['editResource']}
                        fallbackComponent={resource.sampleId}
                      >
                        <ReversibleField fieldName="sampleId" defaultValue={resource.sampleId} handleChangeField={handleChangeField}>
                          <input type="text" />
                        </ReversibleField>
                      </RoleConditional>
                    ) : (
                      resource.sampleId
                    )}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </>
    );
  }
}

//
// Redux Mappings
//
const mapStateToProps = (state) => ({
  locationList: state.locationList
});


export default withRouter(connect(mapStateToProps)(ResourceItem));
