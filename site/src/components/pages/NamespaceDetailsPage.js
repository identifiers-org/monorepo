import React from 'react';
import { connect } from 'react-redux';

// Actions.
import { getNamespaceFromRegistry, getResourcesFromRegistry } from '../../actions/NamespaceList';
import { setNamespacePatch, setNamespacePatchField, patchNamespace } from '../../actions/NamespacePatch';

// Components.
import PageTitle from '../common/PageTitle';
import ResourceItem from '../NamespaceDetailsPage/ResourceItem';
import ReversibleField from '../common/ReversibleField';
import RoleConditional from '../common/RoleConditional';

// Config.
import { config } from '../../config/Config';

// Utils.
import { swalConfirmation, failureToast, successToast, infoToast } from '../../utils/swalDialogs';


class NamespaceDetailsPage extends React.Component {
  constructor (props) {
    super(props);

    const params = new URLSearchParams(props.location.search);

    this.state = {
      editNamespace: params.get('editNamespace') === 'true' || false,
      namespaceId: undefined,
      namespaceFieldsChanged: new Set(),
      newNamespace: this.props.newNamespace
    }
  }


  async componentDidMount() {
    await this.props.getNamespaceFromRegistry(this.props.match.params.prefix);
    await this.props.getResourcesFromRegistry(this.props.namespaceList[0]);

    // Prepares model for namespace patching.
    const {resources, _links, ...newNamespace} = this.props.namespaceList[0];
    const namespaceId = _links.self.href.split('/').pop();

    this.setState({namespaceId, newNamespace});
    this.props.setNamespacePatch(namespaceId, newNamespace);
  }


  //
  // Field manipulation handler. Undefined 'value' field will revert to default.
  //
  handleChangeField = (field, value) => {
    const namespace = this.props.namespaceList[0];

    // Add/remove field to changed list if modified/reset, so it is validated or not when clicked on the perform
    // validation button.
    if (namespace[field] !== value) {
      this.setState(prevState => ({
        namespaceFieldsChanged: prevState.namespaceFieldsChanged.add(field),
        newNamespace: {...this.state.newNamespace, [field]: value}
      }));
    } else {
      this.setState(prevState => {
        prevState.namespaceFieldsChanged.delete(field);
        return {
          namespaceFieldsChanged: prevState.namespaceFieldsChanged,
          newNamespace: {...this.state.newNamespace, [field]: namespace[field]}
        };
      });
    }

    this.props.setNamespacePatchField(field, value);
  }


  handleClickCommitChangesButton = async () => {
    const {
      state: { namespaceId, namespaceFieldsChanged, newNamespace },
      props: { patchNamespace }
    } = this;

    if (namespaceFieldsChanged.size === 0) {
      infoToast('No changes to commit');
      return;
    }

    const result = await swalConfirmation.fire({
      title: 'Confirm changes to namespace',
      text: `Changed fields: ${[...namespaceFieldsChanged].join(', ')}`,
      confirmButtonText: 'Commit changes',
      cancelButtonText: 'Cancel'
    });

    if (result.value) {
      const result = await patchNamespace(namespaceId, newNamespace);

      if (result.status === 200) {
        successToast('Changed committed successfully');
        await this.props.getNamespaceFromRegistry(this.props.match.params.prefix);
        await this.props.getResourcesFromRegistry(this.props.namespaceList[0]);
        this.setState({editNamespace: false, namespaceFieldsChanged: new Set()});
      } else {
        failureToast('Error committing changes');
      }
    }
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
    const sampleUrl = namespace ? `${config.satelliteUrl}/${namespace.prefix}:${namespace.sampleId}` : '';

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
          <div className="row mb-3">
            <div className="col">
              <RoleConditional
                requiredRoles={['editNamespace']}
              >
                <>
                  <button
                    className="btn btn-sm btn-warning edit-button mr-2"
                    onClick={handleClickValidateChangesButton}
                  >
                    <i className="icon icon-common icon-tasks mr-1" />Perform validation
                  </button>
                  <button
                    className="btn btn-sm btn-success edit-button mr-2"
                    onClick={handleClickCommitChangesButton}
                  >
                    <i className="icon icon-common icon-check" /> Commit changes
                  </button>
                  <button
                    className="btn btn-sm btn-danger edit-button"
                    onClick={handleClickDiscardChangesButton}
                  >
                    <i className="icon icon-common icon-times" /> Discard changes
                  </button>
                </>
              </RoleConditional>
            </div>
          </div>
        ) : (
          <div className="row mb-3">
            <div className="col">
              <RoleConditional
                requiredRoles={['editNamespace']}
                >
                <button
                  className="btn btn-sm btn-success edit-button"
                  onClick={handleClickEditButton}
                  >
                  <i className="icon icon-common icon-edit mr-1"/>Edit namespace
                </button>
              </RoleConditional>
            </div>
          </div>
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
                  <td>Registry URI</td>
                  <td>{config.baseUrl}registry/{namespace.prefix}</td>
                </tr>
                <tr>
                  <td>Sample URL</td>
                  <td><a href={sampleUrl} target="_blank" rel="noopener noreferrer">{sampleUrl}</a></td>
                </tr>
                <tr>
                  <td>Sample Compact identifier</td>
                  <td>{namespace.prefix}:{namespace.sampleId}</td>
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

        {
          !namespace.resources && (
            <div className="row">
              <div className="col">
                <p>No resources.</p>
              </div>
            </div>
          ) || (
            namespace.resources.map(resource =>
              <ResourceItem
                key={`resource-${resource.mirId}`}
                namespace={namespace}
                resource={resource}
              />
            )
          )
        }
      </>
    );
  }
}


// Mapping
const mapStateToProps = (state) => {
  return ({
    namespaceList: state.registryBrowser.namespaceList,
    newNamespace: state.curatorEditNamespace
  })
};

const mapDispatchToProps = dispatch => ({
  getNamespaceFromRegistry: (prefix) => dispatch(getNamespaceFromRegistry(prefix)),
  getResourcesFromRegistry: (namespace) => dispatch(getResourcesFromRegistry(namespace)),
  setNamespacePatch: (id, namespace) => dispatch(setNamespacePatch(id, namespace)),
  setNamespacePatchField: (field, value) => dispatch(setNamespacePatchField(field, value)),
  patchNamespace: (id, newNamespace) => dispatch(patchNamespace(id, newNamespace))
});


export default connect (mapStateToProps, mapDispatchToProps)(NamespaceDetailsPage);
