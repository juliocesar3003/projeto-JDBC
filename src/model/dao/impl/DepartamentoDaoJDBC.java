package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartamentoDao;
import model.entities.Departamento;

public class DepartamentoDaoJDBC implements DepartamentoDao {

	private Connection connect;
	
	public DepartamentoDaoJDBC(Connection connect) {
		this.connect = connect;
	}
	
	@Override
	public void insert(Departamento obj) {
		PreparedStatement st = null;
		try {
			st = connect.prepareStatement(
					"INSERT INTO seller "
					+"(Name) "
					+"VALUES "
					+"(?)",Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1,obj.getName());
			
		    
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
	public void update(Departamento obj) {
		PreparedStatement st = null;
		try {
			st = connect.prepareStatement(
					"UPDATE departament "
					+"SET Name = ? "
					+"WHERE Id = ?");
			
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
		    
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
	public void deleteById(Departamento obj) {
		PreparedStatement st = null;
		
		try {
			st = connect.prepareStatement("DELETE FROM departament WHERE Id = ?");
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
	public Departamento FindById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
	 try {
		 	st = connect.prepareStatement( "SELECT * FROM department WHERE Id = ?");
				
		 	st.setInt(1, id);
				rs = st.executeQuery();
	 
				// IF SENDO USADO PARA SABER SE RETORNOU ALGUM VENDEDOR
				// INSTACIANDO OS OBJETOS PARA QUAL VAMOS ATRIBUIR OS DADOS QUE FORAM PASSADOS DO MYSQL
				
				if(rs.next()) {
			
					Departamento obj = new Departamento();
					obj.setId(rs.getInt("Id"));
					obj.setName(rs.getString("Name"));
					return obj;
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
	public List<Departamento> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			
			st = connect.prepareStatement(
					
					"SELECT department.Name as DepName "
				    +"FROM department ");
					
					rs = st.executeQuery();
		 
					List<Departamento> lista = new ArrayList<Departamento>();
										
					while (rs.next()) {
				
						Departamento obj = new Departamento();
						obj.setId(rs.getInt("Id"));
						obj.setName(rs.getString("Name"));
						lista.add(obj);
				
				
				
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
	
	
 }
