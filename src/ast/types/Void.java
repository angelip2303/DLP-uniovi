package ast.types;

import ast.ast_node.AbstractASTNode;
import visitor.Visitor;

public class Void extends AbstractType {

    public Void(int line, int column) {
        super(line, column);
    }

    @Override
    public String toString() {
        return "Void{}";
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

    @Override
    public String getDataType() {
        return "Void";
    }

    @Override
    public int numberOfBytes() {
        return 0;
    }

}
