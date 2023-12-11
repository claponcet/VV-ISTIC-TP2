# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

True positive:
*commons-collections\src\main\java\org\apache\commons\collections4\IterableUtils.java:51:        UseUtilityClass:        This utility class has a non-private constructor*

This class has only static methods, we should not be able to instantiate it. However, because it is public and there is no explicit constructor, the class can still be instantiated through the default constructor. We need to define an explicit constructor and make it private to override the default one and make sure the class is never instantiated.

New code:
```java
public class IterableUtils {
    private IterableUtils() {}
}
```

False positive:
*commons-collections\src\main\java\org\apache\commons\collections4\CollectionUtils.java:603:     UnnecessaryFullyQualifiedName:  Unnecessary qualifier 'CollectionUtils': 'isSubCollection' is already in scope*

Code:
```java
public static boolean isProperSubCollection(final Collection<?> a, final Collection<?> b) {
      Objects.requireNonNull(a, "a");
      Objects.requireNonNull(b, "b");
      return a.size() < b.size() && CollectionUtils.isSubCollection(a, b);
  }
```

This code smell does not affect the code but removing this would make the code less explicit for developpers. We thus think that it is a good think to have the class fully qualified and that this code smell is a false positive.
