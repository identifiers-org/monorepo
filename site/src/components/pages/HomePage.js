import React from 'react';
import { connect } from 'react-redux';

// Components.
import Search from '../HomePage/Search';

// Utils.
import { isSmallScreen, isIpadScreen } from '../../utils/responsive';


class HomePage extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { config } = this.props;

    const boxPadding = isSmallScreen() || isIpadScreen() ? 'p-3' : 'p-5';
    const searchBarPadding = isSmallScreen() ? 'p-1' : 'px-5';
    const topSpacer = isSmallScreen() ? '' : 'spacer-8';

    return (
      <div className={`row border ${topSpacer}`}>
        <div className={`col col-12 col-xl-6 ${boxPadding}`}>
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

        <div className="col col-12 col-xl-6 bg-light">
          <h4 className={`mt-3 ${boxPadding}`}><i className="icon icon-common icon-search mr-2" />Resolve a Compact Identifier</h4>
          <div className={`row justify-content-center mt-2 ${searchBarPadding}`}>
            <div className="col col-lg-7 col-xl-12">
              <Search
                button={true}
                buttonCaption={<span><i className="icon icon-common icon-search mr-1" /> Resolve</span>}
                floatingGoToButton={false}
                placeholderCaption="Enter an identifier to resolve"
              />
            </div>
          </div>
        </div>
      </div>
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