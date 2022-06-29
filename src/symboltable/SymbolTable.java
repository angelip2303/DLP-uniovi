package symboltable;

import ast.program.Definition;

import java.util.*;

public class SymbolTable {
	
	private int scope = 0; // acts like a pointer to the table
	private List<Map<String, Definition>> table;

	public SymbolTable()  {
		this.table = new ArrayList<>();
		this.table.add(new HashMap<>()); // we have to create a new scope
	}

	public void set() {
		this.scope++; // we are entering a deeper scope
		this.table.add(new HashMap<>()); // we have to create a new scope
	}
	
	public void reset() {
		this.scope--; // we left the scope we entered: go a step upwards the nesting
		table.remove(table.size() - 1); // and remove the last nesting also
	}
	
	public boolean insert(Definition definition) {
		definition.setScope(scope); // we update the scope of the definition
		// In case we find the definition we are trying to insert in THE CURRENT scope we cannot insert
		if (findInCurrentScope(definition.getName()) != null) return false;
		else {
			// We insert the definition to its corresponding scope
			table.get(scope).put(definition.getName(), definition);
			return find(definition.getName()) != null; // return if we have found the definition
		}
	}
	
	public Definition find(String id) {
		Definition definition = null;
		for (int i = scope; i >= 0; i--) { // we iterate from scope outwards
			if (definition != null) break; // if we found an element: stop iterating
			definition = table.get(i).get(id);
		}
		return definition; // null if not found
	}

	public Definition findInCurrentScope(String id) {
		return table.get(scope).get(id);
	}
}
