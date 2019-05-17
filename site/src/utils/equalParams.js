// Assumes all fields are in paramsA.
export const equalParams = (paramsA, paramsB) => !(Object.keys(paramsA).map(field => paramsA[field] === paramsB[field]).includes(false));
