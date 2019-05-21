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
    <div className="card-header d-flex align-items-center">
          <FontAwesomeIcon icon={faCubes} size="2x" className="mr-2" />
          <a
            href={data.compactIdentifierResolvedUrl}
            className="ml-2 clear-link"
            target="_blank"
          >
            <p className="mb-0">{data.description}</p>
            <small className="text-muted">{data.compactIdentifier}</small>
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