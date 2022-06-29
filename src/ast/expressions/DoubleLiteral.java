package ast.expressions;

import visitor.Visitor;

public class DoubleLiteral extends AbstractExpression {

    public double value;

    public DoubleLiteral(int line, int column, double value) {
        super(line, column);
        this.value = value;
    }

    @Override
    public String toString() {
        return "DoubleLiteral{" +
                "value=" + value +
                '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

}
