import React from 'react';
import { connect } from 'react-redux';

// Components.
import Search from '../HomePage/Search';


class HomePage extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { config } = this.props;

    return (
      <div className="row border">
        <div className="col col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-6 p-5">
          <h1 className="text-primary mb-4">Identifiers.org Resolution Service</h1>
          <div className="d-flex">
            <p className="mb-0 text-justify text-muted">
            The Identifiers.org Resolution Service provides consistent access to life science data using Compact
            Identifiers. Compact Identifiers consist of an assigned unique prefix and a local provider designated
            accession number (prefix:accession). The resolving location of Compact Identifiers is determined using
            information that is stored in the Identifiers.org  <a
                href={config.registryUrl}
                className="text-primary"
              >
                Registry
              </a>.
            </p>
          </div>
        </div>

        <div className="col col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-6 p-5 bg-light">
          <h4 className="mt-3 mb-5"><i className="icon icon-common icon-search mr-2" />Resolve a Compact Identifier</h4>
          <div className="row justify-content-center mt-2">
            <div className="col col-lg-7 col-xl-12">
              <Search />
            </div>
          </div>
        </div>
      </div>
    );
  }
}


const mapStateToProps = (state) => ({
  config: state.config,
});

export default connect(mapStateToProps)(HomePage);