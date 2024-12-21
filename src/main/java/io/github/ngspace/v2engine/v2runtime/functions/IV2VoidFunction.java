package io.github.ngspace.v2engine.v2runtime.functions;

import io.github.ngspace.v2engine.utils.CompileException;
import io.github.ngspace.v2engine.utils.ObjectWrapper;
import io.github.ngspace.v2engine.v2runtime.AV2Compiler;
import io.github.ngspace.v2engine.v2runtime.V2Runtime;
import io.github.ngspace.v2engine.v2runtime.values.AV2Value;

@FunctionalInterface
public interface IV2VoidFunction extends IV2Function {
	
	/**
	 * Called when the first parameter passed to MetaCompiler has been a command registered with this object.
	 * @param config - The config used
	 * @param meta - The current meta
	 * @param compiler - The compiler invoking this method
	 * @param args - the parameters supplied to this method
	 * @throws CompileException - if the method is not called properly or is unable to execute.
	 */
	public void invoke(AV2Compiler comp, String type, int line, int charpos, ObjectWrapper... args)
			throws CompileException;

	public default Object execute(V2Runtime runtime, String name, AV2Value[] args, int line, int charpos)
			throws CompileException {
		invoke(runtime.compiler, name, line, charpos, args);
		return null;
	}
}
