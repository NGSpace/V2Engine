package io.github.ngspace.v2engine.v2runtime.functions;

import java.util.ArrayList;

import io.github.ngspace.v2engine.utils.CompileException;
import io.github.ngspace.v2engine.v2runtime.V2Runtime;
import io.github.ngspace.v2engine.v2runtime.values.AV2Value;

public class ArrayV2Function implements IV2Function {
	
	@Override
	public Object execute(V2Runtime runtime, String functionName, AV2Value[] args, int line, int charpos)
			throws CompileException {
		var lst = new ArrayList<Object>();
		Object o = args[0].get();
		if (o instanceof String str) {
			for (char c : str.toCharArray()) lst.add(c);
		} else if (o instanceof Number b) {
			Object value = 0;
			if (args.length>1) value = args[1].get();
			for (int i = 0;i<b.intValue();i++) {
				lst.add(value);
			}
		}
		return lst;
	}
	
}
