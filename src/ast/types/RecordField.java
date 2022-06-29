package ast.types;

import ast.ast_node.AbstractASTNode;
import visitor.Visitor;

public class RecordField extends AbstractASTNode {

    public String name;
    public Type type;
    public int offset = 0;

    public RecordField(int line, int column, String name, Type type) {
        super(line, column);

        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "RecordField{" +
                "name='" + name + '\'' +
                ", type=" + type.toString() +
                '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordField that = (RecordField) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
