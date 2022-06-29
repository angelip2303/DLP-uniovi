package ast.expressions;

import ast.ast_node.ASTNode;
import ast.types.Type;

public interface Expression extends ASTNode {

    boolean getLvalue();
    void setLvalue(boolean lvalue);
    Type getType();
    void setType(Type type);

}
