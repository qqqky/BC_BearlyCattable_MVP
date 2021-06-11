package app;

import java.util.regex.Pattern;

/*
 * Basic input checker for REGISTRATION. Rules (after String.trim()) are the following:
 * 1. only letters(ASCII), digits and underscores are allowed
 * 2. in total, not more than 2 digits or underscores are allowed
 * 3. cannot start with letter or underscore
 * 
 * Additional checks: non-null, non-empty, not ending with underscore
 */

public class BasicInputCheck {
	public static final Pattern BASIC_INPUT_CHECK = Pattern.compile("^.*[^0-9A-Z_a-z]{1,}.*$");
	public static final Pattern NO_UNDERSCORE_OR_DIGIT_ABUSE = Pattern.compile("^.*(_|\\d).*(_|\\d).*(_|\\d).*$");
	public static final Pattern NO_UNDERSCORE_OR_DIGIT_START = Pattern.compile("^(_|\\d){1,}.*$");
	
	public static void main(String[] args) {
		BasicInputCheck.verify(null);
		BasicInputCheck.verify("");
		BasicInputCheck.verify("123john");
		BasicInputCheck.verify("john%");
		BasicInputCheck.verify(" efewfwefw"); //OK, because input is trimmed
		BasicInputCheck.verify("ewgewg_ewfwef"); //OK
		BasicInputCheck.verify("_fwfefewf");
		BasicInputCheck.verify("fewfewf___");
		BasicInputCheck.verify("fewfewf++-");
		BasicInputCheck.verify("ewf efew");
		BasicInputCheck.verify("john5_2");
		BasicInputCheck.verify("john5g2g2g2g2g");
		BasicInputCheck.verify("john_2"); //OK
		BasicInputCheck.verify("john22"); //OK
		BasicInputCheck.verify("john_22"); //NO, only for spawns
		
	}
	public static final boolean verify(String input) {
		
		return input != null 
				&& !BasicInputCheck.BASIC_INPUT_CHECK.matcher(input.trim()).matches() 
				&& !BasicInputCheck.NO_UNDERSCORE_OR_DIGIT_START.matcher(input.trim()).matches()
				&& !BasicInputCheck.NO_UNDERSCORE_OR_DIGIT_ABUSE.matcher(input).matches()
				&& !input.equals("") 
				&& !input.endsWith("_");	
	}
}
