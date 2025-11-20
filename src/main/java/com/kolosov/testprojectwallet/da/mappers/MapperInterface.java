package com.kolosov.testprojectwallet.da.mappers;

public interface MapperInterface<T, E> {

    E convert(T source);

    T convertReverse(E source);
}
