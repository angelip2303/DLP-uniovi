package ast.expressions;

import ast.types.Type;
import visitor.Visitor;

public class Cast extends AbstractExpression {

    public Type castType;
    public Expression expression;

    public Cast(int line, int column, Type castType, Expression expression) {
        super(line, column);

        this.castType = castType;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "Cast{" +
                "castType=" + castType.toString() +
                ", expression=" + expression.toString() +
                '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);

    }

}
