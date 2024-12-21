package io.github.ngspace.v2engine.v2runtime.functions;

import java.io.IOException;

import io.github.ngspace.v2engine.V2Engine;
import io.github.ngspace.v2engine.utils.CompileException;
import io.github.ngspace.v2engine.utils.ObjectWrapper;
import io.github.ngspace.v2engine.v2runtime.AV2Compiler;

public class LoadMethod implements IV2VoidFunction {
	@Override
	public void invoke(AV2Compiler comp, String type, int line, int charpos, ObjectWrapper... args)
			throws CompileException {
		String file = args[0].asString();
		
		try {
			AV2Compiler ecompiler=V2Engine.v2compiler;
			ecompiler.compile(V2Engine.reader.getFile(file), file);
		} catch (CompileException e) {
			throw new CompileException(e.getFailureMessage() +"\nExecution failed for " + file, line, charpos);
		} catch (IOException e) {
			e.printStackTrace();
			throw new CompileException(e.getMessage() +"\nExecution failed for " + file, line, charpos);
		}
	}
}
