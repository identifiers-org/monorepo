import React from 'react';

import { config } from '../../config/Config';


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
    <div className="inline-search-container">
      <div className="suggestions-box">
        <div className="row mx-1">
          <div className="col align-self-end">
            <p className="text-muted text-right my-0"><small>Suggestions</small></p>
          </div>
        </div>

        <ul className="suggestion-list pb-2">
                {
                  searchSuggestionList.map((result, index) => {
                  return (
                    <li
                      key={`suggestion-${index}`}
                      onMouseOver={() => {mouseOver(index)}}
                      className={`suggestion ${selectedSearchSuggestion === index ? 'suggestion__selected' : ''}`}
                    >
                      <div className="row no-gutters py-1 mx-2">
                        <div className="col col-11">
                         <a
                            className="clear-link d-flex align-items-center"
                            href="#!"
                            id={result.prefix}
                            key={result.prefix}
                            onClick={() => {handleClick(result.prefix)}}
                          >
                            {highlightQuery(result.prefix, query)}
                            <p
                              className={`mb-0 ml-2 ${selectedSearchSuggestion === index ? 'text-white' : ''}`}
                            >
                              {result.name}
                            </p>
                          </a>
                        </div>
                        <div className="col col-1">
                          <button
                            className={`ml-auto clear-button ${selectedSearchSuggestion === index ? 'text-white' : ''}`}
                            onClick={() => {handleSuggestionLinkClick(result.prefix)}}
                            type="button"
                          >
                            <i className="icon icon-common icon-external-link-alt" />
                          </button>
                        </div>
                      </div>
                    </li>
                  );
                })
              }
            </ul>
      </div>
    </div>
  );
}


export default SearchSuggestions;
