package com.yzy.crud.test;

import com.yzy.crud.bean.Department;
import com.yzy.crud.bean.Employee;
import com.yzy.crud.dao.DepartmentMapper;
import com.yzy.crud.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * @Description 测试dao层, 使用spring单元测试
 * //@ContextConfiguration指定spring配置文件的位置
 * @Author yzy
 * @Date 2018-10-15 12:50
 * @Version 1.0
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class MapperTest {
	@Autowired
	private DepartmentMapper departmentMapper;
	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private SqlSession sqlSession;

	@Test
	public void fun1() {
		//System.out.println(departmentMapper); 测试部门插入
		departmentMapper.insertSelective(new Department(null, "华山派"));
		departmentMapper.insertSelective(new Department(null, "衡山派"));
		departmentMapper.insertSelective(new Department(null, "嵩山派"));
		departmentMapper.insertSelective(new Department(null, "恒山派"));
		departmentMapper.insertSelective(new Department(null, "泰山派"));
		//测试员工插入

	}

	@Test
	public void fun2() {
		//测试员工插入
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
		for (int i = 0; i < 1000; i++) {
			String uid = UUID.randomUUID().toString().substring(0, 5) + i;
			mapper.insertSelective(new Employee(null, uid, "M", uid + "@yzy.com", 1));
		}
		System.out.println("over");
	}
}
