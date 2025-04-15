import React from 'react';
import { useNavigate } from 'react-router-dom';
import SearchStates from './SearchStates'
import SearchSuggestions from './SearchSuggestions';
import { config } from '../../config/Config';
import { isSmallScreen } from '../../utils/responsive';
import PropTypes from 'prop-types';


class Search extends React.Component {
  exampleCuries = Object.freeze([ 'uniprot:P12345', 'pdb:2gc4', 'taxonomy:9606' ])

  constructor(props) {
    super(props);

    this.state = {
      isSearchValid: false,
      showSuggestions: false,
      query: '',
    }

    this.search = React.createRef();
    this.suggestionListRef = React.createRef();
  }

  setSearchState = (currentState) => {
    const isNewStateValid = currentState === SearchStates.VALID_CURIE;
    if (this.state.isSearchValid !== isNewStateValid) {
      this.setState({isSearchValid : isNewStateValid})
    }
  }

  handleFocusShowSuggestions = () => {
    this.setState({
      showSuggestions: true
    })
    // Also scroll down to take the suggestion bar to the top of screen in small screens.
    if (isSmallScreen()) {
      const searchBarYOffset = this.search.current.getBoundingClientRect().top + window.pageYOffset - 130;
      window.scroll({top: searchBarYOffset, behavior: 'smooth'});
    }
  };

  closeSuggestions = () => {
    this.setState({
      showSuggestions: false
    })
  }

  handleChange = () => {
    this.setState({
      query: this.search.current.value,
    });
  }

  handleKeyDown = e => {
    switch (e.key) {
      case 'Enter': {
        e.preventDefault();
        e.stopPropagation()
        if (this.suggestionListRef.current?.hasSelection()) {
          this.suggestionListRef.current?.clickSelection();
        }
        else if (this.state.isSearchValid) {
          this.props.onButtonClick(this.state.query);
        }
        break;
      }

      case 'ArrowUp': {
        e.preventDefault();
        e.stopPropagation()
        this.suggestionListRef.current?.upSelection()
        break;
      }

      case 'ArrowDown': {
        e.preventDefault();
        e.stopPropagation()
        this.suggestionListRef.current?.downSelection();
        break;
      }

      case 'PageDown': {
        e.preventDefault();
        e.stopPropagation()
        this.suggestionListRef.current?.downSelection(5);
        break;
      }

      case 'PageUp': {
        e.preventDefault();
        e.stopPropagation()
        this.suggestionListRef.current?.upSelection(5);
        break;
      }
    }
  }

  handleSuggestion = (suggestion) => {
    this.setState({
      query: suggestion,
      showSuggestions: true,
    }, () => this.search.current.focus());
  }

  handleSubmit = e => {
    e.preventDefault();
    this.handleButtonClick();
  }

  handleButtonClick = () => {
    if (this.state.isSearchValid) {
      this.props.onButtonClick(this.state.query);
    } else {
      this.search.current && this.search.current.focus();
    }
  }

  handleExampleClick = (e) => {
    e.preventDefault();
    if (e.key && e.key !== 'Enter') return; // Check for Enter key keyboard event (For accessibility)
    this.search.current.value = e.target.innerText;
    this.handleChange();
    this.search.current.focus();
  }

  render() {
    const {
      props: { buttonCaption, placeholderCaption },
      state: { query, showSuggestions, isSearchValid }
    } = this;

    return (
      <form onSubmit={this.handleSubmit} role='search'>
        <div className="form-group">
          <small className="form-text text-muted ms-1">
            Examples:{' '}
            {
              this.exampleCuries.map(example => (
                  <a role='suggestion' key={example} tabIndex='0'
                     onClick={this.handleExampleClick}
                     onKeyDown={this.handleExampleClick}
                     className='text-primary text-decoration-none me-2'>
                    {example}
                  </a>
              ))
            }
          </small>
          <div className="input-group">
            <input
                role="searchbox"
                spellCheck={false}
                className="form-control search-input"
                onChange={this.handleChange}
                onKeyDown={this.handleKeyDown}
                onFocus={this.handleFocusShowSuggestions}
                placeholder={placeholderCaption}
                ref={this.search}
                value={query}
            />
            <button
                className="btn btn-primary text-white search-button"
                onFocus={this.handleFocusShowSuggestions}
                onClick={this.handleButtonClick}
                disabled={!isSearchValid}
                title={isSearchValid ? undefined : "The compact identifier is not valid"}
                type="submit"
            >
              {buttonCaption}
            </button>
            {config.showSearchSuggestions && showSuggestions &&
                <SearchSuggestions
                    setSearchState={this.setSearchState}
                    closeSuggestions={this.closeSuggestions}
                    setSuggestion={this.handleSuggestion}
                    suggestionListRef={this.suggestionListRef}
                    query={query}
                />
            }
          </div>
          <a className="text-muted text-sm ms-1 text-decoration-none"
             href="https://www.ebi.ac.uk/ebisearch" target="_blank">
            powered by EBI Search
          </a>
        </div>
      </form>
    )
  }
}

Search.propTypes = {
  onButtonClick: PropTypes.func.isRequired,
  buttonCaption: PropTypes.oneOfType([PropTypes.element, PropTypes.string]).isRequired,
  placeholderCaption: PropTypes.string.isRequired
}
export default props => {
  const navigate = useNavigate();
  return <Search {...props} navigate={navigate} />
};