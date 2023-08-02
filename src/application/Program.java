package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
			
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== TEST 1: seller findById ====");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("\n=== TEST 2: seller findByDepartment ====");
		Department department = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(department);
		for (Seller obj : list) {
			System.out.println(obj);
		
		}
		
		System.out.println("\n=== TEST 3: seller findAll====");
       list = sellerDao.findAll();
		for (Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("\n=== TEST 4: seller insert====");
	      //Para testar a inserção de um novo vendedor criamos um novo objeto como feito também nos procedimentos anteriores
			Seller newSeller = new Seller(null,"Greg", "greg@gmail.com", new Date(), 4000.0, department);
			//nulo pois é um seller novo sem id ainda e usamos o department usado anteriormente
			//Agora inserimos no banco de dados
			sellerDao.insert(newSeller);
			//Print para ver se o programa deu certo e retorna o id de volta
			System.out.println("Inserted! New id = " + newSeller.getId());
					
	
	
	
		
	}

}
