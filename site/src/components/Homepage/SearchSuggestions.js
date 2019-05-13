import React from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faLeaf } from '@fortawesome/free-solid-svg-icons';
import { Config } from '../../config/config';


const highlightQuery = (prefix, query) => {
  const parts = prefix.split(query);
  return parts.reduce(
    (sum, part, index) =>
      [...sum, part, <strong key={`${prefix}-${index}`} className="text-warning">{query}</strong>], []).slice(0, -1);
}

const SearchSuggestions = (props) => {
  if (props.searchSuggestionList.length === 0 || props.query === '') {
    return '';
  }

  return (
    <ul className="suggestion-list py-2">
      {
        props.searchSuggestionList.map((result, index) => (
          <a
            className="clear-link"
            href={`${Config.baseUrl}registry/${result.prefix}`}
            id={result.prefix}
            key={result.prefix}
          >
            <li
              onMouseOver={() => {props.mouseover(index)}}
              className={`suggestion ${props.selectedSearchSuggestion === index ? 'suggestion__active' : ''}`}
              onClick={props.onClick}
            >
              <div className="row no-gutters py-1 mx-2">
                <div className="col d-flex align-items-center">
                  <span className="badge badge-secondary font-weight-normal">{highlightQuery(result.prefix, props.query)}</span>
                  <p className="mb-0 ml-2">{result.name}</p>
                </div>
              </div>
            </li>
          </a>
        ))
      }
    </ul>
  );
}


export default SearchSuggestions;
