import React from 'react';
import SearchSuggestions from './SearchSuggestions';

import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';

import { getNamespacesFromRegistry } from '../../actions/NamespaceList';
import { querySplit, completeQuery, evaluateSearch } from '../../utils/identifiers';


class Search extends React.Component {
  constructor(props) {
    super(props);
    this.search = React.createRef();

    this.state = {
      query: '',
      queryParts: {
        resource: undefined,
        prefix: undefined,
        id: undefined,
        bad: []
      },
      activeSuggestion: -1
    }
  }

  componentDidMount() {
    this.updateNamespaceList();
  }


  updateNamespaceList = async () => {
    const {
      props: { getNamespacesFromRegistry },
      state: { queryParts: { prefix } }
    } = this;

    // set active suggestion to -1.
    this.setState({activeSuggestion: -1});

    await getNamespacesFromRegistry(prefix);
  }


  handleChange = () => {
    const { updateNamespaceList } = this;

    this.setState({
      query: this.search.value,
      queryParts: querySplit(this.search.value)
    }, () => {
      updateNamespaceList();
    });
  }

  handleKeyDown = e => {
    const {
      handleChange,
      handleSearch,
      props: { namespaceList },
      state: { activeSuggestion, query, queryParts }
    } = this;

    switch (e.keyCode) {
    case 13: {  // Enter key
      e.preventDefault();

      if (activeSuggestion === -1) {
        handleSearch();
      } else {
        e.currentTarget.value = completeQuery(queryParts.resource, namespaceList[activeSuggestion].prefix, queryParts.id);
        handleChange();
        break;
      }
    }

    case 38: {  // Up key
      if (this.state.activeSuggestion > -1) {
        this.setState({activeSuggestion: activeSuggestion - 1});
      }
      break;
    }

    case 40: {  // Down key
      if (activeSuggestion < namespaceList.length - 1) {
        this.setState({activeSuggestion: activeSuggestion + 1});
      }
      break;
    }
    }
  }

  handleMouseOver = index => {
    this.setState({activeSuggestion: index});
  }

  handleClick = (query) => {
    const { queryParts } = this.state;

    this.setState({query: completeQuery(queryParts.resource, query, queryParts.id)}, () => {this.handleChange()});
  }

  handleSubmit = e => {
    const {query} = this.state;

    e.preventDefault();
    this.handleSearch();
  }

  handleSearch = () => {
    const {
      props: { config, history, namespaceList },
      state: { query, queryParts }
    } = this;

    const evaluation = evaluateSearch(queryParts, namespaceList, config.enableResourcePrediction);

    if (evaluation !== 'ok') {
      return;
    }


    history.push(`resolve?query=${query}`);
  }


  render() {
    const {
      handleChange, handleClick, handleKeyDown, handleMouseOver, handleSubmit,
      props: { config, namespaceList },
      state: { activeSuggestion, query, queryParts }

    } = this;

    return (
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <div className="input-group">
            <input
              autoFocus
              spellCheck={false}
              className="form-control search-input"
              onChange={handleChange}
              onKeyDown={handleKeyDown}
              placeholder="Enter an identifier to resolve it"
              ref={input => this.search = input}
              value={query}
            />
            <div className="input-group-append">
              <button className="btn btn-primary search-button">
                <i className="icon icon-common icon-search" /> Resolve
              </button>
            </div>
              { config.showSearchSuggestions &&
              <SearchSuggestions
                searchSuggestionList={namespaceList}
                selectedSearchSuggestion={activeSuggestion}
                queryParts={queryParts}
                mouseOver={handleMouseOver}
                handleClick={handleClick}
              />
            }
          </div>
        </div>
      </form>
    )
  }
}


const mapStateToProps = (state) => ({
  config: state.config,
  namespaceList: state.namespaceList
});

const mapDispatchToProps = dispatch => ({
  getNamespacesFromRegistry: (query) => dispatch(getNamespacesFromRegistry(query))
});

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Search));
