import React from 'react';
import { connect } from 'react-redux';

import { getNamespacesFromRegistry } from '../../actions/NamespaceList';

import NamespaceItem from './NamespaceItem';
import Paginator from '../common/Paginator';


class NamespaceList extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      namespaceListParams: {
        ...this.props.namespaceListParams,
        content: this.props.query,
        size: 20
      }
    };
  }

  updateNamespaceList = async () => {
    await this.props.getNamespacesFromRegistry(this.state.namespaceListParams);
    this.setState({namespaceListParams: this.props.namespaceListParams});
  }

  componentDidMount() {
    this.updateNamespaceList();
  }


  handleNavigate = (where) => {
    const { namespaceListParams } = this.state;

    if (namespaceListParams.number === where) return;

    this.setState({
      namespaceListParams: {
        ...namespaceListParams,
        number: where
      }
    }, () => this.updateNamespaceList());
  }

  handleAlphabeticSearch = e => {
    const { namespaceListParams } = this.state;

    this.setState({
      namespaceListParams: {
        ...namespaceListParams,
        content: '',
        number: 0,
        prefixStart: e.target.innerText.toLowerCase()
      }
    }, () => {
      this.updateNamespaceList();
    });
  }

  handleSearch = e => {
    const { namespaceListParams } = this.state;

    this.setState({
      namespaceListParams: {
        ...namespaceListParams,
        prefixStart: '',
        content: e.currentTarget.value,
        number: 0
      }
    }, () => {
      this.updateNamespaceList();
    });
  }

  handleSetSize = e => {
    const { namespaceListParams } = this.state;

    this.setState({
      namespaceListParams: {
        ...namespaceListParams,
        size: e.target.value,
        number: 0
      }
    }, () => this.updateNamespaceList());
  }


  render() {
    const alphabetSearch = '#ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');

    const {
      handleNavigate,
      handleAlphabeticSearch,
      handleSearch,
      handleSetSize,
      state: {
        namespaceListParams: {prefixStart, content, number, size, totalElements, totalPages}
      },
      props: {namespaceList}
    } = this;

    return (
      <>
        <div className="row">

          {/* ALPHABETIC PAGINATOR */}
          <div className="col col-lg-12 col-xl-8">
            <div className="paginator">
              <ul className="pagination pagination-sm m-0">
                {
                  alphabetSearch.map(letter =>
                    <li
                      key={`alphabet-${letter}`}
                      className={`page-item ${letter.toLowerCase() === prefixStart ? 'active' : ''}`}
                    >
                      <a
                        className={`page-link ${letter.toLowerCase() === prefixStart ? 'active' : ''}`}
                        href="#!"
                        onClick={handleAlphabeticSearch}
                      >
                        {letter}
                      </a>
                    </li>
                  )
                }
              </ul>
            </div>
          </div>

          {/* SEARCH BAR */}
          <div className="col col-lg-12 col-xl-4">
            <div className="input-group input-group-sm mb-3">
              <div className="input-group-prepend">
                <span className="input-group-text" id="registry-search">Search</span>
              </div>
              <input
                type="text"
                className="form-control"
                placeholder="Input a search query"
                aria-label="Search"
                aria-describedby="registry-search"
                onChange={handleSearch}
                value={content}
              />
            </div>
          </div>
        </div>

        {/* NAMESPACE LIST */}
        <div>
          {
            namespaceList.length === 0 ? (
              <p className="text-center my-5">No items</p>
            ) : (
              <div className="card mb-3">
                <table className="table table-sm table-striped table-hover table-borderless">
                  <thead className="thead-light thead-rounded">
                    <tr>
                      <th className="narrow">
                        <i className="icon icon-common icon-list" /> Name
                      </th>
                      <th className="narrow">
                        <i className="icon icon-common icon-card" /> Prefix
                      </th>
                      <th className="wide">
                        <i className="icon icon-common icon-info" /> Description
                      </th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                      // Page data.
                      namespaceList.map(namespace => (
                        <NamespaceItem key={`namespace-${namespace.prefix}`} {...namespace} />
                      ))
                    }
                  </tbody>
                </table>
              </div>
            )
          }
        </div>

        {/* FOOTER */}
        <footer>
          <Paginator
            navigate={handleNavigate}
            number={number}
            setsize={handleSetSize}
            size={size}
            totalPages={totalPages}
            totalElements={totalElements}
          />
        </footer>
      </>
    );
  }
}


// Mapping
const mapStateToProps = (state) => {
  return {
    namespaceList: state.registryBrowser.namespaceList,
    namespaceListParams: state.registryBrowser.namespaceListParams
  }
};

const mapDispatchToProps = dispatch => ({
  getNamespacesFromRegistry: (params) => dispatch(getNamespacesFromRegistry(params))
});

export default connect (mapStateToProps, mapDispatchToProps)(NamespaceList);
