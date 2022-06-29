package ast.expressions;

import ast.statements.Statement;
import visitor.Visitor;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionInvocation extends AbstractExpression implements Statement {

    public Variable variable;
    public List<Expression> arguments;

    public FunctionInvocation(int line, int column, String name, List<Expression> arguments) {
        super(line, column);

        this.variable = new Variable(line, column, name); // encapsulation of the creation of the variable
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "FunctionInvocation{" +
                "variable=" + variable.toString() +
                ", arguments=" +
                    this.arguments
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
