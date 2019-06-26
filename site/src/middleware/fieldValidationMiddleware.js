const fieldValidationMiddleware = ({ dispatch, getState }) => next => action => {

  if (!action.type.includes('/SET_VALUE')) {
    return next(action);
  }

  const validator = validateField()

  console.log('CUSTOM MIDDLEWARE VALIDATION', getState());
  console.log('action', action);


  return next(action);
};


export default fieldValidationMiddleware;
