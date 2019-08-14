const fieldValidationMiddleware = ({ dispatch, getState }) => next => action => {

  if (!action.type.includes('/SET_VALUE')) {
    return next(action);
  }

  const validator = validateField()

  return next(action);
};


export default fieldValidationMiddleware;
