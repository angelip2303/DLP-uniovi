package ast.expressions;

import ast.ast_node.AbstractASTNode;
import ast.program.Definition;
import visitor.Visitor;

public class Variable extends AbstractExpression {

    public String name;
    public Definition definition;

    public Variable(int line, int column, String name) {
        super(line, column);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

}
