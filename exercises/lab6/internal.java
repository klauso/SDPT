package internal;

/**
 * Building on the preparations in `externalResult.java` where generics
 * are used to express the result type of a visitor we can turn the
 * external visitor into an internal visitor by moving the `dispatch` or
 * `accept` calls to the data structure itself.
 *
 * Please note that internal vs. external and accept vs. explicit dispatching
 * are two orthogonal concepts. That is we could imagine the same code as
 * presented in this file, but without `accept`.
 *
 * Also using generics to represent the result type is orthogonal to *internal
 * vs. external* visitors. It is more convenient to implement internal visitors
 * using generics. Some handins I received manually maintained a stack of
 * results to overcome the limitation of the `void` result type:
 *
 *    For a  concrete visitor every visit-method computes its result and pushes
 *    it on the explicitly maintained stack. visit-methods of nodes with
 *    children then pop of the necessary amount of results.
 *
 * This technique can of course also be used for internal visitors. However,
 * while the implementation in this file is technically very similar, the
 * stack maintenance is performed by the language itself and thus is less
 * error prone.
 */
interface Exp {
  <R> R accept(Visitor<R> v);
}
class Lit implements Exp {
  int n; Lit(int n) { this.n = n; }
  public <R> R accept(Visitor<R> v) { return v.lit(n); }
}
class Add implements Exp {
  Exp l, r; Add(Exp l, Exp r) { this.l = l; this.r = r; }
  public <R> R accept(Visitor<R> v) {
    return v.add(l.accept(v), r.accept(v));
  }
}
class Mul implements Exp {
  Exp l, r; Mul(Exp l, Exp r) { this.l = l; this.r = r; }
  public <R> R accept(Visitor<R> v) {
    return v.mul(l.accept(v), r.accept(v));
  }
}

// The visitor method for addition and multiplication both take the already
// computed results of the children as arguments. So we are forced to explicitly
// name them differently. An alternative would be to also pass the visited
// node as additional argument like: `R visit(Add a, R l, R r);`
interface Visitor<R> {
  R lit(int n);
  R add(R l, R r);
  R mul(R l, R r);
}

class Eval implements Visitor<Integer> {
  public Integer lit(int n) { return n; }
  public Integer add(Integer l, Integer r) { return l + r; }
  public Integer mul(Integer l, Integer r) { return l * r; }
}

class Test {
  public static void main(String[] args) {
    Exp e = new Add(new Lit(4), new Mul(new Lit(3), new Lit(5)));
    Eval v = new Eval();
    System.out.println(e.accept(v));
  }
}