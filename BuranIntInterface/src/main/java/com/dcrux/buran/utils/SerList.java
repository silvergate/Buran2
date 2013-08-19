package com.dcrux.buran.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Buran.
 *
 * @author: ${USER} Date: 20.08.13 Time: 00:01
 */
public class SerList {

    public static <TTypeX extends Serializable> ISerList<TTypeX> wrap(List<TTypeX> list) {
        return new MyObject<TTypeX>(list);
    }

    private static class MyObject<TType extends Serializable> implements ISerList<TType> {

        private final List<TType> inner;

        public MyObject(List<TType> inner) {
            this.inner = inner;
        }

        public java.util.Iterator<TType> iterator() {
            return this.inner.iterator();
        }

        public Object[] toArray() {
            return this.inner.toArray();
        }

        public <T> T[] toArray(T[] a) {
            return this.inner.toArray(a);
        }

        public boolean addAll(int index, Collection<? extends TType> c) {
            return this.inner.addAll(index, c);
        }


        public TType get(int index) {
            return this.inner.get(index);
        }

        public TType set(int index, TType element) {
            return this.inner.set(index, element);
        }

        public void add(int index, TType element) {
            this.inner.add(index, element);
        }

        public TType remove(int index) {
            return this.inner.remove(index);
        }

        public int indexOf(Object o) {
            return this.inner.indexOf(o);
        }

        public int lastIndexOf(Object o) {
            return this.inner.lastIndexOf(o);
        }

        public java.util.ListIterator<TType> listIterator() {
            return this.inner.listIterator();
        }

        public ListIterator<TType> listIterator(int index) {
            return this.inner.listIterator(index);
        }

        public List<TType> subList(int fromIndex, int toIndex) {
            return this.inner.subList(fromIndex, toIndex);
        }

        public int size() {
            return this.inner.size();
        }

        public boolean isEmpty() {
            return this.inner.isEmpty();
        }

        public boolean contains(Object o) {
            return this.inner.contains(o);
        }


        public boolean add(TType tType) {
            return this.inner.add(tType);
        }

        public boolean remove(Object o) {
            return this.inner.remove(o);
        }

        public boolean containsAll(Collection<?> c) {
            return this.inner.containsAll(c);
        }

        public boolean addAll(Collection<? extends TType> c) {
            return this.inner.addAll(c);
        }

        public boolean removeAll(Collection<?> c) {
            return this.inner.removeAll(c);
        }

        public boolean retainAll(Collection<?> c) {
            return this.inner.retainAll(c);
        }

        public void clear() {
            this.inner.clear();
        }

        public boolean equals(Object o) {
            return this.inner.equals(o);
        }

        public int hashCode() {
            return this.inner.hashCode();
        }

    }
}
