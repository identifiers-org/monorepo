import React from 'react';
import { connect } from 'react-redux';

// Assets.
import identifiersLogo from '../../assets/identifiers_logo.png';

// Components.
import Search from '../HomePage/Search';


class HomePage extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { config } = this.props;

    return (
      <>
        <div className="row justify-content-center">
          <div className="col col-5">
            <div className="logo">
              <img src={identifiersLogo} />
              <div className="logo-text">
                <h1>Identifiers.org</h1>
                <p className="logo-subtitle">Central registry</p>
              </div>
            </div>
          </div>
        </div>
        <div className="row justify-content-center mt-2">
          <div className="col col-xs-12 col-sm-11 col-md-9 col-lg-7 col-xl-6">
            <Search query />
          </div>
        </div>
        <div className="mt-5 d-flex align-items-center">
          <i className="icon icon-common icon-info size-400 text-primary mr-4" />
          <h4 className="mb-0 text-justify">
            The Identifiers.org Resolution Service provides consistent access to life science data using Compact
            Identifiers. Compact Identifiers consist of an assigned unique prefix and a local provider designated
            accession number (prefix:accession). The resolving location of Compact Identifiers is determined using
            information that is stored in the Identifiers.org <a
                href={config.registryUrl}
                className="text-primary"
              >
                Registry
              </a>.
          </h4>
        </div>
      </>
    );
  }
}

//
// Redux mappings.
//
const mapStateToProps = (state) => ({
  config: state.config
});

export default connect(mapStateToProps)(HomePage);