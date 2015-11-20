package com.eliteams.quick4j.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eliteams.quick4j.web.model.AreaModel;
import com.eliteams.quick4j.web.model.BasicInfoModel;
import com.eliteams.quick4j.web.model.CaseDataModel;
import com.eliteams.quick4j.web.model.EmissionModel;
import com.eliteams.quick4j.web.model.ParamListForm;
import com.eliteams.quick4j.web.model.ParamModel;
import com.eliteams.quick4j.web.model.SceneModel;
import com.eliteams.quick4j.web.model.TestModel;
import com.eliteams.quick4j.web.model.User;
import com.eliteams.quick4j.web.service.CaseDataService;


@Controller
public class CaseController {
	
	@Resource
	private CaseDataService caseService;
	
	// step 1 
    @RequestMapping(value="index",method=RequestMethod.GET)
    public String  index(Model model,HttpSession session){
    	BasicInfoModel m = (BasicInfoModel) session.getAttribute("basic");
		User u = (User) session.getAttribute("userInfo");
		if(u!=null){
	    	model.addAttribute("username", u.getUsername());
			System.out.println(u.toString());
		}else{
			System.out.println("session lose........");
		}
		String task_status = (String) session.getAttribute("task_status");
//		if(task_status!=null && !task_status.equals("finish")){
//			model.addAttribute("task_status", task_status);
//			return "showlog";
//		}
		System.out.println("index page");
    	model.addAttribute("m", m);
    	model.addAttribute("flag", m==null?0:1);
    	return "index";
    }

    
	@RequestMapping(value="index",method=RequestMethod.POST )
	public String go_area(BasicInfoModel m,HttpSession session){
		session.setAttribute("basic", m);
		return "admin-area";
	}
	
	//step 2
	@RequestMapping(value="admin-area" )
	public String area(Model model,HttpSession session){
    	
		AreaModel m =(AreaModel) session.getAttribute("area");
        model.addAttribute("m",m); 
        model.addAttribute("flag", m==null?0:1);
		return "admin-area";
	}
	
	@RequestMapping(value="admin-area",method=RequestMethod.POST )
	public String go_in(AreaModel m,HttpSession session){
		System.out.println(m.toString());
		//模拟区域对象的数据
		List<AreaModel> list = new ArrayList<AreaModel>();
		list.add(new AreaModel("hello","1"));
		list.add(new AreaModel("wold","2"));
		list.add(new AreaModel("java","3"));
		
		session.setAttribute("arealist", list);
		return "admin-in";
	}
	
	//step 3
	
	@RequestMapping(value="admin-in" )
	public String in(Model model,HttpSession session){
		EmissionModel m =(EmissionModel) session.getAttribute("emission");
//		System.out.println(m.toString());
		model.addAttribute("flag", m==null?0:1);
        model.addAttribute("m",m); 
        
		return "admin-in";
	}
	
	@RequestMapping(value="admin-in" ,method=RequestMethod.POST)
	public String go_case(EmissionModel m,HttpSession session){
		System.out.println(m.toString());
		session.setAttribute("emission", m);
		return "admin-case";
	}
	
	//step 4
	
	
	@RequestMapping(value="admin-case" )
	public String showcase(Model model,HttpSession session){
		SceneModel m =(SceneModel) session.getAttribute("scene");
        model.addAttribute("m",m);
        model.addAttribute("flag", m==null?0:1);
		return "admin-case";
	}
	
	@RequestMapping(value="admin-case" ,method=RequestMethod.POST)
	public String go_para(SceneModel m,HttpSession session,Model model){
		System.out.println(m.toString());
		session.setAttribute("scene", m);

		return "admin-para";
	}
	
	//step 5 
	
	@RequestMapping(value="admin-para" )
	public String param(Model model,HttpSession session){
		ParamModel m =(ParamModel) session.getAttribute("para");
        model.addAttribute("m",m);
        model.addAttribute("flag", m==null?0:1);
		return "admin-para";
	}
	
	
	
	@RequestMapping(value="admin-para" ,method=RequestMethod.POST)
	public String go_test(ParamListForm listparaform,HttpSession session,Model model,HttpServletRequest request){
		String  data =request.getParameter("listpara");
		String projects = request.getParameter("projects");
		if(data!=null && projects!=null){
			//service doing 
			List<ParamModel> params = caseService.initParamModel(data, projects);
			session.setAttribute("paramlist", params);
		}
		System.out.println("准备跳转到test页面");
		return "admin-test";
	}
	
	//step 6
	@RequestMapping(value="admin-test" )
	public String test(Model model,HttpSession session){
		
		StringBuffer sb = new StringBuffer();
		BasicInfoModel b = (BasicInfoModel) session.getAttribute("basic");
		if(b!=null){
			sb.append(b.toString()).append("<br>");
		}
		
		SceneModel sc = (SceneModel) session.getAttribute("scene");
		if(sc!=null){
			sb.append(sc.toString()).append("<br>");
		}
		
		
		TestModel m =(TestModel) session.getAttribute("test");
        model.addAttribute("m",m);
        model.addAttribute("flag", m==null?0:1);
        
        model.addAttribute("allpara", sb==null?"no param":sb.toString());
        
		return "admin-test";
	}
	
	@RequestMapping(value="admin-test" ,method=RequestMethod.POST)
	public String go_done(TestModel m,HttpSession session){
//		System.out.println(m.toString());
		session.setAttribute("test", m);
		return "admin-done";
	}
	
	
	//step 6
	@RequestMapping(value="admin-done" )
	public String done(Model model,HttpSession session){
		System.out.println("完成页面");
		return "admin-done";
	}
	
	@RequestMapping(value="saveResult")
	@ResponseBody
	public Map<String,Object> saveResult(HttpSession session){
		Map<String,Object> msg = new HashMap<String, Object>();
		
		BasicInfoModel basic = (BasicInfoModel) session.getAttribute("basic");
//		ParamModel param = (ParamModel) session.getAttribute("paramlist");
		boolean status = caseService.saveCaseData();
		CaseDataModel m = new CaseDataModel();
		m.setId("4");
		m.setCase_name("case name");
		m.setArinv_inventory("dddd");
//		caseService.insert(m);
		List<CaseDataModel> list= caseService.selectList();
		if(list!=null && list.size()>0){
			for(CaseDataModel mm : list){
				System.out.println(mm.getId()+"\t"+mm.getCase_name()+mm.toString());
			}
		}
		if(status ==true){
			msg.put("msg", "恭喜，数据保存成功");
		}else{
			msg.put("msg", "ooh,数据保存遇到问题，联系管理员");
		}
		return msg;
	}
	
}
