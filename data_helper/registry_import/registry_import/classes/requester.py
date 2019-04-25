class Requester:

    #
    # Constructor.
    #
    def __init__(self, name = None, email = None):
        self.name = name
        self.email = email


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
    requester_empty = Requester()
    print('isComplete():', requester_empty.isComplete())
    print('serialize():', requester_empty.serialize())

    requester_test = Requester('John Doe', 'john@example.com')
    print('isComplete():', requester_test.isComplete())
    print('serialize():', requester_test.serialize())
