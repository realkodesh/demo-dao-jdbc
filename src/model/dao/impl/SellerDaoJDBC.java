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
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {

		this.conn = conn;

	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		try {
			// Montando statemente
			st = conn.prepareStatement(

					"INSERT INTO seller " + "(Name, Email, BirthDate, BaseSalary, DepartmentId) " + "VALUES "
							+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			// Configurar os ? de acordo com seu tipo a cima.

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());

		
			
		int rowsAffected = st.executeUpdate(); //Verificar linhas afetadas

			if (rowsAffected > 0) {

				ResultSet rs = st.getGeneratedKeys();
				// Se existir...
				if (rs.next()) {

					int id = rs.getInt(1);

					// atribuir id gerado no objeto obj

					obj.setId(id);

				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");

			}
		}
		// Criando excessão (Personalizada)
		catch (SQLException e) {

			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			// Montando statemente
			st = conn.prepareStatement(
					
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
				
			// Configurar os ? de acordo com seu tipo a cima.

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());

			 st.executeUpdate();

		
		}
		// Criando excessão (Personalizada)
		catch (SQLException e) {

			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;
		try {
			// Montando statemente
			st = conn.prepareStatement(
					
					"DELETE FROM seller WHERE Id = ?");
					
					
			// Configurar os ? de acordo com seu tipo a cima.

	
			st.setInt(1, id);
 //Teste para ver se o id existe ou não colocario o rows comm o if e a excessão 
			
			 st.executeUpdate();
    
		
		}
		// Criando excessão (Personalizada)
		catch (SQLException e) {

			throw new DbException(e.getMessage());
		} finally {
			
			//fechar o st
			DB.closeStatement(st);
		}
		

	}

	@Override
	public Seller findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {

			st = conn.prepareStatement(

					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				dep.setId(rs.getInt("DepartmentId"));
				dep.setName(rs.getString("DepName"));
				Seller obj = instantiateSeller(rs, dep);

				return obj;

			}
			return null;

		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());

		}

		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);

		}

	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;

	}

	@Override
	public List<Seller> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {

			st = conn.prepareStatement(

					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "ORDER BY Name");

			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentId"));

				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);

				}
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);

			}
			return list;

		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());

		}

		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);

		}
	}

	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {

			st = conn.prepareStatement(

					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");

			st.setInt(1, department.getId());

			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentId"));

				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);

				}
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);

			}
			return list;

		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());

		}

		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);

		}
	} // fechar os st e rs

}
