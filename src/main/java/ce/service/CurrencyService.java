package ce.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ce.entity.CurrencyName;
import ce.entity.CurrencyRate;
import ce.repository.CurrencyNameRepository;
import ce.repository.CurrencyRateRepository;

@Service
public class CurrencyService {
	
	
	@Autowired
	CurrencyNameRepository currencyNameRepository;
	@Autowired
	CurrencyRateRepository currencyRateRepository;

	public void getAllCurrencyNames() throws ParserConfigurationException, SAXException, IOException {
		String url = "http://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrencyList";
		Document doc = getXmlResponseDocument(url);
		NodeList nodeList = doc.getElementsByTagName("CcyNtry");
		for (int x = 0; x < nodeList.getLength(); x++) {
			CurrencyName currencyName = new CurrencyName();
			Node node = nodeList.item(x);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				currencyName.setCode(eElement.getElementsByTagName("Ccy").item(0).getTextContent());
				currencyName.setNameLt(eElement.getElementsByTagName("CcyNm").item(0).getTextContent());
				currencyName.setNameEn(eElement.getElementsByTagName("CcyNm").item(1).getTextContent());
				currencyNameRepository.save(currencyName);

			}
		}
	}



	

	public Map<String,String> calculate(String sum, String from, String to) {
		BigDecimal bigSum = new BigDecimal(sum);
		Map<String, String> response = new HashMap<>();
		CurrencyName nameFrom = currencyNameRepository.findOneByCode(from).orElse(null);
		if (nameFrom == null) {
			response.put("result" ,"Error finding currency name");
			return response;
		}
		CurrencyRate fromRateObject = currencyRateRepository.findTopByNameOrderByDateDesc(nameFrom).orElse(null);
		if (fromRateObject == null) {
			response.put("result" ,"Error finding currency rate");
			return response;
		}
		BigDecimal fromRate = fromRateObject.getRate();
		CurrencyName nameTo = currencyNameRepository.findOneByCode(to).orElse(null);
		if (nameTo == null) {
			response.put("result" ,"Error finding currency name");
			return response;
		}
		CurrencyRate toRateObject = currencyRateRepository.findTopByNameOrderByDateDesc(nameTo).orElse(null);
		if (toRateObject == null) {
			response.put("result" ,"Error finding currency rate");
			return response;
		}
		BigDecimal toRate = toRateObject.getRate();
		BigDecimal bigOne = new BigDecimal("1");
		response.put("result", bigSum.divide(fromRate, 6, RoundingMode.HALF_UP).multiply(toRate).stripTrailingZeros().toPlainString());
		response.put("rate", bigOne.divide(fromRate, 6, RoundingMode.HALF_UP).multiply(toRate).stripTrailingZeros().toPlainString());
		return response;
	}

	public void getLatestRates() throws ParserConfigurationException, SAXException, IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime timeNow = LocalDateTime.now();
		// I don't use /webservices/FxRates/FxRates.asmx/getCurrentFxRates because sometimes
		// it gives dates from the future.
		String url = "http://www.lb.lt/webservices/FxRates/FxRates.asmx/getFxRates?tp=LT&dt="
				+ timeNow.format(formatter);
		int count = currencyRateRepository.countByDate(timeNow.toLocalDate());
		if (count < 1) {
			Document doc = getXmlResponseDocument(url);
			NodeList nodeList = doc.getElementsByTagName("FxRate");
			for (int x = 0; x < nodeList.getLength(); x++) {
				CurrencyRate currencyRate = new CurrencyRate();
				Node node = nodeList.item(x);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					CurrencyName currency = currencyNameRepository
							.findOneByCode(eElement.getElementsByTagName("Ccy").item(1).getTextContent()).orElse(null);
					currency.getNameEn();
					if (currency != null) {
						currencyRate.setName(currency);

						currencyRate
								.setRate(new BigDecimal(eElement.getElementsByTagName("Amt").item(1).getTextContent()));
						currencyRate.setDate(LocalDate
								.parse(eElement.getElementsByTagName("Dt").item(0).getTextContent(), formatter));
						currencyRateRepository.save(currencyRate);
					}

				}
			}

		}
	}
	public Document getXmlResponseDocument(String url) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document doc = factory.newDocumentBuilder().parse(new URL(url).openStream());
		return doc;
	}

	public void removeNameWithNoData() {
		List<CurrencyName> nameList = currencyNameRepository.findAll();
	
		for (CurrencyName name : nameList) {
			if (name.getRate().size() < 1) {
				currencyNameRepository.delete(name);

			}

		}
	}
	


}
