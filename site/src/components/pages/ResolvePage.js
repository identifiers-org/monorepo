import React from 'react';


class ResolvePage extends React.Component {
  constructor(props) {
    super(props);

    console.log('this.props', props);
    // const params = new URLSearchParams(props.location.search);

    // this.state = {
    //   query: params.get('query') || ''
    // };
  }

  render() {
    return (
        <>
          results
        </>
    );
  }
}


export default ResolvePage;
