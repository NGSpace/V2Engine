package io.github.ngspace.v2engine.v2runtime;

import java.util.HashMap;
import java.util.Map;

import io.github.ngspace.v2engine.utils.CompileException;
import io.github.ngspace.v2engine.v2runtime.functions.IV2Function;
import io.github.ngspace.v2engine.v2runtime.functions.V2FunctionHandler;
import io.github.ngspace.v2engine.v2runtime.values.AV2Value;
import io.github.ngspace.v2engine.v2runtime.values.DefaultV2VariableParser;
import io.github.ngspace.v2engine.v2runtime.values.IV2VariableParser;

public abstract class AV2Compiler {

	public static Map<String, Object> variables = new HashMap<String, Object>();
	public Map<String, V2Runtime> runtimes = new HashMap<String, V2Runtime>();
	public Map<String, Object> tempVariables = new HashMap<String, Object>();
	public V2FunctionHandler functionHandler = new V2FunctionHandler();
	protected IV2VariableParser variableParser = new DefaultV2VariableParser();
	public boolean SYSTEM_VARIABLES_ENABLED = true;
	
	
	
	/**
	 * Tokenize the provided string to a AV2Value instance.
	 * @param runtime - The V2Runtime.
	 * @param string - The string to tokenize.
	 * @param line - The line at which the string is tokenized
	 * @param col - The col at which the string is tokenized
	 * @returns The tokenized AV2Value
	 * @throws CompileException
	 */
	public AV2Value getV2Value(V2Runtime runtime, String string, int line, int col) throws CompileException {
		return getVariableParser().parse(runtime, string, this, line, col);
	}

	/** @return The variable parser used by this compiler. */
	public IV2VariableParser getVariableParser() {return variableParser;}
	
	/** Sets the variable parser used by this compiler. */
	public void setVariableParser(IV2VariableParser parser) {variableParser = parser;}
	
	

	public final Object compile(String text, String filename)
			throws CompileException {
		V2Runtime runtime = runtimes.get(text);
		if (runtime==null) runtimes.put(text, (runtime=buildRuntime(text, new CharPosition(-1, -1), filename, null)));
		return runtime.execute().returnValue;
	}
	public V2Runtime buildRuntime(String text, String filename) throws CompileException {
		return buildRuntime(text, new CharPosition(-1, -1), filename, null);
	}
	
	
	
	public abstract V2Runtime buildRuntime(String text, CharPosition charPosition, String filename,
			V2Runtime scope) throws CompileException;
	


	public void defineFunctionOrMethod(String commands, String[] args, String name, CharPosition pos, String filename)
			throws CompileException {
		V2Runtime runtime = buildRuntime(commands, pos, filename, null);
		
		functionHandler.bindFunction((IV2Function) (funcruntime,funcname,vals,line,charpos) -> {
			if (vals.length<args.length) throw new CompileException("Not enough arguments", pos.line, pos.charpos);
			for (int i = 0;i<vals.length;i++) {
				Object v = vals[i].get();
				runtime.putScoped("arg"+(i+1), v);
				runtime.putScoped(args[i].trim().toLowerCase(), v);
			}
			try {
				return runtime.execute().returnValue;
			} catch (CompileException e) {
				throw new CompileException("Method "+name+" threw an error: \n"+e.getFailureMessage(),line,charpos);
			}
		}, name);
	}

	
	public void put(String key, Object value) {variables.put(key, value);}
	public Object get(String key) {return variables.get(key);}
	
	protected CharPosition getPosition(int ind, String string) {
		int line = 0;
		int charpos = 0;
		
		for (int i = 0;i<ind;i++) {
			if (string.charAt(i)=='\n') {
				line++;
				charpos = 0;
				continue;
			}
			charpos++;
		}
		return new CharPosition(line, charpos);
	}
	protected CharPosition getPosition(CharPosition charPosition, int ind, String j) {
		int line = charPosition.line;
		int charpos = charPosition.charpos;
		if (line==-1||charpos==-1) {
			line = 0;
			charpos = 0;
		}
		
		for (int i = 0;i<ind;i++) {
			if (j.charAt(i)=='\n') {
				line++;
				charpos = 0;
				continue;
			}
			charpos++;
		}
		return new CharPosition(line, charpos);
	}
	public static class CharPosition {
		public final int line;
		public final int charpos;
		public CharPosition(int line, int charpos) {
			this.line = line;
			this.charpos = charpos;
		}
	}
}
