package ast.types;

import ast.ast_node.ASTNode;

import java.util.List;

public interface Type extends ASTNode {

    Type arithmetic(Type type);

    Type cast(Type type);

    Type comparison(Type type);

    Type parenthesis(List<Type> argTypes);

    Type squareBrackets(Type type);

    Type logical(Type type);

    Type dot(String name);

    Type minus();

    Type not();

    Type modulus(Type type);

    Type assign(Type type);

    Type asBoolean();

    Type readable();

    Type writable();

    Type mustBeSubType(Type type);

    String getDataType();

    int numberOfBytes();

    String suffix();

    String convertTo(Type type);

    String promoteToInt();

}
