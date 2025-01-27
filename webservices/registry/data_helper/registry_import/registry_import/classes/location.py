class Location:

    #
    # Constructor.
    #
    def __init__(self, countryCode = None, countryName = None):
        self.countryCode = countryCode
        self.countryName = countryName


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
    location_empty = Location()
    print('isComplete():', location_empty.isComplete())
    print('serialize():', location_empty.serialize())

    location_test = Location('UK', 'United Kingdom')
    print('isComplete():', location_test.isComplete())
    print('serialize():', location_test.serialize())
