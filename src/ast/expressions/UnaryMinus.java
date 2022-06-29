package ast.expressions;

import visitor.Visitor;

public class UnaryMinus extends AbstractExpression {

    public Expression expression;

    public UnaryMinus(int line, int column, Expression expression) {
        super(line, column);
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "UnaryMinus{" +
                "expression=" + expression.toString() +
                '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

}
