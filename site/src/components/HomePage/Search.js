import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';

// Components.
import SearchSuggestions from './SearchSuggestions';

// Actions.
import { getNamespacesFromRegistry } from '../../actions/NamespaceList';
import { setConfig } from '../../actions/Config';

// Utils.
import { querySplit, completeQuery, evaluateSearch } from '../../utils/identifiers';
import { isSmallScreen } from '../../utils/responsive';


class Search extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      query: '',
      queryParts: {
        resource: undefined,
        prefix: undefined,
        prefixEffectiveValue: undefined,
        id: undefined,
        idWithEmbeddedPrefix: undefined,
        bad: []
      },
      activeSuggestion: -1,
      namespaceList: []
    }

    this.search = React.createRef();
    this.suggestionListRef = React.createRef();
  }

  componentDidMount() {
    this.updateNamespaceList();
  }


  updateNamespaceList = async () => {
    const {
      props: {
        getNamespacesFromRegistry,
        config: { suggestionListSize }
      },
      state: { queryParts: { prefixEffectiveValue } }
    } = this;

    // set active suggestion to -1.
    this.setState({activeSuggestion: -1});

    await getNamespacesFromRegistry(prefixEffectiveValue);

    this.setState({
      namespaceList: this.props.namespaceList.sort((a, b) => {
        if (a.prefix.startsWith(prefixEffectiveValue) && !b.prefix.startsWith(prefixEffectiveValue)) {
          return -1;
        }

        if (!a.prefix.startsWith(prefixEffectiveValue) && b.prefix.startsWith(prefixEffectiveValue)) {
          return 1;
        }

        return a.prefix - b.prefix;
      }).slice(0, suggestionListSize)
    });
  }

  handleFocusShowSuggestions = () => {
    const {
      search,
      props: { setConfig }
    } = this;

    setConfig({showSearchSuggestions: true})

    // Also scroll down to take the suggestion bar to the top of screen in small screens.
    if (isSmallScreen()) {
      const searchBarYOffset = search.getBoundingClientRect().top + window.pageYOffset - 130;
      window.scroll({top: searchBarYOffset, behavior: 'smooth'});
    }
  };

  // Ref to handle suggestion list scroll when using arrows keys.
  setSuggestionListRef = (ref) => {
    if (ref) {
      this.suggestionListRef = ref;
    };
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
      state: { namespaceList, activeSuggestion, queryParts },
      suggestionListRef
    } = this;

    switch (e.keyCode) {
    case 13: {  // Enter key
      e.preventDefault();   // Do not send form.

      if (activeSuggestion === -1) {
        handleSearch();
      } else {
        e.currentTarget.value = completeQuery(queryParts.resource, namespaceList[activeSuggestion], queryParts.id);
        handleChange();
        break;
      }
      break;
    }

    case 38: {  // Up key
      e.preventDefault();   // Do not take cursor to start of input text.

      if (this.state.activeSuggestion > -1) {
        this.setState({activeSuggestion: activeSuggestion - 1});
        // Scroll up to that item.
        if ((activeSuggestion - 1) * 33 < suggestionListRef.scrollTop) {
          suggestionListRef.scrollTop -= 33;
        }
      }
      break;
    }

    case 40: {  // Down key
      e.preventDefault();   // Do not take cursor to end of input text.

      if (activeSuggestion < namespaceList.length - 1) {
        this.setState({activeSuggestion: activeSuggestion + 1});
        // Scroll down to that item.
        if ((activeSuggestion + 2) * 33 > suggestionListRef.clientHeight) {
          suggestionListRef.scrollTop += 33;
        }
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
    e.preventDefault();
    this.handleSearch();
  }

  handleSearch = () => {
    const {
      props: { config, history },
      state: { namespaceList, query, queryParts }
    } = this;

    const evaluation = evaluateSearch(queryParts, namespaceList, config.enableResourcePrediction);

    if (evaluation !== 'ok') {
      return;
    }


    history.push(`resolve?query=${query}`);
  }


  render() {
    const {
      handleChange,
      handleClick,
      handleFocusShowSuggestions,
      handleKeyDown,
      handleMouseOver,
      handleSubmit,
      setSuggestionListRef,
      props: { config },
      state: { activeSuggestion, namespaceList, query, queryParts }

    } = this;

    return (
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <div className="input-group">
            <input
              autoFocus={!isSmallScreen()}
              spellCheck={false}
              className="form-control search-input"
              onChange={handleChange}
              onKeyDown={handleKeyDown}
              onFocus={handleFocusShowSuggestions}
              placeholder="Enter an identifier to resolve it"
              ref={input => this.search = input}
              value={query}
            />
            <div className="input-group-append">
              <button
                className="btn btn-primary search-button"
                onFocus={handleFocusShowSuggestions}
              >
                <i className="icon icon-common icon-search" /> Resolve
              </button>
            </div>
            { config.showSearchSuggestions &&
              <SearchSuggestions
                searchSuggestionList={namespaceList}
                selectedSearchSuggestion={activeSuggestion}
                setSuggestionListRef={setSuggestionListRef}
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
  getNamespacesFromRegistry: (query) => dispatch(getNamespacesFromRegistry(query)),
  setConfig: (config) => dispatch(setConfig(config))
});

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Search));