package com.rom4ik.service;

import com.rom4ik.model.Book;
import com.rom4ik.model.Person;
import com.rom4ik.repo.BooksRepository;
import com.rom4ik.repo.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author rom4ik
 */
@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll(int page, int itemsPerPage, boolean sortByYear) {
        return booksRepository.findAll(
                PageRequest.of(
                        page,
                        itemsPerPage,
                        getSortingMethod(sortByYear))
        ).getContent();
    }

    private Sort getSortingMethod(boolean sortByYear) {
        Sort sorting;
        if(sortByYear) {
            sorting = Sort.by("publicationYear");
        } else {
            sorting = Sort.unsorted();
        }

        return sorting;
    }

    public Book findById(int id) {
        Optional<Book> optionalBook = booksRepository.findById(id);

        return optionalBook.orElse(null);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        List<Book> books;

        if(person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            books = person.get().getBooks();
            checkIfHoldingTimeExpired(books);
        } else {
            books = Collections.emptyList();
        }

        return books;
    }

    private void checkIfHoldingTimeExpired(List<Book> books) {
        ZonedDateTime dateTenDaysAgo = ZonedDateTime.now().minusDays(10);

        for(Book book : books) {
            if(book.getBorrowDate().toInstant().isBefore(dateTenDaysAgo.toInstant())) {
                book.setHoldingTimeExpired(true);
            }
        }
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        booksRepository.assign(id, selectedPerson, new Date());
    }

    @Transactional
    public void release(int id) {
        booksRepository.release(id);
    }

    public List<Book> search(String title) {
        return booksRepository.findAllByNameStartingWith(title);
    }
}
