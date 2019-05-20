import React from 'react';
import { connect } from 'react-redux';

import { getResolvedResources } from '../../actions/ResolvedResources';
import ResourceList from '../resolverpage/ResourceList';


class ResolvePage extends React.Component {
  constructor(props) {
    super(props);

    const params = new URLSearchParams(props.location.search);

    this.state = {
      query: params.get('query') || ''
    };
  }

  componentDidMount() {
    const {
      props: { getResolvedResources },
      state: { query }
    } = this;

    getResolvedResources(query);
  }


  render() {
    return (
      <>
      <div className="row mb-5">
        <div className="col">
          <ResourceList />
        </div>
      </div>
      </>
    );
  }
}


// Redux mappings.
const mapDispatchToProps = dispatch => ({
  getResolvedResources: (query) => dispatch(getResolvedResources(query))
});

export default connect (undefined, mapDispatchToProps)(ResolvePage);
