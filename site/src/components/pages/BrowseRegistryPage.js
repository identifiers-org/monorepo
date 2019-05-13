import React from 'react';

import { faDatabase } from '@fortawesome/free-solid-svg-icons';

import NamespaceList from '../BrowseRegistryPage/NamespaceList';
import PageTitle from '../common/PageTitle';


class BrowseRegistryPage extends React.Component {
  constructor (props) {
    super(props);

    const params = new URLSearchParams(props.location.search);

    this.state = {
      query: params.get('query') || ''
    };
  }

  render() {
    const { query } = this.state;

    return (
      <>
        <PageTitle
          icon={faDatabase}
          title="Registry"
          description="This is the list of Prefixes currently registered in Identifiers.org database.
                       Clicking on one will show additional details and the related providers."
        />

        <div>
          <NamespaceList query={query} />
        </div>
      </>
    );
  }
}


export default BrowseRegistryPage;