package ast;

import ast.types.Array;
import ast.types.Type;

public class ArrayTypeFactory {

    public static Array createArray(Type type, int size) {
        if (type instanceof Array) { // we want to expand the array in more dimensions...
            var array = (Array) type; // the type IS an array
            array.type = createArray(array.type, size); // we create the array in a recursive way
            return array; // return the actual array
        } else // if we are creating a one-dimensional array
            return new Array(type.getLine(), type.getColumn(), size, type); // create it and return
    }

}
