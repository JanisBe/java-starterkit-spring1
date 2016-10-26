package pl.spring.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.spring.demo.exception.BookNotNullIdException;
import pl.spring.demo.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "CommonServiceTest-context.xml")
public class BookServiceImplTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setUp() {
	cacheManager.getCache("booksCache").clear();
    }

    @Test
    public void testShouldFindAllBooks() {
	// when
	List<BookTo> allBooks = bookService.findAllBooks();
	// then
	assertNotNull(allBooks);
	assertFalse(allBooks.isEmpty());
	assertEquals(7, allBooks.size());
    }

    @Test
    @Ignore
    public void testShouldFindAllBooksByTitle() {
	// given
	final String title = "Opium w rosole";
	// when
	List<BookTo> booksByTitle = bookService.findBooksByTitle(title);
	// then
	assertNotNull(booksByTitle);
	assertFalse(booksByTitle.isEmpty());
    }

    @Test(expected = BookNotNullIdException.class)
    public void testShouldThrowBookNotNullIdException() {
	// given
	final BookTo bookToSave = new BookTo();
	bookToSave.setId(22L);
	// when
	bookService.saveBook(bookToSave);
	// then
	fail("test should throw BookNotNullIdException");
    }

    @Test
    public void testShouldHaveGoodIdForNewBook() {
	// given
	final BookTo newBook = new BookTo(null, "Testowa książka", "Testowy autor");
	// when
	bookService.saveBook(newBook);
	long newId = newBook.getId();
	// then
	assertEquals(6L, newId);

    }
}
