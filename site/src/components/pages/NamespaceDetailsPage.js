import React from 'react';
import { connect } from 'react-redux';

// Actions.
import { getNamespaceFromRegistry, getResourcesFromRegistry } from '../../actions/NamespaceList';

// Components.
import PageTitle from '../common/PageTitle';
import ResourceItem from '../NamespaceDetailsPage/ResourceItem';
import ReversibleField from '../common/ReversibleField';
import RoleConditional from '../common/RoleConditional';

// Config.
import { config } from '../../config/Config';

// Utils.
import { swalConfirmation } from '../../utils/swalDialogs';


class NamespaceDetailsPage extends React.Component {
  constructor (props) {
    super(props);

    const params = new URLSearchParams(props.location.search);
    this.state = {
      editNamespace: params.get('editNamespace') === 'true' || false,
      namespaceFieldsChanged: new Set()
    }
  }


  async componentDidMount() {
    await this.props.getNamespaceFromRegistry(this.props.match.params.prefix);
    await this.props.getResourcesFromRegistry(this.props.namespaceList[0]);
  }


  //
  // Field manipulation handler. Undefined 'value' field will revert to default.
  //
  handleChangeField = (field, value) => {
    const namespace = this.props.namespaceList[0];

    // Add/remove field to changed list if modified/reset, so it is validated or not when clicked on the perform
    // validation button.
    if (namespace[field] !== value) {
      this.setState(prevState => ({namespaceFieldsChanged: prevState.namespaceFieldsChanged.add(field)}));
    } else {
      this.setState(prevState => {
        prevState.namespaceFieldsChanged.delete(field);
        return {namespaceFieldsChanged: prevState.namespaceFieldsChanged}
      });
    }
  }


  handleClickCommitChangesButton = () => {
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
      this.setState({editNamespace: false});
    }
  }

  handleClickValidateChangesButton = () => {
    console.log('validate changes');
  }

  handleClickEditButton = () => {
    this.setState({editNamespace: true});
  }

  // TODO: create model for changes, and validate / commit.


  render() {
    const {
      handleChangeField,
      handleClickCommitChangesButton,
      handleClickDiscardChangesButton,
      handleClickEditButton,
      handleClickValidateChangesButton,
      state: { editNamespace }
    } = this;

    const namespace = this.props.namespaceList[0];

    return !namespace ? (
      '' // Placeholder for empty/not found.
    ) : (
      <>
        <PageTitle
          icon="icon-leaf"
          title={`Data collection:`}
          extraTitle={namespace.name}
        />

        {editNamespace ? (
          <>
            <button
              className="btn btn-sm btn-warning edit-button mb-3 mr-2"
              onClick={handleClickValidateChangesButton}
            >
              <i className="icon icon-common icon-check mr-1" />Perform validation
            </button>
            <button
              className="btn btn-sm btn-success edit-button mb-3 mr-2"
              onClick={handleClickCommitChangesButton}
            >
              <i className="icon icon-common icon-save" /> Commit changes
            </button>
            <button
              className="btn btn-sm btn-danger edit-button mb-3"
              onClick={handleClickDiscardChangesButton}
            >
              <i className="icon icon-common icon-times" /> Discard changes
            </button>
          </>
        ) : (
          <button
            className="btn btn-sm btn-success edit-button mb-3"
            onClick={handleClickEditButton}
          >
            <i className="icon icon-common icon-edit mr-1"/>Edit namespace
          </button>
        )}

        <div className="row">
          <div className="col">
            <h2><i className="icon icon-common icon-info" /> General Information</h2>
          </div>
        </div>

        <div className="row mb-3">
          <div className="col overflow-y-scroll">
            <table className="table table-sm table-striped table-borderless">
              <tbody>
                <tr>
                  <td className="w-35">
                    Resource Name
                  </td>
                  <td>
                    {editNamespace ? (
                      <RoleConditional
                        requiredRoles={['editNamespace']}
                        fallbackComponent={namespace.name}
                      >
                        <ReversibleField fieldName="name" defaultValue={namespace.name} handleChangeField={handleChangeField}>
                          <input type="text" />
                        </ReversibleField>
                      </RoleConditional>
                    ) : (
                      namespace.name
                    )}
                  </td>
                </tr>
                <tr>
                  <td>Description</td>
                  <td>
                    {editNamespace ? (
                      <RoleConditional
                        requiredRoles={['editNamespace']}
                        fallbackComponent={namespace.description}
                      >
                        <ReversibleField fieldName="description" defaultValue={namespace.description} handleChangeField={handleChangeField}>
                          <textarea rows="5" />
                        </ReversibleField>
                      </RoleConditional>
                    ) : (
                      namespace.description
                    )}
                  </td>
                </tr>
                <tr>
                  <td>Identifier pattern</td>
                  <td>
                    {editNamespace ? (
                      <RoleConditional
                        requiredRoles={['editNamespace']}
                        fallbackComponent={namespace.pattern}
                      >
                        <ReversibleField fieldName="pattern" defaultValue={namespace.pattern} handleChangeField={handleChangeField}>
                        <input type="text" />
                        </ReversibleField>
                      </RoleConditional>
                    ) : (
                      namespace.pattern
                    )}
                  </td>
                </tr>
                <tr>
                  <td>Registry identifier</td>
                  <td>{namespace.mirId}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div className="row">
          <div className="col">
            {/* TODO: Change to FINGERPRINT when EBI adds it to EBI-Font-icons */}
            <h2><i className="icon icon-common icon-barcode" /> Identification schemes</h2>
          </div>
        </div>

        <div className="row mb-3">
          <div className="col overflow-y-scroll">
            <table className="table table-sm table-striped table-borderless">
              <tbody>
                <tr>
                  <td className="w-35">
                    Namespace
                  </td>
                  <td>
                    {namespace.prefix}
                  </td>
                </tr>
                <tr>
                  <td>URI</td>
                  <td>{config.baseUrl}registry/{namespace.prefix}</td>
                </tr>
                <tr>
                  <td>Compact identifier</td>
                  <td>{namespace.prefix}:{'{'}accession number{'}'}</td>
                </tr>
                <tr>
                  <td>Sample Id</td>
                  <td>
                    {editNamespace ? (
                        <RoleConditional
                          requiredRoles={['editNamespace']}
                          fallbackComponent={namespace.sampleId}
                        >
                          <ReversibleField fieldName="sampleId" defaultValue={namespace.sampleId} handleChangeField={handleChangeField}>
                          <input type="text" />
                          </ReversibleField>
                        </RoleConditional>
                      ) : (
                        namespace.sampleId
                      )}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div className="row">
          <div className="col">
            <h2><i className="icon icon-common icon-cubes" /> Resources</h2>
          </div>
        </div>

        <div className="row">
          <div className="col overflow-y-scroll">
            {
              !namespace.resources && 'No resources' ||
                namespace.resources.map(resource =>
                  <ResourceItem
                    key={`resource-${resource.mirId}`}
                    resource={resource}
                  />
                )
            }
          </div>
        </div>
      </>
    );
  }
}


// Mapping
const mapStateToProps = (state) => {
  return ({
    namespaceList: state.registryBrowser.namespaceList
  })
};

const mapDispatchToProps = dispatch => ({
  getNamespaceFromRegistry: (prefix) => dispatch(getNamespaceFromRegistry(prefix)),
  getResourcesFromRegistry: (namespace) => dispatch(getResourcesFromRegistry(namespace))
});


export default connect (mapStateToProps, mapDispatchToProps)(NamespaceDetailsPage);

