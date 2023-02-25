package com.rom4ik.repo;

import com.rom4ik.model.Book;
import com.rom4ik.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

/**
 * @author rom4ik
 */
@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    @Modifying
    @Query("update Book b set b.owner = null where b.id = :id")
    void release(@Param(value = "id") int id);

    @Modifying
    @Query("update Book b set b.owner = :selectedPerson, b.borrowDate = :date where b.id = :id")
    void assign(@Param(value = "id") int id,
                @Param(value = "selectedPerson") Person selectedPerson,
                @Param(value = "date") Date date);

    List<Book> findAllByNameStartingWith(String startingWith);
}
