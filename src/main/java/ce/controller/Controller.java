package ce.controller;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ce.entity.CurrencyName;
import ce.repository.CurrencyNameRepository;
import ce.service.CurrencyService;




@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {
     
	@Autowired
	CurrencyNameRepository currencyNameRepository;
	@Autowired
	CurrencyService currencyService;
	
	@RequestMapping(value = "/getnames")	
	public List<CurrencyName> getCurrencyNames(HttpServletResponse response) {
		System.out.println(new BigDecimal("1").divide(new BigDecimal("1"),6, RoundingMode.HALF_UP).toPlainString());
	return currencyNameRepository.findAll();
	}
	
	
	@RequestMapping(value = "/calculate", method = RequestMethod.POST)
	public Map<String,String> calculate(@RequestBody Map<String, String> map, HttpServletResponse response) {
		return currencyService.calculate(map.get("sum"), map.get("from"), map.get("to"));

	}
}
