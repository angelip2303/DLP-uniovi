package ast.program;

import ast.statements.Statement;
import ast.types.Type;
import visitor.Visitor;

public class VariableDefinition extends AbstractDefinition implements Statement {

    public int offset = 0;

    public VariableDefinition(int line, int column, String name, Type type) {
        super(line, column, name, type);
    }

    @Override
    public String toString() {
        return "VariableDefinition{" +
                "name='" + getName() + '\'' +
                ", type=" + getType().toString() +
                '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

}
