package com.remotelab.externalApi.mappers;

public interface Mapper<A, B> {
    B mapTo(A a);
    A mapFrom(B b);
}

