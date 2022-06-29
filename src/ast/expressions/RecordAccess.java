package ast.expressions;

import visitor.Visitor;

public class RecordAccess extends AbstractExpression {

    public String name;
    public Expression expression;

    public RecordAccess(int line, int column, String name, Expression expression) {
        super(line, column);

        this.name = name;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "RecordAccess{" +
                "name='" + name + '\'' +
                ", expression=" + expression.toString() +
                '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);    }

}
