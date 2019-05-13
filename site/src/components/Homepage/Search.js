import React from 'react';
import SearchSuggestions from './SearchSuggestions';

import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSearch } from '@fortawesome/free-solid-svg-icons';

import { getNamespacesFromRegistry } from '../../actions/NamespaceList';


class Search extends React.Component {
  constructor(props) {
    super(props);
    this.search = React.createRef();

    this.state = {
      query: this.props.query,
      activeSuggestion: -1
    }
  }

  componentDidMount() {
    this.updateNamespaceList();
  }


  updateNamespaceList = async () => {
    const {
      props: {getNamespacesFromRegistry},
      state: {query}
    } = this;

    // set active suggestion to -1 if length of query is 0.
    if (query.length === 0) {
      this.setState({activeSuggestion: -1});
    }

    await getNamespacesFromRegistry({
      content: query,
      number: 0,
      prefixStart: '',
      size: 10,
      sort: 'name,asc'
    });
  }


  handleChange = () => {
    this.setState({query: this.search.value}, () => {
      this.updateNamespaceList();
    });
  }

  handleKeyDown = e => {
    const {
      props: {namespaceList},
      state: {activeSuggestion, query}
    } = this;

    switch (e.keyCode) {
    case 13: {  // Enter key
      e.preventDefault();
      if (activeSuggestion === -1) {
        this.props.history.push(`/registry?query=${query}`);
      } else {
        this.props.history.push(`/registry/${namespaceList[activeSuggestion].prefix}`);
        break;
      }
    }

    case 38: {  // Up key
      if (this.state.activeSuggestion >= -1) {
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

  handleSubmit = e => {
    const {query} = this.state;

    e.preventDefault();
    this.props.history.push(`/registry?query=${query}`);
  }


  render() {
    const {
      handleChange, handleKeyDown, handleMouseOver, handleSubmit,
      props: {namespaceList},
      state: {activeSuggestion, query}

    } = this;

    return (
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <div className="input-group">
            <input
              autoFocus
              className="form-control"
              onChange={handleChange}
              onKeyDown={handleKeyDown}
              placeholder="Enter a namespace to search the registry"
              ref={input => this.search = input}
              value={query}
            />
            <div className="input-group-append">
              <button className="btn btn-primary">
              <FontAwesomeIcon icon={faSearch} /> Search
              </button>
            </div>
          </div>
          <SearchSuggestions
            searchSuggestionList={namespaceList}
            selectedSearchSuggestion={activeSuggestion}
            query={query}
            mouseover={handleMouseOver}
          />
        </div>
      </form>
    )
  }
}


const mapStateToProps = (state) => ({
  namespaceList: state.registryBrowser.namespaceList
});

const mapDispatchToProps = dispatch => ({
  getNamespacesFromRegistry: (params) => dispatch(getNamespacesFromRegistry(params))
});

export default withRouter(connect (mapStateToProps, mapDispatchToProps)(Search));
