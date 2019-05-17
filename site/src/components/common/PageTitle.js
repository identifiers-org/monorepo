import React from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';


const PageTitle = ({ icon, title, extraTitle, description }) => (
  <>
    <div className="row">
      <div className="col">
        <h1>
          <FontAwesomeIcon icon={icon} /> {title}&nbsp;
          <span className="font-italic">{extraTitle}</span>
        </h1>
      </div>
    </div>
    <div className="row">
      <div className="col">
        <p>{description}</p>
      </div>
    </div>
  </>
);


export default PageTitle;