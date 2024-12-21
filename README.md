# V2Engine

V2Engine is a desktop version of the V2Runtime which was made for Hudder (A minecraft mod that allows one to modify their game's hud to their liking).

The V2Runtime started as a simple markup language lacking very critical parts of a complete programming language (functions, arrays, etc.).
But as time passed I added more and more features to the point where it was not only turing complete, it had almost all the features of a complete programming language.

And so, I decided to make a desktop fork of it. It was fairly easy since the V2Runtime itself was mostly detached from the game and used removable abstraction layers.


This is pure Java, no helper dependencies or smt.

##### Although it is based on Hudder, uses the same runtime and mostly has the same code, the syntax is not at all the same and there are some added features which I did not put into hudder (explained later).



# Installation methods:

Requires Java 21 to be installed, [JRE 21 For windows users](https://www.openlogic.com/openjdk-downloads?field_java_parent_version_target_id=828&field_operating_system_target_id=436&field_architecture_target_id=391&field_java_package_target_id=401).

1. (Unix) Using the install script remotely: `bash <(wget -qO- https://raw.githubusercontent.com/NGSpace/V2Engine/refs/heads/main/install.sh)`
2. (Unix) Using the install script locally: Download it from git and run it
3. (Unix) Manual installation: Download the shell script and jar and put them both in the desired installation folder and add the folder to PATH (optional)
4. (Windows) Embrace extend extinguish: Use WSL
5. (Windows) Privacy: Install Linux Mint
6. (Windows) Manual installation: Fine, the jar is crossplatform, use it on your shitty operating system


# Differences between V2Engine and Hudder:

1. Methods don't exist
2. Functions can now be created without always returning a value
3. All the minecraft-related functions no longer exist.
4. "import" keyword now exists in-place of the "load" method.
5. Variables no longer need to be wrapped in {}
6. All text is now treated as statements.
7. The return method is now just a keyword.
8. So is the break variable.
9. All statements are seperated by a ;.
10. Basic conditions don't exist anymore.

# Example:

v2engine.v2:
```
import ~/Desktop/import.v2;

r=11;
log(str(r,true));
log(testfunc("20cm"));
warn("I love colors");
err("angy");
```

import.v2:
```
#def testfunc, test
	return "result=" + pp;
	
#if true
	log("test");
```

# why?

Because I fucking can.
