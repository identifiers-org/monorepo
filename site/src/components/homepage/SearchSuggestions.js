import React from 'react';
import { connect } from 'react-redux';

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

  handleSuggestionLinkClick = (prefix) => window.open(`${this.props.config.registryUrl}/registry/${prefix}`, '_blank');


  render() {
    const {
      handleSuggestionLinkClick,
      highlightQuery,
      props: {
        config,
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
            <div className="suggestions-box pb-2">
              <div className="row mx-1">
                <div className="col align-self-end">
                  <p className="text-muted text-right my-0"><small>Suggestions</small></p>
                </div>
              </div>

              <ul className="suggestion-list">
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
                    <li
                      key={`suggestion-${index}`}
                      onMouseOver={() => {mouseOver(index)}}
                      className={`suggestion ${suggestionBgColor}`}
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
                            <span className="badge badge-secondary font-weight-normal">
                              {highlightQuery(result.prefix, queryParts.prefix)}
                            </span>
                            <p className="mb-0 ml-2">{result.name}</p>
                          </a>
                        </div>
                        <div className="col col-1">
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
                  );
                })
              }
            </ul>
            </div>
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
