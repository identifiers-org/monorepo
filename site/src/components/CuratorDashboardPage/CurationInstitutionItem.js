import React from 'react';
import { connect } from 'react-redux';

// Components.
import ReversibleField from '../common/ReversibleField';
import RoleConditional from '../common/RoleConditional';

// Utils.
import { swalConfirmation } from '../../utils/swalDialogs';


class CurationInstitutionItem extends React.Component {
  constructor(props) {
    super(props)

    this.state = {
      expanded: false,
      editInstitution: false,
      institutionFieldsChanged: new Set(),
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

    //this.props.setResourcePatchField(field, value);
  };


  handleClickCommitButton = async () => {
    console.log('commit', this.state.newInstitution);
  };

  handleClickDiscardButton = async () => {
    const result = await swalConfirmation.fire({
      title: 'Are you sure?',
      text: 'All data changed will be lost',
      confirmButtonText: 'Discard changes',
      cancelButtonText: 'Continue editing'
    });

    if (result.value) {
      this.setState({editInstitution: false});
    }
  };

  handleClickEditButton = () => {
    const { institution } = this.props;

    this.state.editInstitution ?
      this.setState(state => ({ editInstitution: false }))
      :
      this.setState({editInstitution: true, expanded: true});
  };


  render() {
    const {
      handleChangeField,
      handleClickCommitButton,
      handleClickEditButton,
      handleClickDiscardButton,
      props: { institution: { id, name, homeUrl, description, location }, locationList },
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
                    <strong><i className="icon icon-common icon-sitemap mr-2" /></strong>
                    {name}
                  </a>
                )}
              </div>
              <div className="col col-md-2 col-lg-3 col-xl-2 text-right">
                {!editInstitution ? (
                  <RoleConditional
                    requiredRoles={['editInstitution']}
                  >
                    <button
                      className="clear-link btn btn-warning btn-sm m-0 py-0 px-2"
                      onClick={handleClickEditButton}
                    >
                      <i className="icon icon-common icon-ellipsis-h" /> Edit
                    </button>
                  </RoleConditional>
                ) : (
                  <>
                    <button
                      className="clear-link btn btn-success btn-sm m-0 mr-2 py-0 px-2"
                      onClick={handleClickCommitButton}
                    >
                      <i className="icon icon-common icon-check" /> Commit
                    </button>
                    <button
                      className="clear-link btn btn-danger btn-sm m-0 py-0 px-2"
                      onClick={handleClickDiscardButton}
                    >
                      <i className="icon icon-common icon-ellipsis-h" /> Discard
                    </button>
                  </>
                )}
              </div>
            </div>
          </div>

          {expanded && (
            <div className="card-body">
              <table className="table table-sm table-striped table-borderless">
                <tbody>
                  <tr>
                    <td className="w-35">
                      Home URL
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
                    <td className="w-35">
                      Description
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
                        homeUrl
                      )}
                    </td>
                  </tr>
                  <tr>
                    <td className="w-35">
                      Location
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
  locationList: state.locationList
});


export default connect(mapStateToProps)(CurationInstitutionItem);
