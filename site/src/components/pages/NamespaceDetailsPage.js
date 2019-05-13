import React from 'react';
import { connect } from 'react-redux';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import {
  faLeaf,
  faInfoCircle,
  faFingerprint,
  faCubes
} from '@fortawesome/free-solid-svg-icons';

import PageTitle from '../common/PageTitle';
import { getNamespaceFromRegistry, getResourcesFromRegistry } from '../../actions/NamespaceList';
import ResourceItem from '../NamespaceDetailsPage/ResourceItem';

import { Config } from '../../config/config';


class NamespaceDetailsPage extends React.Component {
  constructor (props) {
    super(props);
  }


  async componentDidMount() {
    await this.props.getNamespaceFromRegistry(this.props.match.params.prefix);
    await this.props.getResourcesFromRegistry(this.props.namespaceList[0]);
  }


  render() {
    const namespace = this.props.namespaceList[0];

    return !namespace ? (
      '' // Placeholder for empty/not found.
    ) : (
      <>
        <PageTitle
          icon={faLeaf}
          title={`Data collection:`}
          extraTitle={namespace.name}
        />

        <div className="row">
          <div className="col">
            <h2><FontAwesomeIcon icon={faInfoCircle} /> General Information</h2>
          </div>
        </div>

        <div className="row mb-3">
          <div className="col">
            <table className="table table-sm table-striped">
              <tbody>
                <tr>
                  <td className="w-35">
                    Recommended Name
                  </td>
                  <td>
                    {namespace.name}
                  </td>
                </tr>
                <tr>
                  <td>Description</td>
                  <td>{namespace.description}</td>
                </tr>
                <tr>
                  <td>Identifier pattern</td>
                  <td>{namespace.pattern}</td>
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
            <h2><FontAwesomeIcon icon={faFingerprint} /> Identification schemes</h2>
          </div>
        </div>

        <div className="row mb-3">
          <div className="col">
            <table className="table table-sm table-striped">
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
                  <td>{Config.baseUrl}registry/{namespace.prefix}</td>
                </tr>
                <tr>
                  <td>Compact identifier</td>
                  <td>{namespace.prefix}:{'{'}accession number{'}'}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div className="row">
          <div className="col">
            <h2><FontAwesomeIcon icon={faCubes} /> Resources</h2>
          </div>
        </div>

        <div className="row">
          <div className="col">
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

