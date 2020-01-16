package rim;

import java.util.List;

public class check {
	static List<Product> products = Product.getProducts();
	static List<Employee> employees=Employee.getEmployees();
	
	public static boolean checkExistingProduct(String name){
		boolean existingProduct=false;
		for(Product p: products){
			if(name.equals(p.getName())){
				existingProduct=true;
				System.out.println("duplicated product name");
				break;
			}
		}
		return existingProduct;
	}
	public static boolean checkExistingUser(String userName){
		boolean existingUser=false;
		for(Employee e:employees){
			if(e.getUserName().equals(userName)){
				existingUser=true;
				System.out.println("User name has been taken, please use another one");
				break;
			}
		}
		return existingUser;
	}
}
