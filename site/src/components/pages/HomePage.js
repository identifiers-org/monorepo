import React from 'react';

// Components.
import Search from '../HomePage/Search';


class HomePage extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className="row border">
        <div className="col col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-6 p-5">
          <h1 className="text-primary mb-4">Identifiers.org Resolution Services</h1>
          <div className="d-flex">
            <p className="mb-0 text-justify text-muted">
              Non incididunt mollit nostrud magna sunt consectetur consequat enim. Et laborum labore consectetur laborum
              cupidatat mollit ex dolore enim minim. Tempor mollit amet ex dolor nisi deserunt sint dolor quis. Proident
              excepteur excepteur velit id.
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


export default HomePage;
