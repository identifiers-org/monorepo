//
// Filtering function for pagination.
//
export default (namespaceList, prefixEffectiveValue, suggestionListSize) => namespaceList
  .sort((a, b) => {
    if (a.prefix.startsWith(prefixEffectiveValue) && !b.prefix.startsWith(prefixEffectiveValue)) {
      return -1;
    };

    if (!a.prefix.startsWith(prefixEffectiveValue) && b.prefix.startsWith(prefixEffectiveValue)) {
      return 1;
    }

    return a.prefix - b.prefix;
  })
  .slice(0, suggestionListSize);
