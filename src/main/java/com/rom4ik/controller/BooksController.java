package com.rom4ik.controller;

import com.rom4ik.model.Book;
import com.rom4ik.model.Person;
import com.rom4ik.service.BooksService;
import com.rom4ik.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author rom4ik
 */
@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(name = "books_per_page", defaultValue = "3") int booksPerPage,
                        @RequestParam(name = "sort_by_year", defaultValue = "false") boolean sortByYear) {
        List<Book> books = booksService.findAll(page, booksPerPage, sortByYear);
        model.addAttribute("books", books);
        return "/books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "/books/new";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/books/new";
        }

        booksService.save(book);
        return "redirect:books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model,
                           @ModelAttribute("person") Person person) {
        Book book = booksService.findById(id);
        Person owner = book.getOwner();

        if(owner != null) {
            model.addAttribute("owner", owner);
        } else {
            List<Person> people = peopleService.findAll();
            model.addAttribute("people", people);
        }

        model.addAttribute("book", book);
        return "/books/bookInfo";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        Book book = booksService.findById(id);
        model.addAttribute("book", book);
        return "/books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if(bindingResult.hasErrors()) {
            return "/books/edit";
        }
        booksService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int id,
                             @ModelAttribute("person") Person selectedPerson) {
        booksService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        booksService.release(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search() {
        return "/books/search";
    }

    @PostMapping("/search")
    public String search(@RequestParam(name = "title") String title, Model model) {
        List<Book> foundBooks = booksService.search(title);

        model.addAttribute("books", foundBooks);
        return "/books/search";
    }
}
