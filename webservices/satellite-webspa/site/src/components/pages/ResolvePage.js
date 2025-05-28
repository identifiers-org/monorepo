import React from 'react';
import { connect } from 'react-redux';

// Actions.
import { getResolvedResources } from '../../actions/ResolvedResources';
import { getSchemaOrgMetadataFromRegistry, getSchemaOrgMetadataByPrefixFromRegistry } from '../../actions/SchemaOrgMetadata';

// Components.
import Spinner from '../common/Spinner';
import ResourceList from '../ResolverPage/ResourceList';
import MetadataView from "../ResolverPage/MetadataView";

// Utils.
import { querySplit } from '../../utils/identifiers';
import { useLocation } from 'react-router-dom';
import { useMatomo } from '@datapunt/matomo-tracker-react';
import {copyToClipboard} from "../../utils/copyToClipboard";

class ResolvePage extends React.Component {
  constructor(props) {
    super(props);

    const params = new URLSearchParams(props.location.search);

    this.state = {
      query: params.get('query') || '',
      isLoading: true
    };
  }

  componentDidMount = async () => {
    const {
      props: { getResolvedResources, getSchemaOrgMetadataByPrefixFromRegistry, schemaOrgMetadata },
      state: { query }
    } = this;
    const { trackPageView } = this.props.matomo;
    trackPageView();

    // Load and update schema.org metadata.
    const { prefix } = querySplit(query);
    if (!schemaOrgMetadata.dataset) {
      getSchemaOrgMetadataByPrefixFromRegistry(prefix);
    }

    await getResolvedResources(query);
    this.setState({isLoading: false});
  }

  async componentWillUnmount() {
    const { getSchemaOrgMetadataFromRegistry } = this.props;
    getSchemaOrgMetadataFromRegistry();
  }

  render() {
    const {
      props: { config },
      state: { isLoading, query }
    } = this;

    const compactIdentifier = `${config.resolverHardcodedUrl}/${query}`;

    return (
      <div className="row mb-5">
        <div className="col">
          {
            isLoading ? (
              <Spinner />
            ) : (
              <>
                <div className="row mb-5 justify-content-md-center">
                  <div className="col d-flex align-items-center text-middle justify-content-center border p-2 bg-cardgrey">
                    <h4 className="mb-0 text-success text-ellipsis">
                      {compactIdentifier}
                    </h4>
                      <button
                        className="btn btn-sm btn-primary-outline"
                        onClick={(e) => {copyToClipboard(compactIdentifier, e)}}
                        type="button"
                      >
                        <i className="icon icon-common icon-copy size-150" />
                      </button>
                  </div>
                </div>
                <div className="row">
                  <div className="col px-0">
                    <h4 className="mb-0">
                      <div className="inline-block border border-end-0 p-1 pe-2" style={{backgroundColor: "#00000008"}}>
                        Compact Identifier
                      </div>
                      <div className="inline-block border-bottom  py-1 px-2">
                        <strong className="text-primary fst-italic">{query}</strong>
                      </div>
                    </h4>
                  </div>
                </div>
                <div className="row g-0">
                  <ResourceList/>
                  <MetadataView curie={query}/>
                </div>
              </>
            )
          }
        </div>
      </div>
    );
  }
}


// Redux mappings.
const mapStateToProps = (state) => ({
  config: state.config,
  schemaOrgMetadata: state.schemaOrgMetadata
});

const mapDispatchToProps = dispatch => ({
  getResolvedResources: (query) => dispatch(getResolvedResources(query)),
  getSchemaOrgMetadataFromRegistry: (namespaceId) => dispatch(getSchemaOrgMetadataFromRegistry(namespaceId)),
  getSchemaOrgMetadataByPrefixFromRegistry: (namespaceId) => dispatch(getSchemaOrgMetadataByPrefixFromRegistry(namespaceId))
});

const ConnectedResolvePage = connect (mapStateToProps, mapDispatchToProps)(ResolvePage);
export default (props) => {
  const matomo = useMatomo();
  const location = useLocation();

  return <ConnectedResolvePage {...props} location={location} matomo={matomo} />
};
