import React from 'react';
import { connect } from 'react-redux';

import { getResolvedResources } from '../../actions/ResolvedResources';
import ResourceList from '../resolverpage/ResourceList';

import Spinner from '../common/Spinner';


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
      props: { getResolvedResources },
      state: { query }
    } = this;

    await getResolvedResources(query)
    console.log('done loading!');
    this.setState({isLoading: false});
  }


  render() {
    const { isLoading } = this.state;

    return (
      <>
      <div className="row mb-5">
        <div className="col">
          {
            isLoading ? (
              <Spinner />
            ) : (
              <ResourceList />
            )
          }
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
