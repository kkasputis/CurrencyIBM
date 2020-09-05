package ce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import ce.entity.CurrencyName;



@RepositoryRestResource
@CrossOrigin(origins = "http://localhost:4200")
public interface CurrencyNameRepository extends JpaRepository<CurrencyName, Long>{
	
	Optional<CurrencyName> findOneByCode(String code);
}
