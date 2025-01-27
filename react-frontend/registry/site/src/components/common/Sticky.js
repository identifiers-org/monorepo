import React from 'react';

// Adapted from https://medium.com/trendkite-dev/building-a-sticky-header-component-in-react-f2117099a9a2
class Sticky extends React.Component {
  constructor(props) {
    super(props);
  }

  static baseClass = 'Sticky-base'

  state = {
    height: 0,
    width: 0,
    stuckTop: false
  }

  frameId = 0;
  stickyDiv = React.createRef();


  componentDidMount() {
    this.addEvents();
    this.handleScroll();
  }

  componentDidUpdate(prevProps) {
    if (prevProps.scrollTarget !== this.props.scrollTarget) {
      this.removeEvents();
      this.addEvents();
    }
  }

  componentWillUnmount() {
    this.removeEvents();
  }


  addEvents() {
    const scrollTarget = this.props.scrollTarget || window;

    if (scrollTarget && this.stickyDiv.current) {
      scrollTarget.addEventListener('scroll', this.debouncedScroll);
    }
  }

  removeEvents() {
    const scrollTarget = this.props.scrollTarget || window;

    if (scrollTarget) {
      scrollTarget.removeEventListener('scroll', this.debouncedScroll);
    }

    if (this.frameId) {
      cancelAnimationFrame(this.frameId);
    }
  }


  handleScroll = () => {
    const { sides } = this.props;
    const stickyDiv = this.stickyDiv.current || null;
    const scrollTarget = this.props.scrollTarget || window;

    this.frameId = 0;

    if (!stickyDiv) {
      return;
    }

    const scrollRect = scrollTarget.getBoundingClientRect ? scrollTarget.getBoundingClientRect() : {
      height: scrollTarget.innerHeight,
      width: scrollTarget.innerWidth,
      top: 0,
      bottom: 0,
      left: 0,
      right: 0,
      x: scrollTarget.scrollX,
      y: scrollTarget.scrollY,
    };

    let stickyRect = stickyDiv.getBoundingClientRect();

    if (!this.state.height || !this.state.width) {
      this.setState({
        height: stickyRect.height,
        width: stickyRect.height
      });
    }

    stickyRect = {
      height: this.state.height || stickyRect.height,
      width: this.state.width || stickyRect.width,
      x: stickyRect.x,
      y: stickyRect.y
    };

    if (typeof sides.bottom === 'number') {
      const stuckBottom = stickyRect.y + stickyRect.height > (scrollRect.height + scrollRect.top) - sides.bottom;
      this.setState({ stuckBottom });
    }

    if (typeof sides.top === 'number') {
      const stuckTop = stickyRect.y < scrollRect.top + sides.top;
      this.setState({ stuckTop });
    }

    if (typeof sides.left === 'number') {
      const stuckLeft = stickyRect.x < scrollRect.left + sides.left;
      this.setState({ stuckLeft });
    }

    if (typeof sides.right === 'number') {
      const stuckRight = stickyRect.x + stickyRect.width > (scrollRect.width + scrollRect.left) - sides.right;
      this.setState({ stuckRight });
    }
  }

  debouncedScroll = () => {
    if (!this.frameId) {
      const frameId = requestAnimationFrame(this.handleScroll);
      this.frameId = frameId;
    }
  }


  render() {
    const { children } = this.props;
    const { stuckBottom, stuckLeft, stuckRight, stuckTop } = this.state;

    const stickyModifiers = [];

    if (stuckBottom) {
      stickyModifiers.push('stuck-bottom');
    }

    if (stuckLeft) {
      stickyModifiers.push('stuck-left');
    }

    if (stuckRight) {
      stickyModifiers.push('stuck-right');
    }

    if (stuckTop) {
      stickyModifiers.push('stuck-top');
    }

    const childrenWithStuckProps = React.Children.map(children, (child) => {
      const childModifiers = (child.props && child.props.modifiers) || [];
      return React.cloneElement(child, { modifiers: [...childModifiers, ...stickyModifiers] });
    });

    return (
      <div
        className={Sticky.baseClass}
        ref={this.stickyDiv}
      >
        {childrenWithStuckProps}
      </div>
    );
  }
}

Sticky.defaultProps = {
  scrollTarget: null,
  sides: { top: 0 }
}


export default Sticky;
