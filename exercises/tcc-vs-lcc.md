# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

In order to have the TCC and LCC metrics produce the same value, the graph generated from this Java class must be a complete graph. In other words, all the methods must have at least one common instance variable with every other method.

Here is an exemple of a class for which the TCC and LCC metrics are identical:

``` java
public class Example {
  public int a;
  public int b;
  public int c;

  public Example(int a, int b, int c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }

  public int firstMethod() {
    return a + b;
  }

  public int secondMethod() {
    return b + c;
  }

  public int thirdMethod() {
    return c + a;
  }  
}
```

Here is the graph related to this method: 

<img width="273" alt="image" src="https://github.com/claponcet/VV-ISTIC-TP2/assets/tcc_lcc.png">


We can then calculate TCC and LCC metrics:

TCC = 3/3 = 1
LCC = 3/3 = 1

Both metrics produce equal values.
