package org.identifiers.cloud.hq.ws.registry.data.services;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.Person;
import org.identifiers.cloud.hq.ws.registry.data.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.services
 * Timestamp: 2019-03-28 10:11
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This service implements complex persistence operations for Persons
 */
@Service
@Slf4j
public class PersonService {

    // Repository
    @Autowired
    private PersonRepository repository;


    /**
     * Register a person if not registered.
     * @param person the person to register
     * @return the registered person
     * @throws PersonServiceException
     */
    @Transactional
    public Person registerPerson(Person person) throws PersonServiceException {
        log.info("Locating person by e-mail '{}'", person.getEmail());
        Person registeredPerson = repository.findByEmail(person.getEmail());
        if (registeredPerson == null) {
            // TODO Check that the provided e-mail is valid
            registeredPerson = repository.save(person);
            log.info("NEW Person REGISTRATION, with internal ID '{}'", registeredPerson.getId());
        }
        return registeredPerson;
    }
}
