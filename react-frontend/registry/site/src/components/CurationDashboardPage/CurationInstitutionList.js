import React from 'react';
import { connect } from 'react-redux';

// Actions.
import { getCurationInstitutionListFromRegistry } from '../../actions/CurationDashboardPage/CurationInstitutionList';
import { setCurationInstitutionListParams } from '../../actions/CurationDashboardPage/CurationInstitutionListParams';

// Components.
import Paginator from '../common/Paginator';
import CurationInstitutionItem from './CurationInstitutionItem';


class CurationInstitutionList extends React.Component {
  constructor(props) {
    super(props);

    this.state = {debounceSearch: undefined}
  }

  updateCurationInstitutionList = (params) => {
    this.props.getCurationInstitutionListFromRegistry(params);
  }

  componentDidMount() {
    this.updateCurationInstitutionList({page: this.props.curationInstitutionListParams.number});
  }


  handleChangeSearchInput = e => {
    const { curationInstitutionListParams } = this.props;

    clearTimeout(this.state.debounceSearch);

    const nameContent = e.currentTarget.value;

    // Debouce search field.
    this.setState({debounceSearch: setTimeout(() => {
      this.props.setCurationInstitutionListParams({nameContent});
      this.updateCurationInstitutionList({...curationInstitutionListParams, nameContent});
    }, 500)});
  }


  handleNavigate = number => {
    const { curationInstitutionListParams } = this.props;

    this.props.setCurationInstitutionListParams({number});
    this.updateCurationInstitutionList({...curationInstitutionListParams, page: number});
  }

  handleSetSize = e => {
    const { curationInstitutionListParams } = this.props;
    const size = parseInt(e.target.value);
    const number = 0;

    this.props.setCurationInstitutionListParams({size, number});
    this.updateCurationInstitutionList({...curationInstitutionListParams, size});
  }

  render() {
    const {
      handleChangeSearchInput,
      handleNavigate,
      handleSetSize,
      props: {
        curationInstitutionList,
        curationInstitutionListParams: { number, size, totalElements, totalPages }
      },
      state: { nameContent }
    } = this;

    return (
      <>
        <div className="row">
          <div className="col">
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
                onChange={handleChangeSearchInput}
                value={nameContent}
                spellCheck={false}
              />
            </div>
          </div>
        </div>

        <Paginator
          number={number}
          totalPages={totalPages}
          totalElements={totalElements}
          navigate={handleNavigate}
          setSize={handleSetSize}
          size={size}
        />
        <div className="row justify-content-md-center mt-2">
          <div className="col">
            {
              curationInstitutionList.length === 0 ? (
                <p>No institutions stored.</p>
              ) : (
                curationInstitutionList.map(institution => (
                  <CurationInstitutionItem
                    key={`cii-${institution.id}`}
                    institution={institution}
                  />
                ))
              )
            }
          </div>
        </div>
      </>
    );
  }
}


// Redux mapping functions.
const mapStateToProps = (state) => ({
  curationInstitutionList: state.curationDashboard.curationInstitutionList,
  curationInstitutionListParams: state.curationDashboard.curationInstitutionListParams,
});

const mapDispatchToProps = (dispatch) => ({
  getCurationInstitutionListFromRegistry: (params) => dispatch(getCurationInstitutionListFromRegistry(params)),
  setCurationInstitutionListParams: (params) => dispatch(setCurationInstitutionListParams(params))
});

export default connect(mapStateToProps, mapDispatchToProps)(CurationInstitutionList);
