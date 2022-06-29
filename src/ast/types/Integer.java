package ast.types;

import visitor.Visitor;

public class Integer extends AbstractType {

    public Integer(int line, int column) {
        super(line, column);
    }

    @Override
    public String toString() {
        return "Integer{}";
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

    @Override
    public String getDataType() {
        return "int";
    }

    @Override
    public Type arithmetic(Type type) {
        if (type instanceof ErrorType) return type; // return the actual error
        if (this.getClass().equals(type.getClass()))
            return this; // We return an INTEGER

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
    public Type logical(Type type) {
        if (type instanceof ErrorType) return type; // return the actual error
        if (this.getClass().equals(type.getClass()))
            return this; // We return an INTEGER

        return super.logical(type); // show an error message
    }

    @Override
    public Type comparison(Type type) {
        if (type instanceof ErrorType) return type; // return the actual error
        if (this.getClass().equals(type.getClass()))
            return this; // We return an INTEGER

        return super.comparison(type); // show an error message
    }

    @Override
    public Type minus() {
        return this; // We return an INTEGER
    }

    @Override
    public Type not() {
        return this;
    }

    @Override
    public Type modulus(Type type) {
        if (type instanceof ErrorType) return type; // return the actual error
        if (this.getClass().equals(type.getClass()))
            return this; // We return an INTEGER

        return super.comparison(type); // show an error message
    }

    @Override
    public Type asBoolean() {
        return this;
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
        return 2;
    }

    @Override
    public String suffix() {
        return "i";
    }

    @Override
    public String convertTo(Type type) {
        if (type instanceof Integer) return ""; // no conversion needed
        else if (type instanceof Character) return "i2b";
        else if (type instanceof Double) return "i2f";
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
