import React from 'react';
import { connect } from 'react-redux';

// Actions.
import { getCurationInstitutionListFromRegistry } from '../../actions/CuratorDashboardPage/CurationInstitutionList';
import { setCurationInstitutionListParams } from '../../actions/CuratorDashboardPage/CurationInstitutionListParams';

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
    clearTimeout(this.state.debounceSearch);

    const nameContent = e.currentTarget.value;

    // Debouce search field.
    this.setState({debounceSearch: setTimeout(() => {
      this.props.setCurationInstitutionListParams({nameContent});
      this.updateCurationInstitutionList({nameContent});
    }, 500)});
  }


  handleNavigate = (number) => {
    this.props.setCurationInstitutionListParams({number});
    this.updateCurationInstitutionList({page: number});
  }

  render() {
    const {
      handleChangeSearchInput,
      handleNavigate,
      props: {
        curationInstitutionList,
        curationInstitutionListParams: { number, totalPages }
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
          navigate={handleNavigate}
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
  curationInstitutionList: state.curatorDashboard.curationInstitutionList,
  curationInstitutionListParams: state.curatorDashboard.curationInstitutionListParams,
});

const mapDispatchToProps = (dispatch) => ({
  getCurationInstitutionListFromRegistry: (params) => dispatch(getCurationInstitutionListFromRegistry(params)),
  setCurationInstitutionListParams: (params) => dispatch(setCurationInstitutionListParams(params))
});

export default connect(mapStateToProps, mapDispatchToProps)(CurationInstitutionList);
