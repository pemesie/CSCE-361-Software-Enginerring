package rim;

public class GuiInteract {
	
	public static void main(String[] args)
	{
		LoginView.login();
		clearConsole();
		InventoryView.displayProducts(0);
		
	}

	
	public final static void clearConsole()
	{
	    try
	    {
	        final String os = System.getProperty("os.name");

	        if (os.contains("Windows"))
	        {
	            Runtime.getRuntime().exec("cls");
	        }
	        else
	        {
	            Runtime.getRuntime().exec("clear");
	        }
	    }
	    catch (final Exception e)
	    {
	        //  Handle any exceptions.
	    }
	}
	
	

}
