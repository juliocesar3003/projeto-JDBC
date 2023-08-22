package model.dao;

import java.util.List;

import model.entities.Departamento;

public interface DepartamentoDao {
	
	void insert(Departamento obj);
	void update(Departamento obj);
	void deleteById(Departamento obj);
	Departamento FindById(Integer id);
	List<Departamento> findAll();
	}
