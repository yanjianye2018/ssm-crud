package com.yzy.crud.test;

import com.github.pagehelper.PageInfo;
import com.yzy.crud.bean.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * @Description
 * @Author yzy
 * @Date 2018-10-15 14:36
 * @Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml",
		"file:src/main/webapp/WEB-INF/dispatcherServlet-servlet.xml"})
public class MvcTest {
	//传入springmvc的ioc
	@Autowired
	private WebApplicationContext context;
	//虚拟mvc请求,获取处理结果
	private MockMvc mockMvc;
	@Before
	public void initMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	@Test
	public void testPage() throws Exception {
		//模拟请求拿到返回值
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emps")
				.param("pn", "1")).andReturn();

		//请求成功以后,request域中会有pageInfo,取出pageInfo进行验证
		MockHttpServletRequest request = result.getRequest();
		PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
		System.out.println("当前页码" + pageInfo.getPageNum());
		System.out.println("总页码" + pageInfo.getPages());
		System.out.println("总记录数" + pageInfo.getTotal());
		int[] nums = pageInfo.getNavigatepageNums();
		System.out.println("连续显示的页码" + pageInfo.getTotal());
		for (int num : nums) {
			System.out.print(" " + num);
		}
		//获取员工数据
		List<Employee> list = pageInfo.getList();
		for(Employee employee : list){
			System.out.println("ID"+employee.getEmpId()+
					"->name"+employee.getEmpName());
		}


	}
}
