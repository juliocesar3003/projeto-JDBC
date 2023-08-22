package model.dao;

import java.util.List;

import model.entities.Vendedor;

public interface VendedorDao {

	void insert(Vendedor obj);
	void update(Vendedor obj);
	void deleteById(Vendedor obj);
	Vendedor FindById(Integer id);
	List<Vendedor> findAll();
}
