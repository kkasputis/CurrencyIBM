package ce.security;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ce.controller.Controller;
import ce.service.CurrencyService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeviceTest {
	



    @Autowired
    private CurrencyService currencyService;
    @Autowired
	private Controller controller;

	@Test
	public void contexLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

    @Test
	public void calculationTest() {

 	    Map<String, String> response = currencyService.calculate("1", "AAB", "BBA");
 	   Map<String, String> response2 = currencyService.calculate("1", "EUR", "USD");
 	  
  	  assertThat(response.get("result").equals("Error finding currency name"));
  	 assertThat(response2.get("result").equals(response2.get("rate")));
    }
}