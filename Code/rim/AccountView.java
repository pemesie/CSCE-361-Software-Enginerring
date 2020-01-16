package rim;

import java.util.List;
import java.util.Scanner;

public class AccountView {

	public static void accountView(){
		if(!(Account.getCurrentUser().getManagerId() == null))
		{
			managementView();
		}
		else
		{
			employeeView();

		}
	}
	public static void employeeView(){

		System.out.print("******************************************************************************\n"
				+ "        Employee Account Overview\n"
				+ "        Currently logged in: " + Account.getCurrentUser().getFirstName() + " " + Account.getCurrentUser().getLastName() + "\n"
				+ "******************************************************************************\n");

		System.out.println("First Name: " + Account.getCurrentUser().getFirstName() + "   Last Name: " + Account.getCurrentUser().getLastName());

		System.out.println("Username: " + Account.getCurrentUser().getUserName() + "   Employee Id: " + Account.getCurrentUser().getId());



		while(true)
		{
			System.out.print("\nEnter a command: ");
			Scanner s = new Scanner(System.in);
			String command = s.nextLine();

			if(command.contains("password") && command.split(" ").length == 2)
			{
				String password = command.split(" ")[1];
				Account.changePassword(Account.getCurrentUser().getId(), password);
			}
			if(command.contains("username") && command.split(" ").length == 2)
			{
				String username = command.split(" ")[1];
				Account.changeUsername(Account.getCurrentUser().getId(), username);
			}
			else if(command.equals("inventory") || command.equals("back"))
			{
				InventoryView.displayProducts(0);
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
			else if(command.equals("refresh"))
			{
				refreshAccounts();
			}
			else
			{
				System.out.println("Invalid Command.");
			}
		}


	}

	public static void managementView(){
		List<Account> accounts = Account.getAccounts();
		int count = 1;

		System.out.print("******************************************************************************\n"
				+ "        Managment Account Overview\n"
				+ "        Currently logged in: " + Account.getCurrentUser().getFirstName() + " " + Account.getCurrentUser().getLastName() + "\n"
				+ "******************************************************************************\n");

		for(Account a : accounts)
		{
			System.out.println(count + ". " + a.getFirstName() + " " + a.getLastName());
			count++;
		}

		while(true)
		{
			System.out.print("\nEnter product # or command: ");
			Scanner s = new Scanner(System.in);
			String command = s.nextLine();

			if(command.matches("\\d+") && Integer.parseInt(command) > 0 && Integer.parseInt(command) <= accounts.size())
			{
				int accountNum = Integer.parseInt(command) -1;
				displayAccount(accounts.get(accountNum));
			}
			else if(command.contains("add") && command.split(" ").length >= 5)
			{
				if(Account.getCurrentUser().getManagerId() == null){
					System.out.println("You do not have permission to add an account. Only Managers can do so.");
					continue;
				}
				else{

					
					String firstName = command.split(" ")[1];
					String lastName = command.split(" ")[2];
					String username= command.split(" ")[3];
					String password = command.split(" ")[4];
					Object managerId = null;
					if(command.split(" ").length == 6)
					{
						managerId = Integer.parseInt(command.split(" ")[5]);
					}
					Account.addAccount(firstName, lastName, username, password, managerId);
				}
			}
			else if(command.equals("inventory") || command.equals("back"))
			{
				InventoryView.displayProducts(0);
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
			else if(command.equals("refresh"))
			{
				refreshAccounts();
			}
			else
			{
				System.out.println("Invalid Command.");
			}
		}
	}
	private static void displayAccount(Account a) {
		String role = "Employee";
		System.out.print("******************************************************************************\n"
				+ "        Account Details: " + a.getFirstName() +"\n"
				+ "******************************************************************************\n");
		System.out.println("First Name: " + a.getFirstName() + "   Last Name: " + a.getLastName());

		if(!(a.getManagerId() == null)){
			role = "Manager";
		}

		System.out.println("Username: " + a.getUserName() + "   Employee Id: " + a.getId());
		System.out.println("Role :" + role);

		while(true)
		{
			System.out.print("\nEnter product a command: ");
			Scanner s = new Scanner(System.in);
			String command = s.nextLine();

			if(command.contains("password") && command.split(" ").length == 2)
			{
				String password = command.split(" ")[1];
				Account.changePassword(a.getId(), password);
			}
			if(command.contains("username") && command.split(" ").length == 2)
			{
				String username = command.split(" ")[1];
				Account.changeUsername(a.getCurrentUser().getId(), username);
			}
			else if(command.equals("back") || command.equals("account"))
			{
				accountView();
			}
			else if(command.equals("inventory") || command.equals("back"))
			{
				InventoryView.displayProducts(0);
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
			else if(command.equals("refresh"))
			{
				refreshAccount(a);
			}
			else
			{
				System.out.println("Invalid Command.");
			}
		}

	}
	
	public static void refreshAccounts() {
		accountView();

	}

	public static void refreshAccount(Account a) {
		displayAccount(a);

	}
}
