from classes.location import Location


class Institution:

    #
    # Constructor.
    #
    def __init__(self, name = None, homeUrl = None, description = None, location = Location()):
        self.name = name
        self.homeUrl = homeUrl
        self.description = description
        self.location = location


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
    institution_empty = Institution()
    print('isComplete():', institution_empty.isComplete())
    print('serialize():', institution_empty.serialize())

    institution_test = Institution('EMBL-EBI',
                                   'http://www.ebi.ac.uk',
                                   'EBI description',
                                   Location('UK', 'United Kingdom')
    )
    print('isComplete():', institution_test.isComplete())
    print('serialize():', institution_test.serialize())
