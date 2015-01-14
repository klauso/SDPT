package externalresult;

/**
 * The contents of this file build on `externalAccept.java`. We introduce a
 * generic type parameter `R` (for "Result") to use as a return type of
 * visitor methods instead of `void`.
 *
 * Changing the interface of `Visitor` propagates to both `Dispatcher` as well
 * as expressions. Both of them now also need to carry type parameters as
 * can seen below.
 */
interface Exp {
  <R> R accept(Visitor<R> v);
}
class Lit implements Exp {
  int n; Lit(int n) { this.n = n; }
  public <R> R accept(Visitor<R> v) { return v.visit(this); }
}
class Add implements Exp {
  Exp l, r; Add(Exp l, Exp r) { this.l = l; this.r = r; }
  public <R> R accept(Visitor<R> v) { return v.visit(this); }
}
class Mul implements Exp {
  Exp l, r; Mul(Exp l, Exp r) { this.l = l; this.r = r; }
  public <R> R accept(Visitor<R> v) { return v.visit(this); }
}

interface Visitor<R> {
  R visit(Lit l);
  R visit(Add a);
  R visit(Mul m);
}

abstract class Dispatcher<R> implements Visitor<R> {
  R dispatch(Exp e) { return e.accept(this); }
}

// Now passing the result around as a mutable field is not necessary anymore.
// It is `Integer` not `int` since only reference types can be passed as
// type argument to generics.
class Eval extends Dispatcher<Integer> {

  public Integer visit(Lit l) {
    return l.n;
  }
  public Integer visit(Add a) {
    return dispatch(a.l) + dispatch(a.r);
  }
  public Integer visit(Mul m) {
    return dispatch(m.l) * dispatch(m.r);
  }
}

class Test {
  public static void main(String[] args) {
    Exp e = new Add(new Lit(4), new Mul(new Lit(3), new Lit(5)));
    Eval v = new Eval();
    System.out.println(v.dispatch(e));
  }
}