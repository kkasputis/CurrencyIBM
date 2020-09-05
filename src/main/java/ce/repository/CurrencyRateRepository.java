package ce.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import ce.entity.CurrencyName;
import ce.entity.CurrencyRate;

@RepositoryRestResource
@CrossOrigin(origins = "http://localhost:4200")
public interface CurrencyRateRepository  extends JpaRepository<CurrencyRate, Long>{

	int countByDate(LocalDate date);
	Optional<CurrencyRate> findTopByNameOrderByDateDesc(CurrencyName name);

}
