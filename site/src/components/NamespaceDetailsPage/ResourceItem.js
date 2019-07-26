import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';

// Actions.
import { getResourcesFromRegistry } from '../../actions/NamespaceList';
import { setResourcePatch, setResourcePatchField, patchResource } from '../../actions/ResourcePatch';

// Components.
import ReversibleField from '../common/ReversibleField';
import RoleConditional from '../common/RoleConditional';

// Utils.
import { swalConfirmation, failureToast, successToast, infoToast } from '../../utils/swalDialogs';
import validators from '../../utils/validators';



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


  async componentDidMount() {
    // Prepares model for resource patching.
    const { resource } = this.props;

    this.setState({resourceId: resource.id, newResource: resource});
    this.props.setResourcePatch(resource.id, resource);
  }


  //
  // Field manipulation handler. Undefined 'value' field will revert to default.
  //
  handleChangeField = (field, value) => {
    const {
      props: { institutionList, locationList, resource }
    } = this;

    // Add/remove field to changed list if modified/reset, so it is validated or not when clicked on the perform
    // validation button.
    if (resource[field] !== value) {
      this.setState(prevState => ({
        resourceFieldsChanged: prevState.resourceFieldsChanged.add(field),
        newResource: {...this.state.newResource, [field]: value}
      }));
    } else {
      this.setState(prevState => {
        prevState.resourceFieldsChanged.delete(field);
        return {
          resourceFieldsChanged: prevState.resourceFieldsChanged,
          newResource: {...this.state.newResource, [field]: resource[field]}
        };
      });
    }

    this.props.setResourcePatchField(field, value);
  };


  handleClickAddInstitution = () => {
    // TODO: take me to institution curation list.
    console.log('add institution');
  };


  handleClickCommitChangesButton = async () => {
    const {
      state: { resourceId, resourceFieldsChanged, newResource },
      props: { patchResource, namespace }
    } = this;

    if (resourceFieldsChanged.size === 0) {
      infoToast('No changes to commit');
      return;
    }

    const result = await swalConfirmation.fire({
      title: 'Confirm changes to resource',
      text: `Changed fields: ${[...resourceFieldsChanged].join(', ')}`,
      confirmButtonText: 'Commit changes',
      cancelButtonText: 'Cancel'
    });

    if (result.value) {
      const result = await patchResource(resourceId, newResource);

      if (result.status === 200) {
        successToast('Changed committed successfully');
        await this.props.getResourcesFromRegistry(namespace);
        this.setState({editResource: false, resourceFieldsChanged: new Set()});
      } else {
        failureToast('Error committing changes');
      }
    }
  };

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
  };

  handleClickValidateChangesButton = async () => {
    const { newResource } = this.state;
    const resourceFieldsToValidate = [...this.state.resourceFieldsChanged];

    if (resourceFieldsToValidate.length === 0) {
      infoToast('No fields have changed');
      return;
    }

    console.log('fields', resourceFieldsToValidate);

    const validations = await Promise.all(resourceFieldsToValidate
      .map(field => validators[field](newResource[field], newResource))
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

  handleClickEditButton = () => {
    this.setState({editNamespace: true});
  }

  handleClickEditButton = () => {
    this.setState({editResource: true});
  };



  render() {
    const {
      handleChangeField,
      handleClickAddInstitution,
      handleClickCommitChangesButton,
      handleClickDiscardChangesButton,
      handleClickEditButton,
      handleClickValidateChangesButton,
      props: { institutionList, locationList, resource },
      state: { editResource }
    } = this;

    const providerCodeLabel = resource.providerCode === 'CURATOR_REVIEW' ? 'Empty provider code' : resource.providerCode;
    const locationCountryCode = resource ? resource.location._links.self.href : undefined;
    const institutionId = resource ? resource.institution._links.self.href : undefined;


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
                  <td className="d-flex">
                    {editResource ? (
                      <RoleConditional
                        requiredRoles={['editResource']}
                        fallbackComponent={resource.institution.name}
                      >
                        <>
                          <ReversibleField fieldName="institution" defaultValue={institutionId} handleChangeField={handleChangeField}>
                            <select
                              className="form-control"
                              value={institutionId}
                            >
                              <option value="" disabled>Select institution...</option>
                              {
                                institutionList.map(institution =>
                                  <option
                                    value={institution.id}
                                    key={`option-${institution.id}`}
                                  >
                                    {institution.name}
                                  </option>
                                )
                              }
                            </select>
                          </ReversibleField>
                          <button
                            className="btn btn-sm btn-primary ml-2"
                            onClick={handleClickAddInstitution}
                          >
                            <i className="icon icon-common icon-plus" />
                          </button>
                        </>
                      </RoleConditional>
                    ) : (
                      resource.institution.name
                    )}
                  </td>
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
                        <ReversibleField fieldName="location" defaultValue={locationCountryCode} handleChangeField={handleChangeField}>
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
  institutionList: state.institutionList,
  locationList: state.locationList
});

const mapDispatchToProps = dispatch => ({
  getResourcesFromRegistry: (namespace) => dispatch(getResourcesFromRegistry(namespace)),
  setResourcePatch: (id, resource) => dispatch(setResourcePatch(id, resource)),
  setResourcePatchField: (field, value) => dispatch(setResourcePatchField(field, value)),
  patchResource: (id, newResource) => dispatch(patchResource(id, newResource))
});



export default withRouter(connect(mapStateToProps, mapDispatchToProps)(ResourceItem));
