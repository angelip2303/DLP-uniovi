package ast.program;

import ast.ast_node.ASTNode;
import ast.types.Type;

public interface Definition extends ASTNode {

    Type getType();
    String getName();
    int getScope();
    void setScope(int scope);

}
