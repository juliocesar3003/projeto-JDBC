package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.VendedorDao;
import model.entities.Departamento;
import model.entities.Vendedor;

public class VendedorDaoJDBC implements VendedorDao {

	private Connection connect;
	
	public VendedorDaoJDBC(Connection connect) {
		this.connect = connect;
	}
	
	@Override
	public void insert(Vendedor obj) {
		
		
	}

	@Override
	public void update(Vendedor obj) {
	
		
	}

	@Override
	public void deleteById(Vendedor obj) {
		
		
	}

	@Override
	public Vendedor FindById(Integer id) {
	
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = connect.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
					st.setInt(1, id);
					rs = st.executeQuery();
					if(rs.next()) {
				
				Departamento departamento = new Departamento();
				departamento.setId(rs.getInt("DepartmentId"));
				departamento.setName(rs.getString("DepName"));
				
				Vendedor objV = new Vendedor();
				objV.setId(rs.getInt("Id"));
				objV.setNome(rs.getString("Name"));
				objV.setEmail(rs.getString("Email"));
				objV.setSalario(rs.getDouble("BaseSalary"));
				objV.setDataNascimento(rs.getDate("BirthDate"));
				objV.setDepartamento(departamento);
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public List<Vendedor> findAll() {
	
		return null;
	}
   
}
