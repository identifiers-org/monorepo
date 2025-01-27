// Helper function for HATEOAS requests: fetches subobjects.
export const fetchAndAdd = async function (parent, links, init, addId = false) {
  const resources = await Promise.all(links.map(link => fetch(link.url, init)));
  const jsonResources = await Promise.all(resources.map(res => res.ok ? res.json() : undefined));

  jsonResources.forEach((resource, index) => {
    parent[links[index].name] = resource;
    if (addId) {
      parent[links[index].name]['id'] = resource._links.self.href;
    }
  });

  return parent;
}
