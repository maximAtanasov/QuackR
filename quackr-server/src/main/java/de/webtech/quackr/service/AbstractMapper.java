package de.webtech.quackr.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractMapper<T, E> {
    public abstract T map(E entity);

    public Collection<T> map(Collection<E> entities){
        List<T> result = new ArrayList<>();
        for(E e : entities){
            result.add(map(e));
        }
        return result;
    }
}
