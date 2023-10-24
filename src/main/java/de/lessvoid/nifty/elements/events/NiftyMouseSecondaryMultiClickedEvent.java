/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;


/**
 * The Class NiftyMouseSecondaryMultiClickedEvent.
 */
public class NiftyMouseSecondaryMultiClickedEvent extends NiftyMouseBaseEvent {
    
    /** The click count. */
    private final int clickCount;

    /**
	 * Instantiates a new nifty mouse secondary multi clicked event.
	 *
	 * @param element the element
	 */
    public NiftyMouseSecondaryMultiClickedEvent(Element element) {
        super(element);
        clickCount = 0;
    }

    /**
	 * Instantiates a new nifty mouse secondary multi clicked event.
	 *
	 * @param element    the element
	 * @param source     the source
	 * @param clickCount the click count
	 */
    public NiftyMouseSecondaryMultiClickedEvent(final Element element, @Nonnull final NiftyMouseInputEvent source, int clickCount) {
        super(element, source);
        this.clickCount = clickCount;
    }

    /**
	 * How many click.
	 *
	 * @return the clickCount
	 */
    public int getClickCount() {
        return clickCount;
    }
}
