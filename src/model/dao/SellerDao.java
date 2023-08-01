package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {
	
		void insert(Seller obj);
		void update(Seller obj);
		void deleteById(Integer id);
		Seller findById(Integer id); //Consulta no bd o obj com esse id

		//FindAll para retornar todos os departamentos
		List<Seller> findAll();

	}

//CRTL + F FAZ O REPLACE DAS PALAVRAAAAS
