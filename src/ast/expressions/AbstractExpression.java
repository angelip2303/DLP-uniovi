package ast.expressions;

import ast.ast_node.AbstractASTNode;
import ast.types.Type;

public abstract class AbstractExpression extends AbstractASTNode implements Expression {

    private boolean lValue;
    private Type type;

    public AbstractExpression(int line, int column) {
        super(line, column);
    }

    public boolean getLvalue() {
        return !lValue;
    }

    public void setLvalue(boolean lValue) {
        this.lValue = lValue;
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

}
