package io.github.ngspace.v2engine.v2runtime.runtime_elements;

import io.github.ngspace.v2engine.utils.CompileException;
import io.github.ngspace.v2engine.utils.CompileState;
import io.github.ngspace.v2engine.v2runtime.AV2Compiler;
import io.github.ngspace.v2engine.v2runtime.V2Runtime;
import io.github.ngspace.v2engine.v2runtime.values.AV2Value;

public class VariableV2RuntimeElement extends AV2RuntimeElement {
	
	final AV2Value value;
	final AV2Compiler compiler;
	
	public VariableV2RuntimeElement(String value, AV2Compiler compiler, V2Runtime runtime, int line, int charpos) throws CompileException {
		this.compiler = compiler;
		this.value = compiler.getV2Value(runtime, value, line, charpos);
	}

	@Override public boolean execute(CompileState meta) throws CompileException {
		value.get();
		return true;
	}
}