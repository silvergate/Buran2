package com.dcrux.buran.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 14:16
 */
public class SerSet {

    public static <TType extends Serializable> ISerSet<TType> wrap(Set<TType> set) {
        return new MyObject<TType>(set);
    }

    private static class MyObject<TType extends Serializable> implements ISerSet<TType> {

        private MyObject(Set<TType> wrapped) {
            this.wrapped = wrapped;
        }

        private Set<TType> wrapped;

        public int size() {
            return this.wrapped.size();
        }

        public boolean isEmpty() {
            return this.wrapped.isEmpty();
        }

        public boolean contains(Object o) {
            return this.wrapped.contains(o);
        }

        public Iterator<TType> iterator() {
            return this.wrapped.iterator();
        }

        public Object[] toArray() {
            return this.wrapped.toArray();
        }

        public <T> T[] toArray(T[] a) {
            return this.wrapped.toArray(a);
        }

        public boolean add(TType tType) {
            return this.wrapped.add(tType);
        }

        public boolean remove(Object o) {
            return this.wrapped.remove(o);
        }

        public boolean containsAll(Collection<?> c) {
            return this.wrapped.containsAll(c);
        }

        public boolean addAll(Collection<? extends TType> c) {
            return this.wrapped.addAll(c);
        }

        public boolean retainAll(Collection<?> c) {
            return this.wrapped.retainAll(c);
        }

        public boolean removeAll(Collection<?> c) {
            return this.wrapped.removeAll(c);
        }

        public void clear() {
            this.wrapped.clear();
        }

        public boolean equals(Object o) {
            return this.wrapped.equals(o);
        }

        public int hashCode() {
            return this.wrapped.hashCode();
        }

        /*public int size() {
            return this.wrapped.size();
        }

        public boolean isEmpty() {
            return this.wrapped.isEmpty();
        }

        public boolean contains(Object o) {
            return this.wrapped.contains(o);
        }

        @org.jetbrains.annotations.NotNull
        public Iterator<TType> iterator() {
            return this.wrapped.iterator();
        }

        @org.jetbrains.annotations.NotNull
        public Object[] toArray() {
            return this.wrapped.toArray();
        }

        @org.jetbrains.annotations.NotNull
        public <T> T[] toArray(T[] a) {
            return this.wrapped.toArray(a);
        }

        public boolean add(TType tType) {
            return this.wrapped.add(tType);
        }

        public boolean remove(Object o) {
            return this.wrapped.remove(o);
        }

        public boolean containsAll(Collection<?> c) {
            return this.wrapped.containsAll(c);
        }

        public boolean addAll(Collection<? extends TType> c) {
            return this.wrapped.addAll(c);
        }

        public boolean removeAll(Collection<?> c) {
            return this.wrapped.removeAll(c);
        }

        public boolean retainAll(Collection<?> c) {
            return this.wrapped.retainAll(c);
        }

        public void clear() {
            this.wrapped.clear();
        }

        public boolean equals(Object o) {
            return this.wrapped.equals(o);
        }

        public int hashCode() {
            return this.wrapped.hashCode();
        }

        public Iterator<TType> iterator() {
            return this.wrapped.iterator();
        }*/
    }
    //private Set<TType> wrapped;

}
