package ast.expressions;

import visitor.Visitor;

public class Comparison extends AbstractExpression {

    public String operator;
    public Expression leftOperand;
    public Expression rightOperand;

    public Comparison(int line, int column, String operator, Expression leftOperand, Expression rightOperand) {
        super(line, column);

        this.operator = operator;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public String toString() {
        return "Comparison{" +
                "operator='" + operator + '\'' +
                ", leftOperand=" + leftOperand.toString() +
                ", rightOperand=" + rightOperand.toString() +
                '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

}
