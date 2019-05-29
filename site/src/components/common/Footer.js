import React from 'react';
import { NavLink } from 'react-router-dom';


class Footer extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      // This is EMBL-EBI Enforced boilerplate footer.
      <footer>
        <div id="global-footer" className="global-footer">
          <nav id="global-nav-expanded" className="global-nav-expanded row"></nav>
          <section id="ebi-footer-meta" className="ebi-footer-meta row"></section>
        </div>
      </footer>
    );
  }
}


export default Footer;
