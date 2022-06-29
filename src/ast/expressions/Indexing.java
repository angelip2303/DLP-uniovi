package ast.expressions;

import visitor.Visitor;

public class Indexing extends AbstractExpression {

    public Expression expression;
    public Expression index;

    public Indexing(int line, int column, Expression expression, Expression index) {
        super(line, column);

        this.expression = expression;
        this.index = index;
    }

    @Override
    public String toString() {
        return "Indexing{" +
                "expression=" + expression.toString() +
                ", index=" + index.toString() +
                '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

}
