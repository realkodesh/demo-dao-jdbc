package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	void insert(Department obj);
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id); //Consulta no bd o obj com esse id

	//FindAll para retornar todos os departamentos
	List<Department> findAll();

}
