package ast.statements;

import ast.ast_node.AbstractASTNode;
import ast.expressions.Expression;
import visitor.Visitor;

public class Read extends AbstractASTNode implements Statement {

    public Expression expression;

    public Read(int line, int column, Expression expression) {
        super(line, column);
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "Read{" +
                "expression=" +  this.expression + '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }
}
