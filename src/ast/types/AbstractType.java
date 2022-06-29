package ast.types;

import ast.ast_node.AbstractASTNode;

import java.util.List;

public abstract class AbstractType extends AbstractASTNode implements Type {

    private boolean lValue;

    public AbstractType(int line, int column) {
        super(line, column);
    }

    public boolean getLvalue() {
        return lValue;
    }

    public void setLvalue(boolean lValue) {
        this.lValue = lValue;
    }

    @Override
    public Type arithmetic(Type type) {
        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                String.format(
                        "%s and %s types are not allowed to perform ARITHMETIC operations",
                        this.getDataType(),
                        type.getDataType()
                )
        );
    }

    @Override
    public Type cast(Type type) {
        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                String.format(
                        "%s and %s types are not allowed to perform CAST operations",
                        this.getDataType(),
                        type.getDataType()
                )
        );
    }

    @Override
    public Type comparison(Type type) {
        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                String.format(
                        "%s and %s types are not allowed to perform COMPARISON operations. They should be the same and built-in.",
                        this.getDataType(),
                        type.getDataType()
                )
        );
    }

    @Override
    public Type parenthesis(List<Type> argTypes) {
        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                "Cannot perform parenthesis operation over a non-function type"
        );
    }

    @Override
    public Type squareBrackets(Type type) {
        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                String.format(
                        "%s and %s types are not allowed to perform INDEXING",
                        this.getDataType(),
                        type.getDataType()
                )
        );
    }

    @Override
    public Type logical(Type type) {
        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                String.format(
                        "%s and %s types are not allowed to perform LOGICAL operations",
                        this.getDataType(),
                        type.getDataType()
                )
        );
    }

    @Override
    public Type dot(String name) {
        return  new ErrorType(
                this.getLine(),
                this.getColumn(),
                "You can only perform RECORD ACCESS over structs."
        );
    }

    @Override
    public Type minus() {
        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                String.format(
                        "%s type cannot perform UNARY MINUS",
                        this.getDataType()
                )
        );
    }

    @Override
    public Type not() {
        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                String.format(
                        "%s type cannot perform UNARY NOT",
                        this.getDataType()
                )
        );
    }

    @Override
    public Type modulus(Type type) {
        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                String.format(
                        "%s and %s types are not allowed to perform MODULUS",
                        this.getDataType(),
                        type.getDataType()
                )
        );
    }

    @Override
    public Type assign(Type type) {
        if (this.getClass().equals(type.getClass())) return this;
        else if (type instanceof ErrorType) return type;
        else
            return new ErrorType(
                    this.getLine(),
                    this.getColumn(),
                    String.format(
                            "Cannot ASSIGN %s and %s types as they are not the same",
                            this.getDataType(),
                            type.getDataType()
                    )
            );
    }

    @Override
    public Type asBoolean() {
        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                String.format(
                        "%s is not a boolean",
                        this.getDataType()
                )
        );
    }

    @Override
    public Type readable() {
        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                String.format(
                        "%s is not readable",
                        this.getDataType()
                )
        );
    }

    @Override
    public Type writable() {
        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                String.format(
                        "%s is not writable",
                        this.getDataType()
                )
        );
    }

    @Override
    public Type mustBeSubType(Type type) {
        if (this.getClass().equals(type.getClass())) return this;
        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                String.format(
                        "%s and %s are not compatible for return: both should be the same",
                        this.getDataType(),
                        type.getDataType()
                )
        );
    }

    @Override
    public String suffix() {
        // This should never happen as suffix is well-defined for those Types that require it. Checks have already been
        // performed so far in the execution of any program :D
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public String convertTo(Type type) {
        throw new IllegalStateException("Operation not supported :(");
    }

    @Override
    public String promoteToInt() {
        throw new IllegalStateException("Operation not supported :(");
    }

}