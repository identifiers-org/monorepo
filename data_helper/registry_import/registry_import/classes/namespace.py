class Namespace:

    #
    # Constructor.
    #
    def __init__(self,
                 prefix = None,
                 mirId = None,
                 name = None,
                 pattern = None,
                 description = None,
                 sampleId = None
    ):
        self.prefix = prefix
        self.mirId = mirId
        self.name = name
        self.pattern = pattern
        self.description = description
        self.sampleId = sampleId


    #
    # String method.
    #
    def __repr__(self):
        return str(self.__dict__)


    #
    # Completeness checker.
    #
    def isComplete(self):
        return all([getattr(self, var) for var in vars(self)])


    #
    # Serializer.
    #
    def serialize(self):
        return self.__dict__


if __name__ == '__main__':
    namespace_empty = Namespace()
    print('isComplete():', namespace_empty.isComplete())
    print('serialize():', namespace_empty.serialize())

    namespace_test = Namespace('uniprot',
                               'mirId',
                               'UniProt',
                               'P[0-9]{5}',
                               'Uniprot description',
                               'P01448')

    print('isComplete():', namespace_test.isComplete())
    print('serialize():', namespace_test.serialize())
