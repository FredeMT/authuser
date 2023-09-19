package specifications;

import org.springframework.data.jpa.domain.Specification;

import com.ead.authuser.models.UserModel;

import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

public class SpecificationTemplate {
	//Definir 3 filtros/specifications combinados com AND:
	@And({ 
	@Spec(path = "userType", spec = Equal.class),	//filtro que busca por um userType igual ao fornecido.
	@Spec(path = "userStatus", spec = Equal.class),	//filtro que busca por um userStatus igual ao fornecido.
	@Spec(path = "email", spec = Like.class),	//filtro que busca por um email que contenha a string fornecida.
	@Spec(path = "fullName", spec = Like.class)	//filtro que busca por um fullName que contenha a string fornecida.
	})
	public interface UserSpec extends Specification<UserModel> {}

}
