package ast.types;

import ast.TypeCheckingFactory;
import visitor.Visitor;

public class Character extends AbstractType {

    public Character(int line, int column) {
        super(line, column);
    }

    @Override
    public String toString() {
        return "Character{}";
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

    @Override
    public Type arithmetic(Type type) {
        if (type instanceof ErrorType) return type; // return the actual error
        if (type instanceof Integer) return type; // allow operations with an integer
        if (this.getClass().equals(type.getClass()))
            return TypeCheckingFactory.getIntegerType(); // We return an INTEGER

        return super.arithmetic(type); // throw an error
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
        return TypeCheckingFactory.getIntegerType(); // we return an integer
    }

    @Override
    public Type modulus(Type type) { // TODO check this is right
        if (type instanceof ErrorType) return type; // return the actual error
        if (type instanceof Integer) return type; // allow operations with an integer
        if (this.getClass().equals(type.getClass()))
            return TypeCheckingFactory.getIntegerType(); // We return an INTEGER

        return super.arithmetic(type); // throw an error
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
    public String getDataType() {
        return "char";
    }

    @Override
    public int numberOfBytes() {
        return 1;
    }

    @Override
    public String suffix() {
        return "b";
    }

    @Override
    public String convertTo(Type type) {
        if (type instanceof Integer) return "b2i";
        else if (type instanceof Character) return ""; // no conversion needed
        else if (type instanceof Double) return "b2i\n\ti2f";
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
        return "b2i";
    }

}