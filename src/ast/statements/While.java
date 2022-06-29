package ast.statements;

import ast.ast_node.AbstractASTNode;
import ast.expressions.Expression;
import visitor.Visitor;

import java.util.List;
import java.util.stream.Collectors;

public class While extends AbstractASTNode implements Statement {

    public Expression expression;
    public List<Statement> statements;

    public While(int line, int column, Expression expression, List<Statement> statements) {
        super(line, column);

        this.expression = expression;
        this.statements = statements;
    }

    @Override
    public String toString() {
        return "While{" +
                "expression=" + expression.toString() +
                ", statements=" +
                    this.statements
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(", ", "{", "}"))
                + '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

}
