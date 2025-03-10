package io.github.ngspace.v2engine.v2runtime.runtime_elements;

import java.util.Arrays;

import io.github.ngspace.v2engine.utils.CompileException;
import io.github.ngspace.v2engine.utils.CompileState;
import io.github.ngspace.v2engine.v2runtime.AV2Compiler;
import io.github.ngspace.v2engine.v2runtime.V2Runtime;
import io.github.ngspace.v2engine.v2runtime.values.AV2Value;

//What a name...
public class ConditionV2RuntimeElement extends AV2RuntimeElement {

	AV2Value[] results = {};
	AV2Value[] conditions = {};
	AV2Compiler compiler;
	boolean hasElse;
	private String filename;
	public ConditionV2RuntimeElement(String[] condArgs, AV2Compiler compiler, V2Runtime runtime,
			int line, int charpos, String filename) throws CompileException {
		this.compiler = compiler;
		this.filename = filename;
		
		hasElse = condArgs.length%2==1;
		for (int i = 0;i<condArgs.length;i++) {
			String str = condArgs[i];
			if (hasElse&&i==condArgs.length-1) {
				results = addToArray(results, compiler.getV2Value(runtime, str, line, charpos));
				break;
			}
			if (i%2==0) conditions = addToArray(conditions, compiler.getV2Value(runtime, str, line, charpos));
			else results = addToArray(results, compiler.getV2Value(runtime, str, line, charpos));
		}
	}
	
	@Override public boolean execute(CompileState meta) throws CompileException {
		boolean executed = false;
		for (int i = 0;i<conditions.length;i++) {
			if (conditions[i].asBoolean()) {
				executed = true;
				compiler.compile(results[i].asString(),filename);
				break;
			}
		}
		if (!executed&&hasElse) compiler.compile(results[results.length-1].asString(),filename);
		return true;
	}

	private static <T> T[] addToArray(T[] arr, T t) {
		T[] newarr = Arrays.copyOf(arr, arr.length+1);
		newarr[arr.length] = t;
		return newarr;
	}
}
