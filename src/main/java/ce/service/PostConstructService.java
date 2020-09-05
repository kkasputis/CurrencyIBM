package ce.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import ce.entity.CurrencyName;
import ce.entity.CurrencyRate;
import ce.repository.CurrencyNameRepository;
import ce.repository.CurrencyRateRepository;

@Service
public class PostConstructService {
	@Autowired
	CurrencyNameRepository currencyNameRepository;
	@Autowired
	CurrencyRateRepository currencyRateRepository;
	@Autowired
	CurrencyService currencyService;
	
	@PostConstruct 
	public void fillDataBase() throws ParserConfigurationException, SAXException, IOException {
		System.out.println("Please wait, the application is starting...");
		currencyRateRepository.deleteAll();
		currencyNameRepository.deleteAll();
		currencyService.getAllCurrencyNames();
		currencyService.getLatestRates();
		CurrencyName eurName = currencyNameRepository.findOneByCode("EUR").orElse(null);
		CurrencyRate eurRate = new CurrencyRate();
		eurRate.setName(eurName);
		eurRate.setRate(new BigDecimal("1"));
		eurRate.setDate(LocalDate.now());
		currencyRateRepository.save(eurRate);
		currencyService.removeNameWithNoData();
	}
}
