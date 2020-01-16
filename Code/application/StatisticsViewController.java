package application;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rim.InventoryStatistics;
import rim.ItemStatistics;
import rim.Product;
import rim.Sale;

public class StatisticsViewController implements Initializable {
	@FXML private TableView inventoryTable;
	@FXML private TableColumn nameColumn;
	@FXML private TableColumn priceColumn;
	@FXML private TableColumn stockColumn;
	@FXML private ComboBox comboBox;
	@FXML private CheckBox itemCheck;
	@FXML private CheckBox monthCheck;
	@FXML private TableView<Sale> tableView;
	@FXML private TableColumn<Sale, String> dateColumn;
	@FXML private TableColumn<Sale, String> quantityColumn;
	@FXML private TableColumn<Sale, String> saleColumn;
	@FXML private TableColumn<Sale, String> idColumn;
	@FXML private Text totalSaleLabel;
	@FXML private Text quantitySoldLabel;
	@FXML private TextField searchField;
	
	private static Product product;
	private static String month;
	
	ObservableList os = FXCollections.observableArrayList();
	FilteredList filter = new FilteredList(os, e->true);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.setInventoryTable();
		inventoryTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		stockColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		// for combo box
		this.initializeMonths();
	}
	
	private void setInventoryTable() {
		List<Product> products = Product.getProducts();
		for(Product p : products) {
			inventoryTable.getItems().add(p);
			os.add((Product) p);
		}
	}
	
	public void inventoryMouseClicked() {
		this.itemCheck.setSelected(false);
	}
	
	public void monthClicked() {
		this.monthCheck.setSelected(false);
	}
	
	public void getDataPressed() {
		// get product based on name in list
		String pName = ((Product) inventoryTable.getSelectionModel().getSelectedItem()).getName();
		ItemStatistics is = null;
		InventoryStatistics inv = null;
		if (pName != null) {
			product = Product.findProduct(pName);
		}
		
		
		// all items summary
		if( itemCheck.isSelected()) {
			// all months summary
			if(monthCheck.isSelected()) {
				comboBox.getSelectionModel().clearSelection();
				inv = new InventoryStatistics(Product.getProducts());
				totalSaleLabel.setText(String.format("Total Sale: $%.2f", inv.calculateTotalSales()));
				quantitySoldLabel.setText("Quantity Sold: " + inv.calculateQuantitySold());
			// particular month
			} else {
				month = (String) comboBox.getSelectionModel().getSelectedItem();
				inv = new InventoryStatistics(Product.getProducts(), month);
				totalSaleLabel.setText(String.format("Total Sale: $%.2f", inv.calculateTotalSales(month)));
				quantitySoldLabel.setText("Quantity Sold: " + inv.calculateQuantitySold(month));
			}

			return;
			
		}
		
		if (monthCheck.isSelected()) {
			comboBox.getSelectionModel().clearSelection();
			is = new ItemStatistics(product);
		} else {
			month = (String) comboBox.getSelectionModel().getSelectedItem();
			is = new ItemStatistics(product, month);
		}
		
		// fill table with data
		// PropertyValueFactory string corresponds to the members in the Sale class
		dateColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("date"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("quantity"));
		saleColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("totalSale"));
		idColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("saleId"));
		ObservableList os = getSales(is);
		tableView.setItems(os);
		
		
	}
	
	
	/**
	 * stores table data in ObservableList
	 * @return
	 */
	private ObservableList<Sale> getSales(ItemStatistics is) {
		List<Sale> sales = is.getSales();
		
		ObservableList<Sale> os = FXCollections.observableArrayList();
		for (Sale s : sales) {
			os.add(new Sale(s.getSaleId(), s.getDate(), s.getProductId(), s.getProductName(),
					s.getTotalSale(), s.getSalesTax(), s.getQuantity()));
		}
		
		// set total sale and quantity sold
	    totalSaleLabel.setText(String.format("Total Sale: $%.2f", is.calculateTotalSale()));
	    quantitySoldLabel.setText("Quantity Sold: " + is.calculateTotalQuantity());
		return os;
	}
	
	
	public void returnToInventory(ActionEvent event) throws IOException {
		Parent inventoryViewParent = FXMLLoader.load(getClass().getResource("InventoryView.fxml"));
		Scene inventoryViewScene = new Scene(inventoryViewParent, 1000, 600);
		
		// get the stage information
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(inventoryViewScene);
		window.show();
	}
	
	/**
	 * return to login page
	 */
	public void returnToLogin(ActionEvent event) throws IOException{
		Parent loginViewParent = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
		Scene loginViewScene = new Scene(loginViewParent, 480, 320);
		
		// get the stage information
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(loginViewScene);
		window.show();
		
	}
	
	private void initializeMonths() {
		comboBox.getItems().add("January");
		comboBox.getItems().add("February");
		comboBox.getItems().add("March");
		comboBox.getItems().add("April");
		comboBox.getItems().add("May");
		comboBox.getItems().add("June");
		comboBox.getItems().add("July");
		comboBox.getItems().add("August");
		comboBox.getItems().add("September");
		comboBox.getItems().add("October");
		comboBox.getItems().add("November");
		comboBox.getItems().add("December");
	}
	
	public void search(KeyEvent event) {
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filter.setPredicate((Predicate<Product>) (Product product)->{
			
			if (newValue.isEmpty() || newValue == null) {
				return true;
			} else if (product.getName().toUpperCase().contains(newValue.toUpperCase())) {
				return true;
			}
			return false;
			
			});
		});
		
		SortedList sort = new SortedList(filter);
		sort.comparatorProperty().bind(inventoryTable.comparatorProperty());
		inventoryTable.setItems(sort);
	}

}
