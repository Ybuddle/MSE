package com.tech.mse;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import com.tech.mse.dto.AccInfoDto;
import com.tech.mse.dto.RedCodeNameDto;

@Controller
public class HomeController {
	@Autowired
	private SqlSession sqlSession;


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {

		
		return "home";
	}
	@RequestMapping(value = "/marker", method = RequestMethod.GET)
	public String marker(Model model) {
		
		
		return "marker";
	}
	@RequestMapping(value = "/marker2", method = RequestMethod.GET)
	public String marker2(Model model) {
		
		StringBuilder urlbuilder = null;
		TransCoord tc = new TransCoord();
		try {
			urlbuilder = TransCoord.getURLAcc("1","100");
			System.out.println("URL : "+ urlbuilder.toString());
			List<AccInfoDto> accDtoList = tc.useOnlyDocSeoul(urlbuilder);
			model.addAttribute("accDtoList", accDtoList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "marker2";
	}
	@RequestMapping(value = "/marker3", method = RequestMethod.GET)
	public String marker3(Model model) {
		
		StringBuilder urlbuilder = null;
		TransCoord tc = new TransCoord();
		try {
			urlbuilder = TransCoord.getURLAcc("1","100");
			System.out.println("URL : "+ urlbuilder.toString());
			List<AccInfoDto> accDtoList = tc.useOnlyDocSeoul(urlbuilder);
			model.addAttribute("accDtoList", accDtoList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "marker3";
	}
	@RequestMapping(value = "/accInfoAjax", method = RequestMethod.GET)
	@ResponseBody
	public String accInfoAjax(Model model) {
		
		StringBuilder urlbuilder = null;
		String accDtoListJSON = null;
		TransCoord tc = new TransCoord();
		try {
			urlbuilder = TransCoord.getURLAcc("1","100");
			System.out.println("URL : "+ urlbuilder.toString());
			List<AccInfoDto> accDtoList = tc.useOnlyDocSeoul(urlbuilder);
			TransCoordAjax tca = new TransCoordAjax();
			accDtoListJSON = tca.accDTOListToJson(accDtoList);
			System.out.println("---------ajax 실행");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accDtoListJSON;
	}
	@RequestMapping(value = "/codeInsert", method = RequestMethod.GET)
	public String codeInsert(Model model) {
		
		StringBuilder urlbuilder = null;
		RegCode tc = new RegCode();
		try {
			urlbuilder = RegCode.getURLAcc("1","50");
			System.out.println("URL : "+ urlbuilder.toString());
			List<RedCodeNameDto> rDtolist = tc.useOnlyDocSeoul(urlbuilder);
			tc.insertDB(rDtolist,sqlSession);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "marker2";
	}
	@RequestMapping(value = "/codeInsertMain", method = RequestMethod.GET)
	public String codeInsertMain(Model model) {
		
		StringBuilder urlbuilder = null;
		AccMainCode tc = new AccMainCode();
		try {
			urlbuilder = AccMainCode.getURLAcc("1","50");
			System.out.println("URL : "+ urlbuilder.toString());
			List<Map<String, String>> accMainList = tc.useOnlyDocSeoul(urlbuilder);
			tc.insertDB(accMainList,sqlSession);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "marker2";
	}
	@RequestMapping(value = "codeInsertSub", method = RequestMethod.GET)
	public String codeInsertSub(Model model) {
		
		StringBuilder urlbuilder = null;
		AccSubCode tc = new AccSubCode();
		try {
			urlbuilder = AccSubCode.getURLAcc("1","50");
			System.out.println("URL : "+ urlbuilder.toString());
			List<Map<String, String>> accMainList = tc.useOnlyDocSeoul(urlbuilder);
			tc.insertDB(accMainList,sqlSession);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "marker2";
	}
	@RequestMapping(value = "/useboot", method = RequestMethod.GET)
	public String useboot(Model model) {
	
		return "useboot";
	}
	
}
