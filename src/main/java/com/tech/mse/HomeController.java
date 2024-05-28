package com.tech.mse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import com.tech.mse.api.AccMainCode;
import com.tech.mse.api.AccSubCode;
import com.tech.mse.api.RegCode;
import com.tech.mse.api.TransCoord;
import com.tech.mse.api.TransCoordAjax;
import com.tech.mse.dao.CodeDao;
import com.tech.mse.dto.AccInfoDto;
import com.tech.mse.dto.CctvDto;
import com.tech.mse.dto.RedCodeNameDto;
import com.tech.mse.service.CctvService;


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
	@RequestMapping(value = "/cctv", method = RequestMethod.GET)
	public String cctv(Model model) {
		
		
		return "cctv";
	}
	
	@RequestMapping(value = "/accInfoAjax", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
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
	@RequestMapping(value = "/BoundsCctvDataAjax", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String BoundsCctvDataAjax(Model model,@RequestParam String minX, @RequestParam String minY,@RequestParam String maxX, @RequestParam String maxY) {
		System.out.println(minX+" : " + maxX);
		System.out.println(minY+" : " + maxY);
		model.addAttribute("minX", minX);
		model.addAttribute("maxX", maxX);
		model.addAttribute("minY", minY);
		model.addAttribute("maxY", maxY);
		model.addAttribute("sqlSession", sqlSession);
		
		CctvService cctvService = new CctvService();
		String jasonClosestCctv = cctvService.createCCtvObject2(model);
		
		return jasonClosestCctv;
	}
	@RequestMapping(value = "/cctvDataAjax", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String cctvDataAjax(Model model,@RequestParam String lat, @RequestParam String lng) {
		
		System.out.println(lat+" : " + lng);
		model.addAttribute("lat", lat);
		model.addAttribute("lng", lng);
		model.addAttribute("sqlSession", sqlSession);
		
		CctvService cctvService = new CctvService();
		String jasonClosestCctv = cctvService.createCCtvObject(model);
		
		return jasonClosestCctv;
	}
	@RequestMapping(value = "/cctvAllDataAjax", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String cctvAllDataAjax(Model model) {
		
		model.addAttribute("sqlSession", sqlSession);
		
		CctvService cctvService = new CctvService();
		String jasonClosestCctv = cctvService.createAllCCtvObject(model);
		
		return jasonClosestCctv;
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
		
		return "marker";
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
		
		return "marker";
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
		
		return "marker";
	}
	@RequestMapping(value = "cctvInsert", method = RequestMethod.GET)
	public String cctvInsert(Model model) {
		System.out.println("cctvinsert");
		
		List<CctvDto> mdtolist = new ArrayList<CctvDto>();
		
		File csv=new File("C:\\23setSpring\\springwork23\\MSE\\src\\main\\webapp\\resources\\csvCCTV\\OpenDataCCTV.csv");
		System.out.println(csv);
		BufferedReader br=null;
	      String line="";
	      int cnt=0;
	      try {
	         br=new BufferedReader(new FileReader(csv));
	         while ((line=br.readLine())!=null) {
	            System.out.println((++cnt)+line+"\n");
	            if(cnt==1) { continue; }
	            String[] str=line.split("\\|");
	            CctvDto mdto=new CctvDto(str[1], str[2], str[3],str[4],str[5]);
	            mdtolist.add(mdto);   
	         }

	      } catch (Exception e) {
	    	  System.out.println(cnt +"행 오류");
	    	  e.printStackTrace();
	      } finally {
	         try {
	            if (br!=null) {
	               br.close();
	            }
	         } catch (Exception e2) {
	        	 e2.printStackTrace();
	         }
	      }
		
		CodeDao dao=sqlSession.getMapper(CodeDao.class);
//		dao.insertmember(mdtolist);
		//500개씩 잘라서 넣기
		
		List<CctvDto> slist=new ArrayList<CctvDto>();
	      for (int i = 0; i < mdtolist.size(); i++) {
	         slist.add(mdtolist.get(i));   
	         System.out.println(i+" : "+(mdtolist.size()-1));
	         if(i%500==0 || i==mdtolist.size()-1) {
	            dao.insertCctv(slist);         
	            slist.clear();
	         }
	      }
		
		return "marker";
	}

	
}
