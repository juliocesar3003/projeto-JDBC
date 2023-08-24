package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		PreparedStatement st = null;
		try {
			st = connect.prepareStatement(
					"INSERT INTO seller "
					+"(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+"VALUES "
					+"(?, ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataNascimento().getTime()));
		    st.setDouble(4,obj.getSalario());
		    st.setInt(5,obj.getDepartamento().getId());
		    
		    int rowsAffected = st.executeUpdate();
		    
		    if (rowsAffected > 0) {
		    	ResultSet rs = st.getGeneratedKeys();
		    	
		    	if(rs.next()) {
		    		int id = rs.getInt(1);
		    		obj.setId(id);
		    	}
		    	DB.closeResultSet(rs);
		    }
		    else {
		    	throw new DbException("erro inesperado nenhuma linha foi afetada");
		    }
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
	

	@Override
	public void update(Vendedor obj) {
		PreparedStatement st = null;
		try {
			st = connect.prepareStatement(
					"UPDATE seller "
					+"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+"WHERE Id = ?");
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataNascimento().getTime()));
		    st.setDouble(4,obj.getSalario());
		    st.setInt(5,obj.getDepartamento().getId());
		    st.setInt(6, obj.getId());
		    
		    st.executeUpdate();
		   
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
		
	

	@Override
	public void deleteById(Vendedor obj) {
		PreparedStatement st = null;
		
		try {
			st = connect.prepareStatement("DELETE FROM seller WHERE Id = ?");
			st.setInt(1, obj.getId());
			
			st.executeUpdate();
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Vendedor FindById(Integer id) {
	
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			//COMANDO SQL PARA PUXAR OS DADOS QUE QUEREMOS 
			// USANDO O ID DO PARAMETRO PARA DIZER QUAL VENDEDOR QUEREMOS BUSCAR
			
			st = connect.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
					st.setInt(1, id);
					rs = st.executeQuery();
		 
					// IF SENDO USADO PARA SABER SE RETORNOU ALGUM VENDEDOR
					// INSTACIANDO OS OBJETOS PARA QUAL VAMOS ATRIBUIR OS DADOS QUE FORAM PASSADOS DO MYSQL
					
					if(rs.next()) {
				
				Departamento departamento = instaciarDepartamento(rs);
				
				Vendedor objV = instaciarVendedor(rs, departamento);
				
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

	//          AQUI É BASICAMENTE A MESMA APLICAÇÃO DO byID COM A DIFERENÇA  QUE NÃO TEM TEM O WHERE
	
	@Override
	public List<Vendedor> findAll() {
	
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			
			st = connect.prepareStatement(
					
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id "
					+"ORDER BY Name ");
			
					
					rs = st.executeQuery();
		 
					List<Vendedor> lista = new ArrayList<Vendedor>();
					Map <Integer, Departamento> map = new HashMap<>();
					
					while (rs.next()) {
				
						Departamento dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {
					dep = instaciarDepartamento(rs);
					map.put(rs.getInt("DepartamentId"),dep);
				}
				Vendedor objV = instaciarVendedor(rs, dep);
				lista.add(objV);
				
			}
			return lista;
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
	public List<Vendedor> findByDepartment(Departamento departamento) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			
			st = connect.prepareStatement(
					
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id "
					+"WHERE DepartmentId = ? "
					+" ORDER BY Name ");
			
					st.setInt(1, departamento.getId());
					rs = st.executeQuery();
		 
					List<Vendedor> lista = new ArrayList<Vendedor>();
					Map <Integer, Departamento> map = new HashMap<>();
					
					while (rs.next()) {
				
						Departamento dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {
					dep = instaciarDepartamento(rs);
					map.put(rs.getInt("DepartamentId"),dep);
				}
				Vendedor objV = instaciarVendedor(rs, dep);
				lista.add(objV);
				
			}
			return lista;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}
	private Vendedor instaciarVendedor(ResultSet rs, Departamento departamento) throws SQLException {
		Vendedor objV = new Vendedor();
		
		objV.setId(rs.getInt("Id"));
		objV.setNome(rs.getString("Name"));
		objV.setEmail(rs.getString("Email"));
		objV.setSalario(rs.getDouble("BaseSalary"));
		objV.setDataNascimento(rs.getDate("BirthDate"));
		objV.setDepartamento(departamento);
		
		return objV;
	}

	private Departamento instaciarDepartamento(ResultSet rs) throws SQLException {
		Departamento departamento = new Departamento();
		
		departamento.setId(rs.getInt("DepartmentId"));
		departamento.setName(rs.getString("DepName"));
		
		return departamento;
	}
	

   
}
