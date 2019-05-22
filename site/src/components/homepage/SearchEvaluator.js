import React from 'react';
import { connect } from 'react-redux';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faCheckCircle,
  faExclamationTriangle,
  faTimesCircle
} from '@fortawesome/free-solid-svg-icons';

import { evaluateSearch } from '../../utils/identifiers';


class SearchEvaluator extends React.Component {
  constructor(props) {
    super(props);
  }


  render() {
    const { config, namespaceList, queryParts } = this.props;

    const evaluation = evaluateSearch(queryParts, namespaceList, config.enableResourcePrediction);

    return (
      <>
        <div className="row no-gutters ml-3 mr-2">
          <div className="col d-flex">
            {(() => {
              switch (evaluation) {
                case 'ok':
                  return <FontAwesomeIcon className="text-success mr-2" icon={faCheckCircle} size="2x" />
                case 'prefix_empty': case 'id_empty':
                  return <FontAwesomeIcon className="text-warning mr-2" icon={faExclamationTriangle} size="2x" />
                case 'prefix_unknown': case 'id_bad':
                  return <FontAwesomeIcon className="text-danger mr-2" icon={faTimesCircle} size="2x" />
              }
            })()}
            {(() => {
              switch (evaluation) {
                case 'ok':
                  return (
                    <small className="text-block mr-0">
                      Your compact identifier appears to be valid.
                    </small>
                  );
                case 'prefix_empty':
                  return (
                    <small className="text-block mr-0">
                      Your compact identifier appears to be incomplete: The prefix is missing.
                    </small>
                  );
                case 'prefix_unknown':
                  return (
                    <small className="text-block mr-0">
                      Your compact identifier appears to be incorrect: The prefix&nbsp;
                      <span className="font-weight-bold">{queryParts.prefix}</span>&nbsp;
                      does not exist in the registry.
                    </small>
                  );
                case 'id_empty':
                  return (
                    <small className="text-block mr-0">
                      Your compact identifier appears to be incomplete, the local id is missing.
                    </small>
                  );

                case 'id_bad': {
                  const currentNamespace = namespaceList.filter(namespace => namespace.prefix === queryParts.prefix)[0];
                  return (
                    <small className="text-block mr-0">
                      Your compact identifier appears to be incorrect: The local id&nbsp;
                      <span className="font-weight-bold">{queryParts.id}</span>&nbsp;
                      does not match the pattern&nbsp;
                      <span className="text-monospace">{currentNamespace? currentNamespace.pattern : ''}</span>.
                    </small>
                  );
                }

                case 'resource_not_empty': {
                  return (
                    <small className="text-block mr-0">
                      Your compact identifier appears to be incorrect: You have specified a provider code (
                      <span className="font-weight-bold">{queryParts.resource}</span>
                      ). That is not a compact identifier. Please, remove the provider code.
                    </small>
                  );
                }

                case 'resource_bad': {
                  return (
                    <small className="text-block mr-0">
                      Your compact identifier appears to be incorrect: The provider code&nbsp;
                      <span className="font-weight-bold">{queryParts.resource}</span>&nbsp;
                      does not exist for the prefix&nbsp;
                      <span className="font-weight-bold">{queryParts.prefix}</span>.
                    </small>
                  );
                }
              }
            })()}
            {evaluation.message}
          </div>
        </div>

        <div className="row no-gutters ml-3 mr-2">
          <div className="col ml-5">
            <div className="table-responsive">
              <table className="table table-borderless">
                <tbody>
                  {queryParts.resource ?
                    (
                      <tr>
                        <td className="w-25 p-0 font-weight-light font-italic text-muted"><small>Resource:</small></td>
                        <td className="p-0 text-block"><small>{queryParts.resource || 'default'}</small></td>
                      </tr>
                    ) : null
                  }
                  <tr>
                    <td className="w-25 p-0 font-weight-light font-italic text-muted"><small>Prefix:</small></td>
                    <td className="p-0 text-block"><small>{queryParts.prefix || 'empty'}</small></td>
                  </tr>
                  <tr>
                    <td className="w-25 p-0 font-weight-light font-italic text-muted"><small>Local id:</small></td>
                    <td className="p-0 text-block"><small>{queryParts.id || 'empty'}</small></td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </>
    );
  }
}


const mapStateToProps = (state) => ({
  namespaceList: state.namespaceList,
  config: state.config
});

export default connect (mapStateToProps)(SearchEvaluator);
