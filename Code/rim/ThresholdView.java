package rim;

import java.util.List;
import java.util.Scanner;

public class ThresholdView {

	public static void displayThresholdProduct(int sort) {
		List<Product> products = Product.getThresholdProducts();
		int count = 1;
		int sortType = sort;

		System.out.print("******************************************************************************\n"
				+ "        Threshold Information\n"
				+ "******************************************************************************\n");
		
		if(sortType == 1)
		{
			products = Product.sortName(products);
		}
		else if(sortType == 2)
		{
			products = Product.sortPrice(products);
		}
		else if(sortType == 3)
		{
			products = Product.sortQuantity(products);
		}
		else if(sortType == 4)
		{
			products = Product.sortType(products);
		}
		else if(sortType == 5)
		{
			products = Product.sortSupplier(products);
		}
		

		for(Product product : products)
		{
			System.out.println(count + ". " + product.getName());
			count++;
		}

		while(true)
		{
			System.out.print("\nEnter product # or command: ");
			Scanner s = new Scanner(System.in);
			String command = s.nextLine();
			//System.out.println(command.split(" ").length == 7);

			if(command.matches("\\d+") && Integer.parseInt(command) > 0 && Integer.parseInt(command) <= products.size())
			{
				int productNum = Integer.parseInt(command) -1;
				DetailsView.displayDetails(products.get(productNum));
			}
			else if(command.contains("add") && command.split(" ").length == 8 && command.split(" ")[4].matches("\\d+") && command.split(" ")[5].matches("\\d+") && command.split(" ")[6].matches("\\d+\\.\\d+"))
			{
				if(Account.getCurrentUser().getManagerId() == null){
					System.out.println("You do not have permission to add products. Only Managers can do so.");
					continue;
				}
				else{

					String name= command.split(" ")[1];
					String type= command.split(" ")[2];
					String description= command.split(" ")[3];
					int quantity= Integer.parseInt(command.split(" ")[4]);
					int threshold= Integer.parseInt(command.split(" ")[5]);
					double price= Double.parseDouble(command.split(" ")[6]);
					String supplier= command.split(" ")[7];
					Product.addProduct(name, type, description, quantity, threshold, price, supplier);
				}
			}
			else if(command.contains("remove") && command.split(" ").length == 2 && command.split(" ")[1].matches("\\d+")&& Integer.parseInt(command.split(" ")[1]) > 0 && Integer.parseInt(command.split(" ")[1]) <= products.size())
			{
				if(Account.getCurrentUser().getManagerId() == null){
					System.out.println("You do not have permission to remove products. Only Managers can do so.");
					continue;
				}
				else{
					System.out.print("\nYou are about to remove an item from the inventory. Are you sure?: ");
					String prompt = s.nextLine();
					if(prompt.equals("yes"))
					{

						String temp = command.split(" ")[1];
						int productNum = Integer.parseInt(temp) -1;
						Product.removeProduct(products.get(productNum));
						refreshThresholdProducts(sortType);
					}
				}
			}
			else if(command.contains("sort") && command.split(" ").length == 2)
			{
				String sortInput = command.split(" ")[1];
				if(sortInput.equals("name"))
				{
					sortType = 1;
					refreshThresholdProducts(sortType);
				}
				else if(sortInput.equals("price"))
				{
					sortType = 2;
					refreshThresholdProducts(sortType);}
				else if(sortInput.equals("quantity"))
				{
					sortType = 3;
					refreshThresholdProducts(sortType);}
				else if(sortInput.equals("type"))
				{
					sortType = 4;
					refreshThresholdProducts(sortType);}
				else if(sortInput.equals("supplier"))
				{
					sortType = 5;
					refreshThresholdProducts(sortType);}
				else
					System.out.println("Invalid Command.");

			}
			else if(command.equals("refresh"))
			{
				refreshThresholdProducts(sortType);
			}
			else if(command.equals("cart")){
				CartView.DisplayCart();
			}
			else if(command.equals("account"))
			{
				System.out.println(Account.getCurrentUser().getManagerId());
				AccountView.accountView();
			}
			else if(command.contentEquals("logout"))
			{
				System.out.println("Logging Out...\n");
				LoginView.login();
			}
			else if(command.contentEquals("quit"))
			{
				System.out.println("Quitting...\n");
				System.exit(1);;
			}
			else if(command.equals("back") || command.equals("inventory"))
			{
				InventoryView.displayProducts(0);
			}
			else
			{
				System.out.println("Invalid Command.");
			}
		}
	}

	public static void refreshThresholdProducts(int sort) {
		displayThresholdProduct(sort);

	}

}


