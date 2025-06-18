import React from 'react';
import { config } from '../../config/Config'

// Components.
import Search from '../HomePage/Search';

// Utils.
import { isSmallScreen, isIpadScreen } from '../../utils/responsive';

import { useMatomo } from '@datapunt/matomo-tracker-react';
import { useNavigate } from "react-router-dom";
import ReverseResolution from "../HomePage/ReverseSearch";


class HomePage extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    const { trackPageView } = this.props.matomo;
    trackPageView();
  }

  render() {
    const boxPadding = isSmallScreen() || isIpadScreen() ? 'p-3' : 'p-5';
    const searchBarPadding = isSmallScreen() ? 'p-1' : 'px-5';
    const topSpacer = isSmallScreen() ? '' : 'spacer-5';
    const { navigate } = this.props;

    return (
      <div className={`row border ${topSpacer}`}>
        <div className={`col col-12 col-xl-6 ${boxPadding} d-flex align-items-center justify-content-center flex-column`}>
          <h1 className="text-primary mb-4 text-left w-100">Identifiers.org Resolution Service</h1>
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
            <div className="alert alert-info mt-3">
              <h4 className="text-info-emphasis">
                <i className="icon icon-common icon-bullhorn me-2"></i>
                We need your help!
              </h4>
              <p className="mb-0 text-justify text-muted">
                Has identifiers.org saved you time or effort?
                Please take 15 minutes to fill in a survey and help EMBL-EBI make the case for why open data
                resources are critical to life science research.
                <a href="https://www.surveymonkey.com/r/QGFMBH8"> Click here to fill in the survey </a>
              </p>
            </div>
        </div>

        <div className="col col-12 col-xl-6 bg-light py-5 d-flex align-items-center justify-content-center">
          <div className={`row justify-content-center mt-2 ${searchBarPadding} w-100`}>
            <div className="col col-lg-7 col-xl-12">
              <h4><i className="icon icon-common icon-search pe-2" />Resolve a Compact Identifier</h4>
              <Search
                buttonCaption={<span><i className="icon icon-common icon-search me-1" /> Resolve</span>}
                placeholderCaption="Enter an identifier to resolve"
                onButtonClick={(query) => navigate(`resolve?query=${query}`)}
              />
              <hr className="my-4"/>
              <h4><i className="icon icon-common icon-search pe-2" />
                Convert provider URL to identifiers.org URL
                <i className="icon icon-common icon-beta text-primary fs-2 ms-2"></i>
              </h4>
              <ReverseResolution />
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default (props) => {
  const matomo = useMatomo();
  const navigate = useNavigate();
  return <HomePage  {...props} matomo={matomo} navigate={navigate} />
};