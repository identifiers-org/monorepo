import React from 'react';
import { connect } from 'react-redux';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCopy } from '@fortawesome/free-regular-svg-icons';

import { getResolvedResources } from '../../actions/ResolvedResources';
import ResourceList from '../resolverpage/ResourceList';

import Spinner from '../common/Spinner';
import { swalToast } from '../../utils/swalDialogs';


class ResolvePage extends React.Component {
  constructor(props) {
    super(props);

    const params = new URLSearchParams(props.location.search);

    this.state = {
      query: params.get('query') || '',
      isLoading: true
    };
  }

  componentDidMount = async () => {
    const {
      props: { getResolvedResources },
      state: { query }
    } = this;

    await getResolvedResources(query)
    this.setState({isLoading: false});
  }


  handleCopyToClipboard = (value) => {
    const dummy = document.createElement('input');
    document.body.appendChild(dummy);
    dummy.setAttribute('value', value);
    dummy.select();
    document.execCommand('copy');
    document.body.removeChild(dummy);

    swalToast.fire({
      type: 'success',
      title: 'Copied to clipboard'
    })
  }


  render() {
    const {
      handleCopyToClipboard,
      props: { config },
      state: { isLoading, query }
    } = this;

    const compactIdentifier = `${config.resolverHardcodedUrl}/${query}`;

    return (
      <>
      <div className="row mb-5">
        <div className="col">
          {
            isLoading ? (
              <Spinner />
            ) : (
              <>
                <div className="row mb-5 justify-content-md-center">
                  <div className="col d-flex align-items-center text-middle justify-content-center border p-2 bg-cardgrey">
                    <h4 className="mb-0 text-success">
                      {compactIdentifier}
                    </h4>
                    {
                      document.queryCommandSupported('copy') && (
                        <button
                          className="btn btn-sm btn-primary-outline"
                          onClick={() => {handleCopyToClipboard(compactIdentifier)}}
                        >
                          <FontAwesomeIcon
                            icon={faCopy}
                            size="2x"
                            className="min-width-2"
                            title="Copy to clipboard"
                          />
                        </button>
                      )
                    }
                  </div>
                </div>
                <div className="row">
                  <div className="col">
                    <h4 className="mb-0">
                      Compact Identifier:&nbsp;
                      <strong className="text-primary font-italic">{query}</strong>
                    </h4>
                  </div>
                </div>

                <ResourceList />
              </>
            )
          }
        </div>
      </div>
      </>
    );
  }
}


// Redux mappings.
const mapStateToProps = (state) => ({
  config: state.config
});

const mapDispatchToProps = dispatch => ({
  getResolvedResources: (query) => dispatch(getResolvedResources(query))
});

export default connect (mapStateToProps, mapDispatchToProps)(ResolvePage);
