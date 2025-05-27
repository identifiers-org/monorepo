import React from 'react';
import SearchStates from "./SearchStates";
import {copyToClipboard} from "../../utils/copyToClipboard";
import { config } from "../../config/Config";

import { evaluateQuery } from '../../utils/identifiers';
import PropTypes from 'prop-types';


class SearchHelper extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      evaluation: null,
      queryParts: null
    };
  }

  shouldComponentUpdate(nextProps, nextState, nextContext) {
    return !nextProps.loading;
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    const {namespaceList, query, setSearchState} = this.props;
    const [evaluation, queryParts] = evaluateQuery(query, namespaceList);

    if (evaluation !== prevState.evaluation || query !== prevProps.query) {
      this.setState({evaluation, queryParts})
      setSearchState(evaluation);
    }
  }

  render() {
    const { namespaceList, query } = this.props;
    const { evaluation, queryParts } = this.state;

    if (!evaluation || !queryParts) return <></>

    const idorgURI = evaluation === SearchStates.VALID_CURIE && queryParts?.id && queryParts?.prefix ?
      `${config.resolverHardcodedUrl}/${queryParts.prefix}:${queryParts.id}` : null

    return (
      <>
        <div className="row g-0 ms-3 me-2">
          <div className="col d-flex align-items-center">
            {(() => {
              switch (evaluation) {
                case SearchStates.VALID_CURIE:
                  return <i className="icon icon-common icon-check-circle size-200 text-success me-2"/>
                case SearchStates.NO_CURIE: case SearchStates.QUERYING_NAMESPACES:
                  return <i className="icon icon-common icon-info size-200 text-primary me-2"/>
                case SearchStates.PREFIX_ONLY: case SearchStates.PREFIX_WITH_COLON:
                  return <i className="icon icon-common icon-exclamation-triangle size-200 text-warning me-2"/>
                case SearchStates.INVALID_PREFIX: case SearchStates.INVALID_LOCAL_ID:
                  return <i className="icon icon-common icon-times-circle size-200 text-danger me-2"/>
              }
            })()}
            {(() => {
              switch (evaluation) {
                case SearchStates.VALID_CURIE:
                  return (
                    <small className="text-block me-0">
                      Your compact identifier appears to be valid. <br/> Make sure the URI takes you where you expect it to.
                    </small>
                  );
                case SearchStates.NO_CURIE:
                  return (
                    <small className="text-block me-1 mt-2 mb-3">
                      Please type a <a target="_blank" href="https://docs.identifiers.org/pages/identification_scheme.html">compact identifier</a> in this box in the format {'[prefix]:[id]'}. <br/>
                      If you don't know which prefix to use for a data object:
                      <ul>
                        <li>Describe its repository or its data type, then try the suggested prefixes.</li>
                        <li>Avoid describing data objects. We don't index individual objects.</li>
                        <li>Short text works better.</li>
                        <li>Adding the ID of an object will sort results using the registry's ID patterns, aiming to show best matches first.</li>
                        <li>If you can't find a good prefix, request help via the feedback button above.</li>
                      </ul>
                    </small>
                  );
                case SearchStates.INVALID_PREFIX:
                  return (
                    <small className="text-block me-0">
                      Your compact identifier appears to be incorrect.{' '}
                      {query.split(/\s+/).length === 1 && <>
                        The prefix{' '}
                        <span className="fw-bold">{queryParts.prefixEffectiveValue}</span>{' '}
                        does not exist in the registry.{' '}
                      </>}
                      {namespaceList.length > 0 && <>Try one of the suggestions above or try a different search.</>}
                    </small>
                  );
                case SearchStates.PREFIX_ONLY:
                  return (
                    <small className="text-block me-0">
                      You have a valid prefix, but your compact identifier is incomplete. Please add a colon and object ID.
                    </small>
                  );
                case SearchStates.PREFIX_WITH_COLON:
                  return (
                    <small className="text-block me-0">
                      You are just missing the object ID to finish the compact identifier.
                    </small>
                  );
                case SearchStates.INVALID_LOCAL_ID: {
                  const currentNamespace = namespaceList.filter(namespace => namespace.prefix === queryParts.prefixEffectiveValue)[0];
                  return (
                    <small className="text-block me-0">
                      Your compact identifier appears to be incorrect: The local id{' '}
                      <span className="fw-bold">{queryParts.id}</span>{' '}
                      does not match the pattern{' '}
                      <span className="font-monospace">{currentNamespace? currentNamespace.lui_pattern : ''}</span>.
                    </small>
                  );
                }
              }
            })()}
          </div>
        </div>

        <div className="row g-0 ms-3 me-2">
          <div className="col ms-5">
            <div className="table-responsive">
              <table className="table table-borderless">
                <tbody>
                  { queryParts?.resource &&
                    <tr>
                      <td className="w-25 p-0 fw-light font-italic text-muted"><small>Resource:</small></td>
                      <td className="p-0 text-block"><small>{queryParts.resource || 'default'}</small></td>
                    </tr>
                  }
                  { queryParts?.prefix &&
                    <tr>
                      <td className="w-25 p-0 fw-light fst-italic text-muted"><small>Prefix:</small></td>
                      <td className="p-0 text-block"><small>{queryParts.prefix || 'empty'}</small></td>
                    </tr>
                  }
                  { queryParts?.id &&
                    <tr>
                      <td className="w-25 p-0 fw-light font-italic text-muted"><small>Local id:</small></td>
                      <td className="p-0 text-block"><small>{queryParts.id || 'empty'}</small></td>
                    </tr>
                  }
                  { idorgURI &&
                      <tr>
                        <td className="w-25 p-0 fw-light font-italic text-muted"><small>URI:</small></td>
                        <td className="p-0 text-block">
                        <small>
                          <a href={idorgURI} target='_blank'>{idorgURI}</a>
                          <button className='text-muted ms-1' title='copy to clipboard'
                            onClick={(ev) => this.copyToClipboard(idorgURI, ev)}>
                            <i className="icon icon-common icon-copy"></i>
                          </button>
                        </small>
                      </td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </>
    );
  }
}
SearchHelper.propTypes = {
  loading: PropTypes.bool,
  setSearchState: PropTypes.func.isRequired,
  query: PropTypes.string.isRequired,
  namespaceList: PropTypes.array.isRequired,
}
export default SearchHelper;
