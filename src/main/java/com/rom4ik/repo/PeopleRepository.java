package com.rom4ik.repo;

import com.rom4ik.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rom4ik
 */
@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
}
