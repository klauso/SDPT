package externalaccept;

/**
 * This file is a variant of `external.java` showing the difference to using
 * `accept` methods as known from Gamma et al. This is still an external visitor
 * even though the data structure now performs the runtime dispatch:
 * The traversal is still controlled by the visitor.
 */

interface Exp {
  void accept(Visitor v);
}
class Lit implements Exp {
  int n; Lit(int n) { this.n = n; }
  // Here we statically know that the type of this is `Lit`. That is we
  // statically select the method numbered (1).
  // There is no technical difference in naming the methods visitLit and
  // calling these explicitly here.
  public void accept(Visitor v) { v.visit(this); }
}
class Add implements Exp {
  Exp l, r; Add(Exp l, Exp r) { this.l = l; this.r = r; }
  // Same here but with `Add` -> (2) ...
  public void accept(Visitor v) { v.visit(this); }
}
class Mul implements Exp {
  Exp l, r; Mul(Exp l, Exp r) { this.l = l; this.r = r; }
  public void accept(Visitor v) { v.visit(this); }
}

interface Visitor {
  void visit(Lit l); // (1)
  void visit(Add a); // (2)
  void visit(Mul m); // (3)
}

abstract class Dispatcher implements Visitor {
  // Dispatching now is performed by the data structure itself, so we only
  // need to call it here.
  //
  // Someone in class asked why we still need this dispatch method. Can't we
  // just inline `e.accept(this)` everywhere? Yes, you can. Still using dispatch
  // just serves to highlight the connection to `external.java`. Of course in
  // some special cases it might be important because concrete visitors could
  // override the dispatch method to for instance also perform logging before
  // dispatch.
  void dispatch(Exp e) { e.accept(this); }
}

// No difference to `external.java` below this line ...
class Eval extends Dispatcher {

  int result = 0;

  public void visit(Lit l) {
    result = l.n;
  }
  public void visit(Add a) {
    dispatch(a.l);
    int left = result;
    dispatch(a.r);
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