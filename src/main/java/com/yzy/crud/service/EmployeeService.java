package com.yzy.crud.service;

import com.yzy.crud.bean.Employee;
import com.yzy.crud.bean.EmployeeExample;
import com.yzy.crud.dao.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author yzy
 * @Date 2018-10-15 14:21
 * @Version 1.0
 */
@Service
public class EmployeeService {
	//注入mapper
	@Autowired
	private EmployeeMapper employeeMapper;
	public List<Employee> getAll() {
		/**
		 * @Author yzy
		 * @Description 调用mapper查询所有,没有id就是null
		 **/

		return employeeMapper.selectByExampleWithDept(null);
	}
	//员工保存
	public void saveEmp(Employee employee) {
		employeeMapper.insertSelective(employee);

	}
	//校验用户名是否可用
	//return ==0表示当前数据库没有记录,用户名合法
	public boolean checkUser(String empName) {
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		criteria.andEmpNameEqualTo(empName);
		long count = employeeMapper.countByExample(example);
		return count == 0;
	}

	public Employee getEmp(Integer id) {
		return employeeMapper.selectByPrimaryKey(id);
	}

	//员工更新
	public void updateEmp(Employee employee) {
		employeeMapper.updateByPrimaryKeySelective(employee);
	}
	//员工删除
	public void deleteEmp(Integer id) {
		employeeMapper.deleteByPrimaryKey(id);
	}
	//批量删除
	public void deleteBatch(List<Integer> ids) {
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		criteria.andEmpIdIn(ids);
		employeeMapper.deleteByExample(example);

	}
}
