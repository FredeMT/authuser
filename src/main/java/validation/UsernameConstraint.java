package validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UsernameConstraintImpl.class) //classe que implementa a annotation.
@Target({ElementType.METHOD, ElementType.FIELD}) //Onde podemos usar a annotation (métodos e campos)
@Retention(RetentionPolicy.RUNTIME) //Quando a validacao vai ocorrer (em tempo de execucão).

public @interface UsernameConstraint {	
	//Definir parâmetros default do bean validadtion:
	String message() default "Invalid username";	//message mostrada quando ocorrer a validacao (o erro).
	Class<?>[] groups() default {}; //grupo de validacao, se precisar definir.
	Class<? extends Payload>[] payload() default {};	//nível que vai ocorrer o erro.

}
