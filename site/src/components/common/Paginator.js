import React from 'react';


class Paginator extends React.Component {
  constructor (props) {
    super(props);

    this.state = {
      pageWindow: 3
    };
  }


  handleNavigate = (pageLink) => {
    this.setState({
      currentPage: pageLink
    });

    this.props.navigate(pageLink);
  }


  render() {
    const entriesPerPage = [5, 20, 50, 100];
    const { pageWindow } = this.state;
    const { number = 0, totalPages = 0, size = 0, totalElements = 0, setsize } = this.props;

    // First and last indexes of paginator.
    let firstIndex = Math.max(0, (number - pageWindow));
    let lastIndex = Math.min(number + pageWindow + 1, Math.max(totalPages, (number + pageWindow)));

    // If current page < pageWindow, add difference to last index.
    if (number < pageWindow + 1) {
      lastIndex += pageWindow - number;
    }

    // If current page + pageWindow > totalPages, subtract difference to first index.
    if (number + pageWindow > totalPages - 1) {
      firstIndex -= pageWindow + number + 1;
    }

    const linkArray = [...Array(totalPages).keys()]
      .slice(firstIndex, lastIndex);

    return (
      <div className="row justify-content-between">
        <div className="col col-xs-12 col-sm-6 paginator">
          <ul className="pagination pagination-sm">
            <li className={`page-item ${number === 0 ? 'disabled' : ''}`}>
              <a
                className="page-link"
                href="#!"
                onClick={() => {this.props.navigate(0)}}
              >
                <i className="icon icon-common icon-angle-double-left" />
              </a>
            </li>
            <li className={`page-item ${number === 0 ? 'disabled' : ''}`}>
              <a
                className="page-link"
                href="#!"
                onClick={() => {this.props.navigate(Math.max(number - 1, 0))}}
              >
                <i className="icon icon-common icon-angle-left" />
              </a>
            </li>

            {
              linkArray.map(pageLink =>
                <li
                  key={`li-${pageLink}`}
                  className={`page-item paginator-link ${pageLink === number ? 'active' : ''}`}
                >
                  <a
                    className={`page-link ${pageLink === number ? 'active' : ''}`}
                    href="#!"
                    onClick={() => {this.handleNavigate(pageLink)}}
                    key={`${pageLink}-navigate`}
                  >
                    {pageLink + 1}
                  </a>
                </li>
              )
            }

            <li className={`page-item ${number === totalPages - 1 ? 'disabled' : ''}`}>
              <a
                className="page-link" href="#!"
                onClick={() => {this.props.navigate(Math.min(number + 1, (totalPages - 1)))}}
              >
                <i className="icon icon-common icon-angle-right" />
              </a>
            </li>
            <li className={`page-item ${number === totalPages - 1 ? 'disabled' : ''}`}>
              <a
                className="page-link" href="#!"
                onClick={() => {this.props.navigate(totalPages - 1)}}
              >
                <i className="icon icon-common icon-angle-double-right" />
              </a>
            </li>
          </ul>
        </div>

        <div className="col col-xs-8 col-sm-4 col-md-3 col-xl-4">
          <span className="btn-align">
            {number * size + 1} to {(number + 1) * size} of {totalElements}
          </span>
        </div>

        <div className="col col-xs-3 col-sm-12 col-md-3 col-xl-2">
          <div className="input-group input-group-sm">
            <div className="input-group-prepend">
              <label className="input-group-text" htmlFor="perPageSelect">Per page</label>
            </div>
            <select
              className="custom-select"
              id="perPageSelect"
              onChange={setsize}
              defaultValue={size}
            >
              {entriesPerPage.map(option => (
                <option
                  key={option}
                  value={option}
                >
                  {option}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>
    );
  }
}


export default Paginator;
