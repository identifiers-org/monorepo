import React from 'react';

import identifiersLogo from '../../assets/identifiers_logo.png';
import Search from '../HomePage/Search';


class MainPage extends React.Component {
  constructor(props) {
    super(props);

    const params = new URLSearchParams(props.location.search);

    this.state = {
      query: params.get('query') || ''
    };
  }


  handleClickPrefixRegistrationRequestForm = () => {this.props.history.push('/prefixregistrationrequest')};
  handleClickRegistryBrowser = () => {this.props.history.push('/registry')};


  render() {
    const {
      handleClickPrefixRegistrationRequestForm,
      handleClickRegistryBrowser,
      state: { query }
    } = this;

    return (
      <div className="row border spacer-8">
        <div className="col col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-6 p-5">
          <h1 className="text-primary mb-4">Identifiers.org Central Registry</h1>
          <div className="d-flex">
            <p className="mb-0 text-justify text-muted">
              The <span className="text-dark">Identifiers.org</span> Central Registry service provides a centralized
              directory of Compact Identifiers. This website allows performing searches on the registry by using the
              search bar on the right side or the <a
                href="#!"
                className="text-primary"
                onClick={handleClickRegistryBrowser}
              >
                Registry Browser
              </a>. Resource maintainers can also find the <a
                href="#!"
                className="text-primary"
                onClick={handleClickPrefixRegistrationRequestForm}
              >
                Prefix Registration Request form
              </a> to request a prefix in Identifiers.org for their databases or services.
            </p>
          </div>
        </div>

        <div className="col col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-6 p-5 bg-light">
          <h4 className="mt-3 mb-5"><i className="icon icon-common icon-search mr-2" />Search the registry</h4>
          <div className="row justify-content-center mt-2">
            <div className="col col-lg-7 col-xl-12">
              <Search query={query} />
            </div>
          </div>
        </div>
      </div>
    );
  }
}


export default MainPage;
