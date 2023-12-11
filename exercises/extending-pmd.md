# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
    ...
    if (...) {
        ...
        if (...) {
            ....
        }
    }

}
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-ISTIC-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer

The code is available in code/Exercise3/README.md

When running it, we obtain quite a lot of results, here are a few:

```
commons-collections\src\main\java\org\apache\commons\collections4\sequence\SequencesComparator.java:266:        nestedIfs:      At least 3 levels of nested if statements are present.
commons-collections\src\main\java\org\apache\commons\collections4\set\CompositeSet.java:376:    nestedIfs:      At least 3 levels of nested if statements are present.
commons-collections\src\main\java\org\apache\commons\collections4\set\CompositeSet.java:381:    nestedIfs:      At least 3 levels of nested if statements are present.
commons-collections\src\main\java\org\apache\commons\collections4\trie\AbstractPatriciaTrie.java:176:   nestedIfs:      At least 3 levels of nested if statements are present.
commons-collections\src\main\java\org\apache\commons\collections4\trie\AbstractPatriciaTrie.java:890:   nestedIfs:      At least 3 levels of nested if statements are present.
commons-collections\src\main\java\org\apache\commons\collections4\trie\AbstractPatriciaTrie.java:1219:  nestedIfs:      At least 3 levels of nested if statements are present.
```

And here is the code matching the last example:
```java
TrieEntry<K, V> previousEntry(final TrieEntry<K, V> start) {
        if (start.predecessor == null) {
            throw new IllegalArgumentException("must have come from somewhere!");
        }

        if (start.predecessor.right == start) {
            if (isValidUplink(start.predecessor.left, start.predecessor)) {
                return start.predecessor.left;
            }
            return followRight(start.predecessor.left);
        }
        TrieEntry<K, V> node = start.predecessor;
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }

        if (node.parent == null) { // can be null if we're looking up root.
            return null;
        }

        if (isValidUplink(node.parent.left, node.parent)) {
            if (node.parent.left == root) {
                if (root.isEmpty()) {
                    return null;
                }
                return root;

            }
            return node.parent.left;
        }
        return followRight(node.parent.left);
    }
```
