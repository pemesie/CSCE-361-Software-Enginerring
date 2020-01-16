package rim;

import java.util.Scanner;

public class DetailsView {

	public static void displayDetails(Product p) {

		System.out.print("******************************************************************************\n"
				+ "        Product Details: " + p.getName() +"\n"
				+ "******************************************************************************\n");
		System.out.println("Type: " + p.getType() + "   System Identifier: " + p.getId());
		System.out.println("Price: $" + p.getPrice() + "   Quantity: " + p.getQuantity() + "   Quantity Theshold: " + p.getThreshold());
		System.out.println("Description: " + p.getDescription());
		System.out.println("Supplier: " + p.getSupplier());

		while(true){
			System.out.print("\nEnter a command: ");
			Scanner s = new Scanner(System.in);
			String command = s.nextLine();

			if(command.equals("back") || command.equals("inventory"))
			{
				InventoryView.displayProducts(0);
			}
			else if(command.equals("account"))
			{
				AccountView.accountView();
			}
			else if(command.equals("cart")){
				CartView.DisplayCart();
			}
			else if(command.contains("buy") && command.split(" ")[1].matches("\\d+") && Integer.parseInt(command.split(" ")[1]) > 0)
			{
				int orderQuantity = Integer.parseInt(command.split(" ")[1]);
				if(orderQuantity > p.getQuantity())
				{
					System.out.println("Insufficient Quantity! No items have been added to the cart.");
					continue;
				}
				else
				{
					Cart.addToCart(p, orderQuantity);
				}
			}
			else if(command.contains("stock") && command.split(" ")[1].matches("\\d+") && Integer.parseInt(command.split(" ")[1]) > 0)
			{
				int stockQuantity = Integer.parseInt(command.split(" ")[1]);
				if(Account.getCurrentUser().getManagerId() == null){
					System.out.println("You do not have permission to stock products. Only Managers can do so.");
					continue;
				}
				else if(stockQuantity <= 0)
				{
					System.out.println("Invalid stock quantity! No items have been stocked in the inventory.");
					continue;
				}
				else
				{
					Product.updateQuantity(p.getId(), stockQuantity);
					System.out.printf("\"%s\" x%d Stocked in the inventory.\n", p.getName(), stockQuantity);
				}

			}
			else if(command.contains("price") && command.split(" ").length == 2 && command.split(" ")[1].matches("\\d+\\.\\d+") && Double.parseDouble(command.split(" ")[1]) >= 0)
			{
				if(Account.getCurrentUser().getManagerId() == null){
					System.out.println("You do not have permission to update product prices. Only Managers can do so.");
					continue;
				}
				else{

					double price= Double.parseDouble(command.split(" ")[1]);
					Product.updatePrice(p, price);
				}
			}
			if(command.contains("type") && command.split(" ").length == 2)
			{
				String type = command.split(" ")[1];
				Product.updateType(p, type);
			}
			else if(command.equals("remove"))
			{
				if(Account.getCurrentUser().getManagerId() == null){
					System.out.println("You do not have permission to add products. Only Managers can do so.");
					continue;
				}
				else{
					System.out.print("\nYou are about to remove an item from the inventory. Are you sure?: ");
					String prompt = s.nextLine();
					if(prompt.equals("yes"))
					{
						Product.removeProduct(p);
						InventoryView.refreshDisplayProducts(0);
					}
				}
			}
			else if(command.equals("refresh"))
			{
				refreshDisplayDetails(p);
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
			else if (command.contains("stats")) 
			{
				String [] temp = command.split(" ");
				if(temp.length == 1) {
					ItemStatistics itst = new ItemStatistics(p);
					itst.printStatistics();
				} else {
					ItemStatistics itst = new ItemStatistics(p, temp[1]);
					itst.printStatistics();
				}
				System.out.println();
			}
			else
			{
				System.out.println("Invalid Command.");
			}
		}
	}

	private static void refreshDisplayDetails(Product p) {
		displayDetails(p);
		
	}
}
