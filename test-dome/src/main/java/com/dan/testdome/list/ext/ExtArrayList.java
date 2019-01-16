package com.dan.testdome.list.ext;

/**
 * @fileName: ExtArrayList
 * @author: Dan
 * @createDate: 2018-09-05 13:46.
 * @description: 扩展ArrayList
 */
public class ExtArrayList<E> implements ExtList<E> {

    @Override
    public boolean add(E o) {
        return false;
    }

    @Override
    public boolean add(int index, E o) {
        return false;
    }
}
