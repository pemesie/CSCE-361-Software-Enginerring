package rim;

import java.util.Scanner;

public class CartView {
	public static void DisplayCart() {

		Scanner s = new Scanner(System.in);
		while (true) {
			System.out.print("******************************************************************************\n"
					+ "        Your Cart\n"
					+ "******************************************************************************\n");
			System.out.println(Cart.getCart().toString().replace("[", "").replace("]", "").replace(", ", ""));
			System.out.print("\nEnter a command: ");
			String command = s.nextLine();

			if (command.equals("back") || command.equals("inventory")) {
				InventoryView.displayProducts(0);
			} else if (command.equals("edit")) {
				System.out.print("Enter the item rank in your cart and the quantity you want change it to: ");
				String edit = s.nextLine();
				Cart.editItem(Integer.parseInt(edit.split(" ")[0]), Integer.parseInt(edit.split(" ")[1]));
			} else if(command.equals("clear")){
				Cart.clearCart();
			} else if (command.contains("delete") && command.split(" ")[1].matches("\\d+")) {
				Cart.deleteItem(Integer.parseInt(command.split(" ")[1]));
			} else if (command.equals("checkout")) {
				if(Cart.checkOut() == true){
					System.out.println("You have successfully checked out your items in cart\n");
				}
				else{
					System.out.println("check out fail\n");
				}
			}
			else if(command.equals("refresh"))
			{
				DisplayCart();
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
			else{
				System.out.println("Invalid command");
			}
		}
	}
}
