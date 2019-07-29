// Namespaces List reducer

const defaultState = {
  content: '',
  number: 0,
  prefixStart: '',
  size: 20,
  sort: 'name,asc',
  totalElements: 0,
  totalPages: 0
};

const namespaceListParamsReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_NAMESPACELISTPARAMS':
    return { ...state, ...action.namespaceListParams };

  default:
    return state;
  }
}


export default namespaceListParamsReducer;
