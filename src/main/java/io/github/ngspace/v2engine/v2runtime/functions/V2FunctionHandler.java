package io.github.ngspace.v2engine.v2runtime.functions;

import java.util.HashMap;
import java.util.Map;

import io.github.ngspace.v2engine.V2Engine;
import io.github.ngspace.v2engine.utils.CompileException;

public class V2FunctionHandler {
	private static Map<String, IV2Function> functions = new HashMap<String,IV2Function>();
	
	public V2FunctionHandler() {bindAllAPIFunctions();}

	public void bindAllAPIFunctions() {
		
		//Type casting
		
		bindFunction(new DoubleV2Function(), 1, "int", "num", "number", "double");
		bindFunction(new StringV2Function(), 1, 2, "str", "string");
		bindFunction(new ArrayV2Function(), 1, 2, "array");
		bindFunction((r,n,args,l,c) -> (char)(args[0].asDouble()), 1, "char");
		bindFunction((r,n,args,l,c) -> Integer.toBinaryString((int) args[0].asDouble()), 1, "toBinaryString");
		
		//Math
		
		bindFunction(new RngV2Function(), 2, 3, "rng", "random");

		bindFunction((r,n,args,l,c) -> Math.abs  (args[0].asDouble()), 1, "abs"    );
		bindFunction((r,n,args,l,c) -> Math.floor(args[0].asDouble()), 1, "floor"  );
		bindFunction((r,n,args,l,c) -> Math.ceil (args[0].asDouble()), 1, "ceiling");
		
		bindFunction((r,n,args,l,c) -> Math.sin (args[0].asDouble()), 1, "sin" );
		bindFunction((r,n,args,l,c) -> Math.cos (args[0].asDouble()), 1, "cos" );
		bindFunction((r,n,args,l,c) -> Math.tan (args[0].asDouble()), 1, "tan" );
		
		bindFunction((r,n,args,l,c) -> Math.asin(args[0].asDouble()), 1, "asin");
		bindFunction((r,n,args,l,c) -> Math.acos(args[0].asDouble()), 1, "acos");
		bindFunction((r,n,args,l,c) -> Math.atan(args[0].asDouble()), 1, "atan");
		
		bindFunction((r,n,args,l,c) -> Math.sqrt(args[0].asDouble()), 1, "sqrt");
		
		bindFunction((r,n,args,l,c) -> Math.pow(args[0].asDouble(),args[1].asDouble()), 2, "pow");
		bindFunction((r,n,args,l,c) -> Math.min(args[0].asDouble(),args[1].asDouble()), 2, "min");
		bindFunction((r,n,args,l,c) -> Math.max(args[0].asDouble(),args[1].asDouble()), 2, "max");
		
		bindFunction((r,n,args,l,c) -> Math.floor(args[0].asDouble()*Math.pow(10, args[1].asInt()))
				/Math.pow(10, args[1].asInt()),2,"truncate");
		
		// Misc
		
		bindFunction(new LengthV2Function(), 1, "length");
		bindFunction((r,n,args,l,c)->new HashMap<Object, Object>(),0, "map");
		
		//Compiler and Variables
		
		bindFunction(new LoadMethod(), 1, 1, "load", "execute", "compile", "run", "add");
		
		//Logging and errors
		
		bindConsumer((a, t, l, ch, s) -> {throw new CompileException(s[0].asString(), l, ch);}, 1, 1, "throw");
		
		bindConsumer((a, t, l, ch, s) -> V2Engine.log(s[0].get()), 1, 1, "log");
		bindConsumer((a, t, l, ch, s) -> V2Engine.err(s[0].get()), 1, 1, "err");
		bindConsumer((a, t, l, ch, s) -> V2Engine.warn(s[0].get()), 1, 1, "warn");
	}

	public void bindFunction(IV2Function function, String... names) {
		for(String name:names) functions.put(name,function);
	}

	public void bindFunction(IV2Function function, int length, String... names) {
		bindFunction(function, length, length, names);
	}
	public void bindFunction(IV2Function function, int minlength, int maxlength, String... names) {
		IV2Function expandedFunction = (runtime, name, args, line, charpos) -> {
			if (args.length<minlength) throw new CompileException("Too little parameters for "+name+" function!",line,charpos);
			if (args.length>maxlength) throw new CompileException("Too many parameters for "+name+" function!",line,charpos);
			return function.execute(runtime, name, args, line, charpos);
		};
		bindFunction(expandedFunction,names);
	}
	public void bindConsumer(IV2VoidFunction function, int minlength, int maxlength, String... names) {
		bindFunction(function,minlength,maxlength,names);
	}
	
	public IV2Function getFunction(String name) {
		return functions.get(name);
	}
}
