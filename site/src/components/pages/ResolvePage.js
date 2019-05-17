import React from 'react';


class ResolvePage extends React.Component {
  constructor(props) {
    super(props);

    console.log('props', props);

    const params = new URLSearchParams(props.location.search);

    this.state = {
      query: params.get('query') || ''
    };
  }

  render() {
    return (
      <>
        <div className="row">
          <div className="col">
            results of {this.state.query}
          </div>
        </div>
      </>
    );
  }
}


export default ResolvePage;
