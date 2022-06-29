package ast.statements;

import ast.ast_node.AbstractASTNode;
import ast.expressions.Expression;
import visitor.Visitor;

public class Assignment extends AbstractASTNode implements Statement {

    public Expression leftHandSide;
    public Expression rightHandSide;

    public Assignment(int line, int column, Expression leftHandSide, Expression rightHandSide) {
        super(line, column);

        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    @Override
    public String toString() {
        return "Assigment{" +
                "leftHandSide=" + leftHandSide.toString() +
                ", rightHandSide=" + rightHandSide.toString() +
                '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

}
