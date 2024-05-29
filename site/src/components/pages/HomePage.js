import React from 'react';
import { connect } from 'react-redux';

// Components.
import Search from '../HomePage/Search';

// Utils.
import { isSmallScreen, isIpadScreen } from '../../utils/responsive';

import { useMatomo } from '@datapunt/matomo-tracker-react';


class HomePage extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    const { trackPageView } = this.props.matomo;
    trackPageView();
  }

  render() {
    const { config } = this.props;

    const boxPadding = isSmallScreen() || isIpadScreen() ? 'p-3' : 'p-5';
    const searchBarPadding = isSmallScreen() ? 'p-1' : 'px-5';
    // const topSpacer = isSmallScreen() ? '' : 'spacer-8';

    return (
      <>
        <div className="notifications-js row margin-top-medium w-100">
          <div className="callout warning w-100">
            Do data resources managed by EMBL-EBI and our collaborators make a difference to your work? <br/>
            Please take 10 minutes to fill in our annual user survey, and help us make the case for why sustaining open data resources is critical for life sciences research. <br />
            Survey link: <a href="https://www.surveymonkey.com/r/HJKYKTT?channel=identifiersorg">https://www.surveymonkey.com/r/HJKYKTT?channel=identifiersorg</a>
          </div>
        </div>
        <div className={`row border`}>
          <div className={`col col-12 col-xl-6 ${boxPadding}`}>
            <h1 className="text-primary mb-4">Identifiers.org Resolution Service</h1>
            <div className="d-flex">
              <p className="mb-0 text-justify text-muted">
                The Identifiers.org Resolution Service provides consistent access to life science data using Compact
                Identifiers. Compact Identifiers consist of an assigned unique prefix and a local provider designated
                accession number (prefix:accession). The resolving location of Compact Identifiers is determined using
                information that is stored in the Identifiers.org <a
                href={config.registryUrl}
                className="text-primary"
              >
                Registry
              </a>.
              </p>
            </div>
          </div>

          <div className="col col-12 col-xl-6 bg-light">
            <h4 className={`mt-3 ${boxPadding}`}><i className="icon icon-common icon-search mr-2"/>Resolve a Compact
              Identifier</h4>
            <div className={`row justify-content-center mt-2 ${searchBarPadding}`}>
              <div className="col col-lg-7 col-xl-12">
                <Search
                  button={true}
                  buttonCaption={<span><i className="icon icon-common icon-search mr-1"/> Resolve</span>}
                  floatingGoToButton={false}
                  placeholderCaption="Enter an identifier to resolve"
                />
              </div>
            </div>
          </div>
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

const ConnectedHomePage = connect(mapStateToProps)(HomePage)
export default (props) => {
  const matomo = useMatomo();
  return <ConnectedHomePage  {...props} matomo={matomo}/>
};