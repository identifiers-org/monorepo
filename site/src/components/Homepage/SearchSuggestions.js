import React from 'react';

import { Config } from '../../config/config';


const highlightQuery = (prefix, query) => {
    const parts = prefix.split(query);
    let result;
    let complete = false;

    if (prefix === query) {
      result = <strong>{query}</strong>;
      complete = true;
    } else {
      result = parts.reduce(
        (sum, part, index) =>
          [...sum, part, <strong key={`${prefix}-${index}`} className="text-warning">{query}</strong>], []).slice(0, -1);
    }

    return <span
      className={`badge ${complete ? 'badge-secondary border border-dark' : 'badge-dark border border-secondary'} font-weight-normal`}
    >
      {result}
    </span>
  }

const SearchSuggestions = (props) => {
  const {
    mouseOver,
    onClick,
    query,
    searchSuggestionList,
    selectedSearchSuggestion
  } = props;

  if (searchSuggestionList.length === 0 || query === '') {
    return '';
  }

  return (
    <ul className="suggestion-list py-2">
      {
        searchSuggestionList.map((result, index) => (
          <a
            className="clear-link"
            href={`${Config.baseUrl}registry/${result.prefix}`}
            id={result.prefix}
            key={result.prefix}
          >
            <li
              onMouseOver={() => {mouseOver(index)}}
              className={`suggestion ${selectedSearchSuggestion === index ? 'suggestion__active' : ''}`}
              onClick={onClick}
            >
              <div className="row no-gutters py-1 mx-2">
                <div className="col d-flex align-items-center">
                  <span className="font-weight-normal">{highlightQuery(result.prefix, query)}</span>
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
