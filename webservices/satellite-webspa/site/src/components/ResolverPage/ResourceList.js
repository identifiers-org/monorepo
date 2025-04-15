import React from 'react';
import { connect } from 'react-redux';
import { useLocation } from 'react-router-dom';

import ResourceItem from './ResourceItem';


class ResourceList extends React.Component {
  constructor(props) {
    super(props);
  }


  render() {
    const {
      resolvedResources,
      location
    } = this.props;

    const query = new URLSearchParams(location.search).get('query');

    return (
      resolvedResources.length === 0 ? (
        <div className="col-12 col-lg px-0 pe-md-2">
          <div className="row mb-5">
            <div className="col align-middle">
              <i className="icon icon-common icon-bomb size-200 me-2 mt-4" />
              Error resolving query: {query ? `nothing found for compact identifier \"${query}\"` : "Query is empty."}
            </div>
          </div>
          <div className="row">
            <div className="col">
              <a
                href={`${window.location.protocol}//${window.location.hostname}${window.location.port ? ':' + window.location.port : ''}`}
                className="text-decoration-none"
              >
                <i className="icon icon-common icon-arrow-left me-2" />Go back
              </a>
            </div>
          </div>
        </div>
      ) : (
        <div className="col-12 col-lg px-0 pe-md-2">
          <small className="text-muted">
            Found {resolvedResources.length} {resolvedResources.length === 1 ? 'entry' : 'entries'}.
          </small>
          {resolvedResources.map((rr, index) => <ResourceItem key={`rr-${index}`} data={rr} />)}
        </div>
      )
    );
  }
}


// Redux mappings
const mapStateToProps = (state) => ({
  resolvedResources: state.resolvedResources
});

const ConnectedResourceList = connect(mapStateToProps)(ResourceList);
export default (props) => {
  const location = useLocation();
  return <ConnectedResourceList {...props} location={location} />;
}
