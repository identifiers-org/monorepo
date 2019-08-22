import React from 'react';
import { connect } from 'react-redux';

// Actions.
import { setInstitutionPatchField, patchInstitution, setInstitutionPatch } from '../../actions/CurationDashboardPage/InstitutionPatch';
import { getCurationInstitutionListFromRegistry, deleteInstitutionFromRegistry } from '../../actions/CurationDashboardPage/CurationInstitutionList';

// Components.
import ReversibleField from '../common/ReversibleField';
import RoleConditional from '../common/RoleConditional';

// Utils.
import { swalConfirmation, successToast, failureToast, infoToast, namespacesUsingInstitutionsModal } from '../../utils/swalDialogs';


class CurationInstitutionItem extends React.Component {
  constructor(props) {
    super(props)

    this.state = {
      expanded: false,
      editInstitution: false,
      institutionFieldsChanged: new Set(),
      institutionId: this.props.institution.id,
      newInstitution: {
        description: this.props.institution.description,
        homeUrl: this.props.institution.homeUrl,
        name: this.props.institution.name,
        location: this.props.institution.location.id
      }
    };
  }

  toggle = () => {
    this.setState(state => ({ expanded: !state.expanded }));
  };


  //
  // Field manipulation handler. Undefined 'value' field will revert to default.
  //
  handleChangeField = (field, value) => {
    const {
      props: { institution }
    } = this;

    // Add/remove field to changed list if modified/reset, so it is validated or not when clicked on the perform
    // validation button.
    if (institution[field] !== value) {
      this.setState(prevState => ({
        institutionFieldsChanged: prevState.institutionFieldsChanged.add(field),
        newInstitution: {...this.state.newInstitution, [field]: value}
      }));
    } else {
      this.setState(prevState => {
        prevState.institutionFieldsChanged.delete(field);
        return {
          institutionFieldsChanged: prevState.institutionFieldsChanged,
          newInstitution: {...this.state.newInstitution, [field]: institution[field]}
        };
      });
    }

    this.props.setInstitutionPatchField(field, value);
  };


  handleClickCommitButton = async () => {
    const {
      state: {
        institutionId,
        institutionFieldsChanged,
        newInstitution
      },
      props: {
        curationInstitutionListParams,
        getCurationInstitutionListFromRegistry,
        patchInstitution,
        setInstitutionPatch
      }
    } = this;

    if (institutionFieldsChanged.size === 0) {
      infoToast('No changes to commit');
      return;
    }

    // Make sure the current institution is in redux state for committing.
    await setInstitutionPatch(institutionId, newInstitution);


    const modalResponse = await swalConfirmation.fire({
      title: 'Confirm changes to institution',
      text: `Changed fields: ${[...institutionFieldsChanged].join(', ')}`,
      confirmButtonText: 'Commit changes',
      cancelButtonText: 'Cancel'
    });

    if (modalResponse.value) {
      const result = await patchInstitution(institutionId, newInstitution);

      if (result.status === 200) {
        successToast('Changed committed successfully');
        this.setState({editInstitution: false, expanded: false, institutionFieldsChanged: new Set()});
        await getCurationInstitutionListFromRegistry(curationInstitutionListParams);
      } else {
        failureToast('Error committing changes');
      }
    }
  };

  handleClickDiscardButton = async () => {
    const modalResponse = await swalConfirmation.fire({
      title: 'Are you sure?',
      text: 'All data changed will be lost',
      confirmButtonText: 'Discard changes',
      cancelButtonText: 'Continue editing'
    });

    if (modalResponse.value) {
      this.setState({editInstitution: false, expanded: false});
    }
  };

  handleClickEditButton = () => {
    const {
      props: { institution: { id, homeUrl, description, name, location }, setInstitutionPatch },
      state: { editInstitution }
    } = this;

    const newInstitution = {
      name,
      description,
      homeUrl,
      location: location.id
    }

    if (editInstitution) {
      this.setState({ editInstitution: false });
    } else {
      this.setState({editInstitution: true, expanded: true});
      setInstitutionPatch(id, newInstitution);
    };
  };

  handleClickDeleteButton = async () => {
    const {
      props: { curationInstitutionListParams, deleteInstitution, getCurationInstitutionListFromRegistry },
      state: { institutionId }
    } = this;

    const modalResponse = await swalConfirmation.fire({
      title: 'Confirm institution deletion',
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel'
    });

    if (modalResponse.value) {
      const result = await deleteInstitution(institutionId);

      // Correct deletion.
      if (result.status === 200) {
        successToast('Institution deleted successfully');
        this.setState({editInstitution: false, expanded: false, institutionFieldsChanged: new Set()});
        await getCurationInstitutionListFromRegistry(curationInstitutionListParams);

      // Some namespaces are using the institution.
      } else if (result.status === 400) {
        const namespaceDetailsUrl = `${window.location.protocol}//${window.location.host}/registry`;
        const html = (
          <div>
            <p>The following Namespaces are using that Institution:</p>
            {result.namespacesUsingInstitution.map(namespace => (
              <div className="row" key={`namespace-${namespace.prefix}`}>
                <div className="col mb-3">
                  <div className="d-inline border bg-light p-1">
                    <a href={`${namespaceDetailsUrl}/${namespace.prefix}`} target="_blank" rel="noopener noreferrer">
                      <span
                        className="align-top text-warning font-weight-bold border border-secondary badge badge-dark mr-2"
                      >
                        {namespace.prefix}
                      </span>
                      <span
                        className="clear-link"
                      >
                        {namespace.name}
                      </span>
                    </a>
                  </div>
                </div>
              </div>
            ))}
          </div>
        );

        const namespacesUsingInstitutionsModalResult = await namespacesUsingInstitutionsModal.fire({html});

        if (namespacesUsingInstitutionsModalResult.value) {
          result.namespacesUsingInstitution.forEach(namespace => window.open(`${namespaceDetailsUrl}/${namespace.prefix}`, '_blank'));
        };

      // Failed deletion.
      } else {
        failureToast('Error deleting institution');
      }
    }
  }


  render() {
    const {
      handleChangeField,
      handleClickCommitButton,
      handleClickDeleteButton,
      handleClickEditButton,
      handleClickDiscardButton,
      props: { institution: { name, homeUrl, description, location }, locationList },
      state: { editInstitution, expanded }
     } = this;


     return (
      <div>
        <div className="card mb-1">
          <div className="card-header py-1 pr-1">
            <div className="row justify-content-between">
              <div className="col col-md-10 col-lg-9 col-xl-10">
                {editInstitution ? (
                  <RoleConditional
                    requiredRoles={['editInstitution']}
                    fallbackComponent={name}
                  >
                    <ReversibleField fieldName="name" defaultValue={name} handleChangeField={handleChangeField}>
                      <input type="text" />
                    </ReversibleField>
                  </RoleConditional>
                ) : (
                  <a
                    className="clear-link"
                    href="#!"
                    onClick={this.toggle}
                  >
                    {expanded ? <i className="icon icon-common icon-minus mr-2" /> : <i className="icon icon-common icon-plus mr-2" />}
                    <i className="icon icon-common icon-sitemap mr-2" />
                    {name}
                  </a>
                )}
              </div>
              <div className="col col-md-2 col-lg-3 col-xl-2 text-right">
                {!editInstitution ? (
                  <RoleConditional
                    requiredRoles={['editInstitution']}
                  >
                    <>
                      <button
                        className="clear-link btn btn-warning btn-sm m-0 mr-2 py-0 px-2"
                        onClick={handleClickEditButton}
                      >
                        <i className="icon icon-common icon-ellipsis-h mr-1" />Edit
                      </button>
                      <button
                        className="clear-link btn btn-danger btn-sm m-0 py-0 px-2"
                        onClick={handleClickDeleteButton}
                      >
                        <i className="icon icon-common icon-trash mr-1" />Delete
                      </button>
                    </>
                  </RoleConditional>
                ) : (
                  <>
                    <button
                      className="clear-link btn btn-success btn-sm m-0 mr-2 py-0 px-2"
                      onClick={handleClickCommitButton}
                    >
                      <i className="icon icon-common icon-check mr-1" />Commit
                    </button>
                    <button
                      className="clear-link btn btn-danger btn-sm m-0 py-0 px-2"
                      onClick={handleClickDiscardButton}
                    >
                      <i className="icon icon-common icon-ellipsis-h mr-1" />Discard
                    </button>
                  </>
                )}
              </div>
            </div>
          </div>

          {expanded && (
            <div className="card-body">
              <div className="row">
                <div className="col bg-light p-2">
                  <table className="table table-sm table-striped table-borderless mb-0">
                    <tbody>
                      <tr>
                        <td className="w-15">
                          <strong>Home URL</strong>
                        </td>
                        <td>
                          {editInstitution ? (
                            <RoleConditional
                              requiredRoles={['editInstitution']}
                              fallbackComponent={homeUrl}
                            >
                              <ReversibleField fieldName="homeUrl" defaultValue={homeUrl} handleChangeField={handleChangeField}>
                                <input type="text" />
                              </ReversibleField>
                            </RoleConditional>
                          ) : (
                            homeUrl
                          )}
                        </td>
                      </tr>
                      <tr>
                        <td className="w-15">
                          <strong>Description</strong>
                        </td>
                        <td>
                          {editInstitution ? (
                            <RoleConditional
                              requiredRoles={['editInstitution']}
                              fallbackComponent={description}
                            >
                              <ReversibleField fieldName="description" defaultValue={description} handleChangeField={handleChangeField}>
                                <textarea rows={5} />
                              </ReversibleField>
                            </RoleConditional>
                          ) : (
                            description
                          )}
                        </td>
                      </tr>
                      <tr>
                        <td className="w-15">
                          <strong>Location</strong>
                        </td>
                        <td>
                          {editInstitution ? (
                            <RoleConditional
                              requiredRoles={['editResource']}
                              fallbackComponent={location.countryName}
                            >
                              <ReversibleField fieldName="location" defaultValue={location.id} handleChangeField={handleChangeField}>
                                <select
                                  className="form-control"
                                  value={location}
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
                            location.countryName
                          )}
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    );
  }
}


//
// Redux Mappings
//
const mapStateToProps = (state) => ({
  locationList: state.locationList,
  curationInstitutionListParams: state.curationDashboard.curationInstitutionListParams
});

const mapDispatchToProps = dispatch => ({
  deleteInstitution: (institutionId) => dispatch(deleteInstitutionFromRegistry(institutionId)),
  getCurationInstitutionListFromRegistry: (params) => dispatch(getCurationInstitutionListFromRegistry(params)),
  setInstitutionPatch: (id, institution) => dispatch(setInstitutionPatch(id, institution)),
  setInstitutionPatchField: (field, value) => dispatch(setInstitutionPatchField(field, value)),
  patchInstitution: (id, newInstitution) => dispatch(patchInstitution(id, newInstitution))
});


export default connect(mapStateToProps, mapDispatchToProps)(CurationInstitutionItem);
