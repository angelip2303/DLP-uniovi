package ast.types;

import ast.TypeCheckingFactory;
import visitor.Visitor;

public class Double extends AbstractType {

    public Double(int line, int column) {
        super(line, column);
    }

    @Override
    public String toString() {
        return "Double{}";
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

    @Override
    public String getDataType() {
        return "double";
    }

    @Override
    public Type arithmetic(Type type) {
        if (type instanceof ErrorType) return type; // return the actual error
        if (this.getClass().equals(type.getClass()))
            return this; // We return a DOUBLE

        return super.arithmetic(type);
    }

    @Override
    public Type cast(Type type) {
        if (type instanceof ErrorType) return type; // return the actual error
        else if (type instanceof Integer) return type;
        else if (type instanceof Character) return type;
        else if (type instanceof Double) return type;
        else return super.cast(type); // throw an error
    }

    @Override
    public Type comparison(Type type) {
        if (type instanceof ErrorType) return type; // return the actual error
        if (this.getClass().equals(type.getClass()))
            return TypeCheckingFactory.getIntegerType(); // We return an INTEGER

        return super.comparison(type); // show an error message
    }

    @Override
    public Type minus() {
        return this; // We return a DOUBLE
    }

    @Override
    public Type readable() {
        return this;
    }

    @Override
    public Type writable() {
        return this;
    }

    @Override
    public int numberOfBytes() {
        return 4;
    }

    @Override
    public String suffix() {
        return "f";
    }

    @Override
    public String convertTo(Type type) {
        if (type instanceof Integer) return "f2i";
        else if (type instanceof Character) return "f2i\n\ti2b";
        else if (type instanceof Double) return ""; // no conversion needed
        else
            throw new IllegalStateException(
                    String.format(
                            "Cannot perform the conversion from %s to %s.",
                            this.getDataType(),
                            type.getDataType()
                    )
            );
    }

    @Override
    public String promoteToInt() {
        return "";
    }

}