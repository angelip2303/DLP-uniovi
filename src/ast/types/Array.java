package ast.types;

import visitor.Visitor;

public class Array extends AbstractType {

    public int size;
    public Type type;

    public Array(int line, int column, int size, Type type) {
        super(line, column);

        this.size = size;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Array{" +
                "size=" + size +
                ", type=" + type.toString() +
                '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

    @Override
    public String getDataType() {
        return String.format("[%s, %s]", size, type.getDataType());
    }

    @Override
    public Type squareBrackets(Type type) {
        if (type instanceof ErrorType) return type;
        if (type instanceof Integer) return this.type;
        return super.squareBrackets(type);
    }

    @Override
    public int numberOfBytes() {
        return size * type.numberOfBytes();
    }

}
