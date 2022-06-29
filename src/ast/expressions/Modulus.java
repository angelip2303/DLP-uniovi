package ast.expressions;

import visitor.Visitor;

public class Modulus extends AbstractExpression {

    public Expression leftOperand;
    public Expression rightOperand;

    public Modulus(int line, int column, Expression leftOperand, Expression rightOperand) {
        super(line, column);

        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public String toString() {
        return "Modulus{" +
                ", leftOperand=" + leftOperand.toString() +
                ", rightOperand=" + rightOperand.toString() +
                '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

}
