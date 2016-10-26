package pl.spring.demo.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.beans.factory.annotation.Autowired;

import pl.spring.demo.annotation.NullableId;
import pl.spring.demo.common.Sequence;
import pl.spring.demo.dao.BookDao;
import pl.spring.demo.exception.BookNotNullIdException;
import pl.spring.demo.to.BookTo;
import pl.spring.demo.to.IdAware;

public class BookDaoAdvisor implements MethodBeforeAdvice {
    @Autowired
    private Sequence sequence;

    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {

	if (hasAnnotation(method, o, NullableId.class)) {
	    checkNotNullId(objects[0]);

	    BookDao dao = (BookDao) o;
	    BookTo book = (BookTo) objects[0];
	    book.setId(sequence.nextValue(dao.findAll()));

	}
    }

    private void checkNotNullId(Object o) {
	if (o instanceof IdAware && ((IdAware) o).getId() != null) {
	    throw new BookNotNullIdException();
	}
    }

    private boolean hasAnnotation(Method method, Object o, Class annotationClazz) throws NoSuchMethodException {
	boolean hasAnnotation = method.getAnnotation(annotationClazz) != null;

	if (!hasAnnotation && o != null) {
	    hasAnnotation = o.getClass().getMethod(method.getName(), method.getParameterTypes())
		    .getAnnotation(annotationClazz) != null;
	}
	return hasAnnotation;
    }

    public Sequence getSequence() {
	return sequence;
    }

    public void setSequence(Sequence sequence) {
	this.sequence = sequence;
    }
}
