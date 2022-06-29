package ast.statements;

import ast.ast_node.AbstractASTNode;
import ast.expressions.Expression;
import visitor.Visitor;

public class Write extends AbstractASTNode implements Statement {

    public Expression expression;

    public Write(int line, int column, Expression expression) {
        super(line, column);
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "Write{" +
                "expression=" +
                    this.expression + '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

}
