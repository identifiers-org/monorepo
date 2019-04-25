from classes.location import Location
from classes.institution import Institution


class Provider:

    #
    # Constructor.
    #
    def __init__(self,
                 mirId = None,
                 urlPattern = None,
                 name = None,
                 description = None,
                 official = None,
                 providerCode = None,
                 sampleId = None,
                 resourceHomeUrl = None,
                 location = None,
                 institution = None,
                 namespace = None
    ):
        self.mirId = mirId
        self.urlPattern = urlPattern
        self.name = name
        self.description = description
        self.official = official
        self.providerCode = providerCode
        self.sampleId = sampleId
        self.resourceHomeUrl = resourceHomeUrl
        self.location = location
        self.institution = institution
        self.namespace = namespace



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
    provider_empty = Provider()
    print('isComplete():', provider_empty.isComplete())
    print('serialize():', provider_empty.serialize())

    location_uk = Location('uk', 'United Kingdom')
    institution_ebi = Institution('EMBL-EBI', 'http://ebi.ac.uk', 'EBI description', location_uk)

    provider_test = Provider('MIR:00000001',
                             'http://www.uniprot.org/{$id}',
                             'EBI UniProt',
                             'uniprot at EBI description',
                             True,
                             'main',
                             'P01448',
                             'http://www.ebi.ac.uk',
                             institution_ebi,
                             location_uk)
    print('isComplete():', provider_test.isComplete())
    print('serialize():', provider_test.serialize())

