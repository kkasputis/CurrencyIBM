package ce.service;


import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;


@Service
public class ScheduleService {

	@Autowired
	CurrencyService currencyService;
	
	@Async
	@Scheduled(cron = "0 0 8 * * *")
	public void getNewCurrencyRates() {

		try {
			currencyService.getLatestRates();
		} catch (ParserConfigurationException e) {
			System.out.println("Could not get latest rates..." + e);
			e.printStackTrace();
		} catch (SAXException e) {
			System.out.println("Could not get latest rates..." + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not get latest rates..." + e);
			e.printStackTrace();
		}

		
	}
}
