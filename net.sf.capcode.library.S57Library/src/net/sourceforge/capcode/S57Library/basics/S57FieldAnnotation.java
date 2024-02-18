/**
 * 
 */
package net.sourceforge.capcode.S57Library.basics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author cyrille
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface S57FieldAnnotation {
	String name();
	String setter() default "";
	
}
