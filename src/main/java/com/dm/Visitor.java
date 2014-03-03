package com.dm;

interface Visitor<T> {

    public Visitor<T> visitTree(Tree<T> tree);

    public void visitData(Tree<T> parent, T data);
}
