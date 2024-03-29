/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.elements.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;

/**
 * The Class ElementTreeTraverser.
 *
 * @author telamon
 */
public class ElementTreeTraverser implements Iterator<Element>{
    
    /** The iterators. */
    @Nonnull
    private final ArrayList<Iterator<Element>> iterators =  new ArrayList<Iterator<Element>>();
    
    /** The current. */
    private Iterator<Element> current;
    
    /**
	 * Instantiates a new element tree traverser.
	 *
	 * @param e the e
	 */
    public ElementTreeTraverser(@Nonnull Element e){
        current = e.getChildren().listIterator();
    }

    /**
	 * Checks for next.
	 *
	 * @return true, if successful
	 */
    @Override
    public boolean hasNext() {
        return current.hasNext() || !iterators.isEmpty();
    }

    /**
	 * Next.
	 *
	 * @return the element
	 */
    @Override
    public Element next() {
        if(current.hasNext()){
            Element e = current.next();
            if(!e.getChildren().isEmpty())
                iterators.add(e.getChildren().listIterator());

            return e;
        }else if(!iterators.isEmpty()){
            current = iterators.remove(0);
            return next();
        }
        throw new NoSuchElementException();
    }

    /**
	 * Removes the.
	 */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
