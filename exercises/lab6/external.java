package external;

/**
 * This file shows an external visitor for expressions.
 *
 * The fact that it is external becomes visible in the implementations of the
 * `visit` methods. Here `dispatch` is explicitly called on the children.
 *
 * The file also shows that the visitor pattern as presented by Gamma et al.
 * can be split into two different technical parts.
 *
 * a) representing operations on trees as object with methods for each variant.
 * b) dispatching the actual runtime type of the variant to the corresponding
 *    method.
 *
 * To illustrate this difference please note the two types `Visitor` and
 * `Dispatcher`.
 */

interface Exp {}
class Lit implements Exp { int n; Lit(int n) { this.n = n; } }
class Add implements Exp { Exp l, r; Add(Exp l, Exp r) { this.l = l; this.r = r; }}
class Mul implements Exp { Exp l, r; Mul(Exp l, Exp r) { this.l = l; this.r = r; }}

interface Visitor {
  void visit(Lit l);
  void visit(Add a);
  void visit(Mul m);
}

/**
 * Performs the runtime dispatch on the actual type of e and selects the
 * appropriate method in the visitor.
 */
abstract class Dispatcher implements Visitor {
  void dispatch(Exp e) {
    if (e instanceof Add) { visit((Add) e); }
    else if (e instanceof Mul) { visit((Mul) e); }
    else if (e instanceof Lit) { visit((Lit) e); }
  }
}

class Eval extends Dispatcher {

  // This is an implementation artifact due to the result type of the visit
  // methods being `void`. One can also imagine different ways of working
  // around this (like explicitly maintaining a stack).
  int result = 0;

  public void visit(Lit l) {
    result = l.n;
  }
  public void visit(Add a) {
    dispatch(a.l); // explicit call on the left child
    int left = result;
    dispatch(a.r); // explicit call on the right child
    int right = result;
    result = left + right;
  }
  public void visit(Mul m) {
    dispatch(m.l);
    int left = result;
    dispatch(m.r);
    result = left * result;
  }
}

class Test {
  public static void main(String[] args) {
    Exp e = new Add(new Lit(4), new Mul(new Lit(3), new Lit(5)));
    Eval v = new Eval();
    v.dispatch(e);
    System.out.println(v.result);
  }

}