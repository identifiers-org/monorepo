import React from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faCubes,
  faExternalLinkAlt,
  faLink,
  faGlobeEurope,
  faSitemap
} from '@fortawesome/free-solid-svg-icons';


const ResourceItem = ({ data }) => (
  <div className="card mb-5">
    <div className="card-header">
      <FontAwesomeIcon icon={faCubes} className="min-width-2" />
      <a
        href={data.compactIdentifierResolvedUrl}
        className="clear-link ml-2"
        target="_blank"
      >
        {data.description}
        <FontAwesomeIcon icon={faExternalLinkAlt} className="ml-2" />
      </a>
    </div>
    <ul className="list-group list-group-flush">
      <li className="list-group-item">
        <p className="mb-0">
          <FontAwesomeIcon icon={faLink} className="min-width-2" />
          <span className="text-success">
            {data.compactIdentifierResolvedUrl}
          </span>
        </p>
        <p className="mb-0">
          <FontAwesomeIcon icon={faSitemap} className="min-width-2" />
          <span className="font-weight-bold">
            {data.institution.name}
          </span>
        </p>
        <p className="mb-0">
          <FontAwesomeIcon icon={faGlobeEurope} className="min-width-2" />
          <span className="mb-0">
            {data.location.countryName}
          </span>
        </p>
      </li>
    </ul>
  </div>
);


export default ResourceItem;