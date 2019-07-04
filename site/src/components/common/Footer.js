import React from 'react';

import elixirKiteMark from '../../assets/elixir_kitemark-60px.png';


class Footer extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      // This is EMBL-EBI Enforced boilerplate footer.
      <>
        <div className="row footer-section align-items-center mx-5 mb-2 bg-light py-2">
          <div className="col col-xl-1">
            <img src={elixirKiteMark} />
          </div>
          <div className="col col-xl-11">
            <span>This service</span> is part of the ELIXIR infrastructure

            <p className="mb-0">
              <small>
                <span>Identifiers.org is an ELIXIR Interoperability Platform resource </span>
                <a href="https://elixir-europe.org/platforms/interoperability" target="_blank" className="text-primary">Learn more</a>
              </small>
            </p>
          </div>
        </div>
        <footer>
          <div id="global-footer" className="global-footer">
            <nav id="global-nav-expanded" className="global-nav-expanded row"></nav>
            <section id="ebi-footer-meta" className="ebi-footer-meta row"></section>
          </div>
        </footer>
      </>
    );
  }
}


export default Footer;
