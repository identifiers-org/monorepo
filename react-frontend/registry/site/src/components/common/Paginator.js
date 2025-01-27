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
    const { number = 0, totalPages = 0, size = 0, totalElements = 0, setSize } = this.props;

    const smallerThan = (size) => window.matchMedia(`(max-width: ${size}px)`).matches;

    const smallOrder1 =  smallerThan(991) ? 'order-1' : '';
    const smallOrder2 =  smallerThan(991) ? 'order-2' : '';
    const smallOrder3 =  smallerThan(991) ? 'order-3' : '';

    const smallCenter = smallerThan(767) ? 'justify-content-center align-self-center' : '';

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

    const linkArray = [...Array(totalPages).keys()].slice(firstIndex, lastIndex);

    const firstElement = number * size + 1;
    const lastElement = number < (totalPages - 1) ? (number + 1) * size : (number * size) + (totalElements % size);

    return (
      <div className="row justify-content-between">
        <div className={`col col-12 col-md-8 col-lg-6 col-xl-6 paginator mb-2 ${smallOrder2}`}>
          <ul className={`pagination pagination-sm m-0 ${smallCenter}`}>
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

        <div className={`col col-12 col-lg-3 col-xl-4 mb-2 ${smallOrder1}`}>
          <span className="pager">
            {totalElements === 0 ? '' : `${firstElement} to ${lastElement} of ${totalElements}`}
          </span>
        </div>

        <div className={`col col-xs-12 col-sm-12 col-sm-6 col-md-4 col-lg-3 col-xl-2 mb-2 ${smallOrder3} ${smallCenter}`}>
          <div className="input-group input-group-sm">
            <div className="input-group-prepend">
              <label className="input-group-text" htmlFor="perPageSelect">Per page</label>
            </div>
            <select
              className="custom-select"
              id="perPageSelect"
              onChange={setSize}
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
