package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

public class TestController implements Initializable{

	@FXML private ListView listView;
	@FXML private TextArea ta;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listView.getItems().addAll("one","two","three");
		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	public void buttonPressed() {
		String str = "";
		str += (String) listView.getSelectionModel().getSelectedItem();
//		ObservableList listOfItems = listView.getSelectionModel().getSelectedItems();
//		for(Object item : listOfItems) {
//			str += String.format("%s%n", (String) item);
//		}
		this.ta.setText(str);
	}

}
