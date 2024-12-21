package io.github.ngspace.v2engine.v2runtime.runtime_elements;

import io.github.ngspace.v2engine.utils.CompileException;
import io.github.ngspace.v2engine.utils.CompileState;
import io.github.ngspace.v2engine.v2runtime.AV2Compiler;
import io.github.ngspace.v2engine.v2runtime.V2Runtime;
import io.github.ngspace.v2engine.v2runtime.AV2Compiler.CharPosition;
import io.github.ngspace.v2engine.v2runtime.values.AV2Value;

public class WhileV2RuntimeElement extends AV2RuntimeElement {
	
	private AV2Value condition;

	public WhileV2RuntimeElement(String condition, String cmds, AV2Compiler compiler, V2Runtime runtime,
			CharPosition charPosition, String filename) throws CompileException {
		this.condition = compiler.getV2Value(runtime, condition, charPosition.line, charPosition.charpos);
		this.nestedRuntime = compiler.buildRuntime(cmds, new CharPosition(charPosition.line, 1), filename, runtime);
	}
	
	@Override public boolean execute(CompileState meta) throws CompileException {
		while (condition.asBoolean()) {
			CompileState res = nestedRuntime.execute();
			if (res.hasReturned) meta.setReturnValue(res.returnValue);
		}
		return true;
	}
}
