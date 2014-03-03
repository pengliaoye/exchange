package com.dm;

interface Visitable<T> {

    public void accept(Visitor<T> visitor);
}
