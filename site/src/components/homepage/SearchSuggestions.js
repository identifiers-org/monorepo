import React from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faExternalLinkAlt } from '@fortawesome/free-solid-svg-icons';

import SearchEvaluator from './SearchEvaluator';


class SearchSuggestions extends React.Component {
  constructor(props) {
    super(props);
  }


  highlightQuery = (prefix, query) => {
    const parts = prefix.split(query);
    return parts.reduce(
      (sum, part, index) =>
        [...sum, part, <strong key={`${prefix}-${index}`} className="text-warning">{query}</strong>], []).slice(0, -1);
  }

  handleSuggestionLinkClick = (prefix) => window.open(`${config.registryUrl}/registry/${prefix}`, '_blank');


  render() {
    const {
      handleSuggestionLinkClick,
      highlightQuery,
      props: {
        handleClick,
        mouseOver,
        queryParts,
        searchSuggestionList,
        selectedSearchSuggestion
      }
    } = this;

    // No suggestion list & no query: don't render anything!
    if (searchSuggestionList.length === 0 && queryParts.resource === '' && queryParts.prefix === '' && queryParts.id === '') {
      return '';
    }

    return (
      <>
        {
          (queryParts.prefix || queryParts.id || queryParts.resource) && (
            <div className="hints-box">
              <div className="row mx-1">
                <div className="col align-self-end">
                  <p className="text-muted text-right my-0"><small>Your query</small></p>
                </div>
              </div>
              <SearchEvaluator queryParts={queryParts} />
            </div>
          )
        }

        {
          // Render search suggestion list if it contains elements.
          searchSuggestionList.length > 0 && (
            <>
              <div className="row mx-1">
                <div className="col align-self-end">
                  <p className="text-muted text-right my-0"><small>Suggestions</small></p>
                </div>
              </div>
              {
                searchSuggestionList.map((result, index) => {
                  // Background color for the suggestion:
                  // If result.prefix = queryParts.prefix, highlight as active, and then,
                  // if index = selectedSearchSuggestion, and bg is still white, highlight as selected.
                  let suggestionBgColor = result.prefix === queryParts.prefix ? 'suggestion__active' : '';
                  if (selectedSearchSuggestion === index && suggestionBgColor === '') {
                    suggestionBgColor = 'suggestion__selected';
                  }

                  return (
                    <a
                      className="clear-link"
                      href="#!"
                      id={result.prefix}
                      key={result.prefix}
                      onClick={() => {handleClick(result.prefix)}}
                    >
                      <li
                        onMouseOver={() => {mouseOver(index)}}
                        className={`suggestion ${suggestionBgColor}`}
                      >
                        <div className="row no-gutters py-1 mx-2">
                          <div className="col d-flex align-items-center">
                            <span className="badge badge-secondary font-weight-normal">
                              {highlightQuery(result.prefix, queryParts.prefix)}
                            </span>
                            <p className="mb-0 ml-2">{result.name}</p>
                            <button
                              className="ml-auto clear-button"
                              onClick={() => {handleSuggestionLinkClick(result.prefix)}}
                              type="button"
                            >
                              <FontAwesomeIcon icon={faExternalLinkAlt}/>
                            </button>
                          </div>
                        </div>
                      </li>
                    </a>
                  );
                })
              }
            </>
          )
        }
      </>
    );
  }
}

// Redux mappings.
const mapStateToProps = (state) => ({
  config: state.config
});

export default connect(mapStateToProps)(SearchSuggestions);
