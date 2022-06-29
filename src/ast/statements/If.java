package ast.statements;

import ast.ast_node.AbstractASTNode;
import ast.expressions.Expression;
import visitor.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class If extends AbstractASTNode implements Statement {

    public Expression expression;
    public List<Statement> ifStatements;
    public List<Statement> elseStatements;

    public If(int line,
              int column,
              Expression expression,
              List<Statement> ifStatements) {
        super(line, column);

        this.expression = expression;
        this.ifStatements = ifStatements;
        this.elseStatements = new ArrayList<>();
    }

    public If(int line,
              int column,
              Expression expression,
              List<Statement> ifStatements,
              List<Statement> elseStatements) {
        super(line, column);

        this.expression = expression;
        this.ifStatements = ifStatements;
        this.elseStatements = elseStatements;
    }

    @Override
    public String toString() {
        return "If{" +
                "expression=" + expression +
                ", ifStatements=" +
                    this.ifStatements
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(", ", "{", "}"))
                + ", elseStatements=" +
                    this.ifStatements
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
