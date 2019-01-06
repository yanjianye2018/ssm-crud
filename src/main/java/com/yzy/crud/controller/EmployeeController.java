package com.yzy.crud.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yzy.crud.bean.Employee;
import com.yzy.crud.bean.Msg;
import com.yzy.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 处理员工crud
 * @Author yzy
 * @Date 2018-10-15 14:15
 * @Version 1.0
 */
@Controller
public class EmployeeController {
	@Autowired
	//注入service层
	private EmployeeService employeeService;

	//根据id删除员工
	@ResponseBody
	@RequestMapping(value="/emp/{ids}",method = RequestMethod.DELETE)
	public Msg deleteEmpById(@PathVariable("ids") String ids){
		if(ids.contains("-")){
			List<Integer> list =new ArrayList<>();
			String[] str_ids = ids.split("-");
			for(String string:str_ids){
				list.add(Integer.parseInt(string));
			}
			employeeService.deleteBatch(list);
		}else {
			Integer id = Integer.parseInt(ids);
			employeeService.deleteEmp(id);
		}
		return Msg.success();
	}
	//根据id查询员工
	@ResponseBody
	@RequestMapping(value="/emp/{empId}",method = RequestMethod.PUT)
	public Msg saveEmp(Employee employee){
		System.out.println(employee);
		employeeService.updateEmp(employee);
		return Msg.success();
	}

	@RequestMapping(value="/emp/{id}",method = RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id") Integer id) {
		Employee employee = employeeService.getEmp(id);
		return Msg.success().add("emp", employee);
	}

	@ResponseBody
	@RequestMapping("/checkuser")
	//校验用户名是否可用,
	public Msg checkuser(@RequestParam("empName") String empName) {
		//先判断用户名是否是合法的表达式
		String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
		if (empName.matches(regx)) {
			return Msg.fail().add("va_msg", "用户名必须是6-16为数字和字母的组合");
		}
		//数据库用户名重复校验
		boolean b = employeeService.checkUser(empName);
		if (b) {
			return Msg.success();
		} else {
			return Msg.fail().add("va_msg", "用户名不可用");
		}
	}

	@RequestMapping(value = "/emp", method = RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee, BindingResult result) {
		if (result.hasErrors()) {
			//校验失败,返回失败,
			Map<String, String> map = new HashMap<>();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				System.out.println("错误的字段名" + fieldError.getField());
				System.out.println("错误信息" + fieldError.getDefaultMessage());
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields", map);
		} else {
			employeeService.saveEmp(employee);
			return Msg.success();
		}
	}

	@ResponseBody
	@RequestMapping("/emps")
	public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1")
									   Integer pn) {
		/**
		 * @Author yzy
		 * @Description 需要引入jackson依赖
		 **/

		//传入页码以及每页显示数
		PageHelper.startPage(pn, 5);
		//调用service查询
		List<Employee> emps = employeeService.getAll();
		//封装查询数据,传入导航页数
		PageInfo pageInfo = new PageInfo(emps, 5);
		return Msg.success().add("pageInfo", pageInfo);
	}

	//@RequestMapping("/emps")
	//默认起始页为第一页
	public String getEmps(@RequestParam(value = "pn", defaultValue = "1")
								  Integer pn, Model model) {
		/**
		 * @Author yzy
		 * @Description 查询员工所有数据, 分页查询需要引入分页插件
		 **/
		//传入页码,以及每页显示数
		PageHelper.startPage(pn, 5);

		//调用service查询
		List<Employee> emps = employeeService.getAll();
		// 使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了。
		// 封装了详细的分页信息,包括有我们查询出来的数据，5表示传入连续显示的页数
		PageInfo page = new PageInfo(emps, 5);
		//将查询出来的结果放到request域中
		model.addAttribute("pageInfo", page);
		//转发到list.jsp
		return "list";
	}
}
