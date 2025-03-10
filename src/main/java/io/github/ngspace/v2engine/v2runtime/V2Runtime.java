package io.github.ngspace.v2engine.v2runtime;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import io.github.ngspace.v2engine.V2Engine;
import io.github.ngspace.v2engine.utils.CompileException;
import io.github.ngspace.v2engine.utils.CompileState;
import io.github.ngspace.v2engine.v2runtime.runtime_elements.AV2RuntimeElement;

public class V2Runtime {
	public final AV2Compiler compiler;
	protected V2Runtime scope;
	/**
	 * Should stay mostly unused for now.
	 */
	public static final Object VOID = new Void();
	private static class Void {
		@Override public boolean equals(Object obj) {return obj == this || obj == null;}
		@Override public int hashCode() {return super.hashCode();}
		@Override public String toString() {return "null";}
	}
	public V2Runtime(AV2Compiler compiler, V2Runtime scope) {this.compiler = compiler;this.scope = scope;}
	
	protected AV2RuntimeElement[] elements = new AV2RuntimeElement[0];
	public CompileState compileState;
	
	public CompileState execute() throws CompileException {
		compileState = new CompileState();
		for (AV2RuntimeElement element : elements) {
			if (!element.execute(compileState)||compileState.hasReturned) {
				compileState.hasBroken = true;
				break;
			}
		}
		return compileState;
	}
	
	public void addRuntimeElement(AV2RuntimeElement element) {elements = addToArray(elements, element);}
	public AV2RuntimeElement[] getElements() {return elements;}
	
	public static <T> T[] addToArray(T[] arr, T t) {
		T[] newarr = Arrays.copyOf(arr, arr.length+1);
		newarr[arr.length] = t;
		return newarr;
	}
	
	HashMap<String, Object> scopedVariables = new HashMap<String, Object>();
	public void putScoped(String name, Object value) {scopedVariables.put(name, value);}
	public Object getScoped(String name) {
		Object object = scopedVariables.get(name);
		if (object==null&&scope!=null) return scope.getScoped(name);
		return object;
	}

	public Object getVariable(String name) {
		Object object = getScoped(name);
		if (object==null) return compiler.get(name);
		return object;
	}
	public void importFile(String file, int line, int charpos) throws CompileException {
		try {
			compiler.compile(V2Engine.reader.getFile(file), file);
		} catch (CompileException e) {
			throw new CompileException(e.getFailureMessage() +"\nExecution failed for " + file, line, charpos);
		} catch (IOException e) {
			e.printStackTrace();
			throw new CompileException(e.getMessage() +"\nExecution failed for " + file, line, charpos);
		}
		
	}
}