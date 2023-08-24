package teste;

import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartamentoDao;
import model.entities.Departamento;

public class TesteDeImplementacaoDepartamento {

	public static void main(String[] args) {
		 
		Scanner scan = new Scanner(System.in);
		
		DepartamentoDao departamentoDao = DaoFactory.createDepartamentoDao();
		
		

	}

}
