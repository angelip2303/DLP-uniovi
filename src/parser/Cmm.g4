grammar Cmm;

@header {
    import ast.*;
    import ast.expressions.*;
    import ast.program.*;
    import ast.statements.*;
    import ast.types.*;
}

// +-----------------+
// | -*- program -*- |
// +-----------------+

program returns [Program ast]
        locals  [List<Definition> aux = new ArrayList<>()]:
        (definition { $aux.addAll($definition.ast); } )* mainFunction EOF
            {
                $aux.add($mainFunction.ast);
                $ast = new Program(0, 0, $aux);
            }
        ;

// +---------------------+
// | -*- definitions -*- |
// +---------------------+

definition returns [List<Definition> ast = new ArrayList<>()]:
        returnType name=ID '(' parameters ')' functionBody
            {
                $ast.add(new FunctionDefinition(
                    $returnType.ast.getLine(),
                    $returnType.ast.getColumn(),
                    $name.text,
                    $functionBody.ast,
                    new FunctionType(
                        $returnType.ast.getLine(),
                        $returnType.ast.getColumn(),
                        $name.text,
                        $returnType.ast,
                        $parameters.ast
                    )
                ));
            }
        | variableDefinition
            { $ast.addAll($variableDefinition.ast); }
        ;

variableDefinition returns [List<VariableDefinition> ast = new ArrayList<>()]:
        t1=type name1=ID
            { $ast.add(new VariableDefinition(
                $name1.getLine(),
                $name1.getCharPositionInLine() + 1,
                $name1.text,
                $t1.ast));
            }
        (',' name2=ID
            { $ast.add(new VariableDefinition(
               $name1.getLine(),
               $name1.getCharPositionInLine() + 1,
               $name2.text,
               $t1.ast));
            }
        )* ';'
        ;

mainFunction returns [FunctionDefinition ast]: typeID='void' name='main' '(' ')' functionBody
            {
                $ast = new FunctionDefinition(
                    $typeID.getLine(),
                    $typeID.getCharPositionInLine() + 1,
                    $name.text,
                    $functionBody.ast,
                    new FunctionType(
                        $typeID.getLine(),
                        $typeID.getCharPositionInLine() + 1,
                        $name.text,
                        new ast.types.Void($typeID.getLine(), $typeID.getCharPositionInLine() + 1),
                        new ArrayList()
                    )
                );
            }
        ;

functionBody returns [List<Statement> ast = new ArrayList<>()]: '{' (variableDefinition
            { $ast.addAll($variableDefinition.ast); }
        )* (statement
            { $ast.addAll($statement.ast); }
        )* '}'
        ;

// +-------------------+
// | -*- statement -*- |
// +-------------------+

statement returns [List<Statement> ast = new ArrayList<>()]:
        e1=expression '=' e2=expression ';' // assignment
            { $ast.add(new Assignment($e1.ast.getLine(), $e1.ast.getColumn(), $e1.ast, $e2.ast)); }
        | aux='while' c1=condition b1=block // while
            { $ast.add(new While($aux.getLine(), $aux.getCharPositionInLine() + 1, $c1.ast, $b1.ast)); }
        | aux='if' c1=condition b1=block // if-else: we have to do it this way to avoid the 'dangling else problem'
            { $ast.add(new If($aux.getLine(), $aux.getCharPositionInLine() + 1, $c1.ast, $b1.ast)); }
        | aux='if' c1=condition b1=block 'else' b2=block
            { $ast.add(new If($aux.getLine(), $aux.getCharPositionInLine() + 1, $c1.ast, $b1.ast, $b2.ast)); }
        | aux='read' expressions ';' // read
            { $expressions.ast
                .forEach(e -> $ast.add(new Read(
                    $aux.getLine(),
                    $aux.getCharPositionInLine() + 1,
                    e
                )));
            }
        | aux='write' expressions ';' // write
            { $expressions.ast
                .forEach(e -> $ast.add(new Write(
                    $aux.getLine(),
                    $aux.getCharPositionInLine() + 1,
                    e
                )));
            }
        | aux='return' e1=expression ';' // return
            { $ast.add(new Return($aux.getLine(), $aux.getCharPositionInLine() + 1, $e1.ast)); }
        | invocation ';' // function & procedure invocation
            { $ast.add($invocation.ast); }
        ;

// +--------------------+
// | -*- expression -*- |
// +--------------------+

expression returns [Expression ast]: // ordered by precedence (high to low)
        '(' e1=expression ')' // parenthesis
            { $ast = $e1.ast;  }
        | e1=expression '[' e2=expression ']' // indexing
            { $ast =  new Indexing($e1.ast.getLine(), $e1.ast.getColumn(), $e1.ast, $e2.ast); }
        | e1=expression '.' ID // field access
            { $ast = new RecordAccess(
                $e1.ast.getLine(),
                $e1.ast.getColumn(),
                $ID.text,
                $e1.ast);
            }
        | '(' builtIn ')' e1=expression
            { $ast = new Cast($e1.ast.getLine(), $e1.ast.getColumn(), $builtIn.ast, $e1.ast); }
        | '-' e1=expression // unary minus
            { $ast = new UnaryMinus($e1.ast.getLine(), $e1.ast.getColumn(), $e1.ast); }
        | '!' e1=expression // negation
            { $ast = new UnaryNot($e1.ast.getLine(), $e1.ast.getColumn(), $e1.ast); }
        | e1=expression op=('*' | '/' | '%') e2=expression // multiplication, division & modulus
            { $ast = ArithmeticExpressionFactory.createArithmetic(
                $e1.ast.getLine(),
                $e1.ast.getColumn(),
                $op.text,
                $e1.ast,
                $e2.ast);
            }
        | e1=expression op=('+' | '-') e2=expression // addition & subtraction
            { $ast = new Arithmetic(
                $e1.ast.getLine(),
                $e1.ast.getColumn(),
                $op.text,
                $e1.ast,
                $e2.ast);
            }
        | e1=expression op=('>' | '>=' | '<' | '<=' | '!=' | '==') e2=expression // comparison
            { $ast = new Comparison(
                $e1.ast.getLine(),
                $e1.ast.getColumn(),
                $op.text,
                $e1.ast,
                $e2.ast);
            }
        | e1=expression op=('&&' | '||') e2=expression // logical
            { $ast = new Logical(
                $e1.ast.getLine(),
                $e1.ast.getColumn(),
                $op.text,
                $e1.ast,
                $e2.ast);
            }
        | invocation // function invocation
            { $ast = $invocation.ast; }
        | ID
            { $ast = new Variable($ID.getLine(), $ID.getCharPositionInLine() + 1, $ID.text); }
        | i=INT_CONSTANT
            { $ast = new IntLiteral(
                        $i.getLine(),
                        $i.getCharPositionInLine() + 1,
                        LexerHelper.lexemeToInt($i.text));
            }
        | r=REAL_CONSTANT
            { $ast = new DoubleLiteral(
                        $r.getLine(),
                        $r.getCharPositionInLine() + 1,
                        LexerHelper.lexemeToReal($r.text));
            }
        | c=CHAR_CONSTANT
            { $ast = new CharacterLiteral(
                        $c.getLine(),
                        $c.getCharPositionInLine() + 1,
                        LexerHelper.lexemeToChar($c.text));
            }
        ;

// +--------------+
// | -*- type -*- |
// +--------------+

type returns [Type ast]: returnType
            { $ast = $returnType.ast; }
        | t1=type '[' size=INT_CONSTANT ']' {
            $ast = ArrayTypeFactory.createArray($t1.ast, LexerHelper.lexemeToInt($size.text));
        } // we define the array type
        | structType
            { $ast = $structType.ast; }
        ;

structType returns [Struct ast]:
        aux='struct' '{' structFields '}'
            { $ast = new Struct(
                $aux.getLine(),
                $aux.getCharPositionInLine() + 1,
                $structFields.ast
            ); }
        ;

returnType returns [Type ast]: builtIn
            { $ast = $builtIn.ast; }
        | aux='void'
            { $ast = new ast.types.Void($aux.getLine(), $aux.getCharPositionInLine() + 1); }
        ;

builtIn returns [Type ast]: aux='int'
            { $ast = new ast.types.Integer($aux.getLine(), $aux.getCharPositionInLine() + 1); }
        | aux='char'
            { $ast = new ast.types.Character($aux.getLine(), $aux.getCharPositionInLine() + 1); }
        | aux='double'
            { $ast = new ast.types.Double($aux.getLine(), $aux.getCharPositionInLine() + 1); }
        ;

// +-------------------+
// | -*- utilities -*- |
// +-------------------+

condition returns [Expression ast]: '(' expression ')'
            { $ast = $expression.ast; }
        ;

block returns [List<Statement> ast = new ArrayList<>()]:
        '{' (s1=statement { $ast.addAll($s1.ast); })* '}'
        | s2=statement
            { $ast.addAll($s2.ast); }
        ;

invocation returns [FunctionInvocation ast]: ID '(' a1=arguments ')'
            { $ast = new FunctionInvocation($ID.getLine(), $ID.getCharPositionInLine(), $ID.text, $a1.ast); }
        ;

arguments returns [List<Expression> ast = new ArrayList<>()]:
        | e1=expression { $ast.add($e1.ast); } (',' e2=expression { $ast.add($e2.ast); })*
        ;

expressions returns [List<Expression> ast = new ArrayList<>()]:
        e1=expression { $ast.add($e1.ast); } (',' e2=expression { $ast.add($e2.ast); })*
        ;

structFields returns [List<RecordField> ast = new ArrayList<>()]:
        ( structField { $ast.addAll($structField.ast); } )*
        ;

structField returns [List<RecordField> ast = new ArrayList<>()]:
        type name1=ID
            { RecordField record = new RecordField(
                                         $type.ast.getLine(),
                                         $type.ast.getColumn(),
                                         $name1.text,
                                         $type.ast);

             if (!$ast.contains(record))
                $ast.add(record);
             else
                new ErrorType(
                       $type.ast.getLine(),
                       $name1.getCharPositionInLine() + 1,
                       "Record: " + $name1.text + " has already been defined"
                   ); }
        (',' name2=ID
            { record = new RecordField(
                             $type.ast.getLine(),
                             $type.ast.getColumn(),
                             $name2.text,
                             $type.ast);

             if (!$ast.contains(record))
                $ast.add(record);
             else
                new ErrorType(
                       $type.ast.getLine(),
                       $name2.getCharPositionInLine() + 1,
                       "Record: " + $name2.text + " has already been defined"
                   ); }
        )* ';'
        ;

parameters returns [List<VariableDefinition> ast = new ArrayList<VariableDefinition>()]:
        | bt1=builtIn id1=ID
            { $ast.add(new VariableDefinition(
                $id1.getLine(),
                $id1.getCharPositionInLine() + 1,
                $id1.text,
                $bt1.ast));
            }
        (',' bt2=builtIn id2=ID
            { $ast.add(new VariableDefinition(
                $id2.getLine(),
                $id2.getCharPositionInLine() + 1,
                $id2.text,
                $bt2.ast));
            }
        )*
        ;

// +----------------+
// | -*- tokens -*- |
// +----------------+

COMMENT: '//' (.)*? (EOF | '\n') -> skip
        ;

MULTI_LINE_COMMENT: '/*' (.)*? '*/' -> skip
        ;

ID: (LETTER | '_') (LETTER | DIGIT | '_')*
        ;

INT_CONSTANT: [1-9] DIGIT*
        | DIGIT
        ;

REAL_CONSTANT: DECIMAL
        | (DECIMAL | INT_CONSTANT) [eE] [+-]? INT_CONSTANT
        ;

CHAR_CONSTANT: '\'' CHARACTER '\''
        ;

fragment CHARACTER: .
        | '\\' INT_CONSTANT
        | '\\n'
        | '\\t'
        ;

fragment LETTER: [a-zA-Z]
        ;

fragment DIGIT: [0-9]
        ;

fragment DECIMAL: INT_CONSTANT? '.' DIGIT+
        | INT_CONSTANT '.' DIGIT*
        ;

WB: [\t\n\r ] -> skip
        ;