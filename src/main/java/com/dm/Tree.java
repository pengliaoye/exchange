package com.dm;

import java.util.LinkedHashSet;
import java.util.Set;

class Tree<T> implements Visitable<T> {

    // NB: LinkedHashSet preserves insertion order
    private final Set<Tree> children = new LinkedHashSet<Tree>();
    private final T data;

    Tree(T data) {
        this.data = data;
    }

    public void accept(Visitor<T> visitor) {
        visitor.visitData(this, data);

        children.stream().forEach((child) -> {
            Visitor<T> childVisitor = visitor.visitTree(child);
            child.accept(childVisitor);
        });
    }

    Tree child(T data) {
        for (Tree child: children ) {
            if (child.data.equals(data)) {
                return child;
            }
        }

        return child(new Tree(data));
    }

    Tree child(Tree<T> child) {
        children.add(child);
        return child;
    }
}
