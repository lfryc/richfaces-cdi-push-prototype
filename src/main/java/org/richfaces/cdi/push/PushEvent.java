package org.richfaces.cdi.push;

import java.lang.annotation.Annotation;

import javax.enterprise.util.TypeLiteral;

public interface PushEvent<T> {
    /**
     * <p>
     * Fires an event with the specified qualifiers and notifies observers.
     * </p>
     * 
     * @param event the event object
     * @throws IllegalArgumentException if the runtime type of the event object contains a type variable
     */
    public void fire(T event);

    /**
     * <p>
     * Obtains a child <tt>Event</tt> for the given additional required qualifiers.
     * </p>
     * 
     * @param qualifiers the additional specified qualifiers
     * @return the child <tt>Event</tt>
     * @throws IllegalArgumentException if passed two instances of the same qualifier type, or an instance of an annotation that
     *         is not a qualifier type
     */
    public PushEvent<T> select(Annotation... qualifiers);

    /**
     * <p>
     * Obtains a child <tt>Event</tt> for the given required type and additional required qualifiers.
     * </p>
     * 
     * @param <U> the specified type
     * @param subtype a {@link java.lang.Class} representing the specified type
     * @param qualifiers the additional specified qualifiers
     * @return the child <tt>Event</tt>
     * @throws IllegalArgumentException if passed two instances of the same qualifier type, or an instance of an annotation that
     *         is not a qualifier type
     */
    public <U extends T> PushEvent<U> select(Class<U> subtype, Annotation... qualifiers);

    /**
     * <p>
     * Obtains a child <tt>Event</tt> for the given required type and additional required qualifiers.
     * </p>
     * 
     * @param <U> the specified type
     * @param subtype a {@link javax.enterprise.util.TypeLiteral} representing the specified type
     * @param qualifiers the additional specified qualifiers
     * @return the child <tt>Event</tt>
     * @throws IllegalArgumentException if passed two instances of the same qualifier type, or an instance of an annotation that
     *         is not a qualifier type
     */
    public <U extends T> PushEvent<U> select(TypeLiteral<U> subtype, Annotation... qualifiers);
}
