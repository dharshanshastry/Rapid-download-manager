package application.controller.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class DownloadCompleteController implements Initializable {


	private String filePath;

	@FXML
	private TextField filePathText;

	@FXML
	private Button openFile;

	@FXML
	private Button openFolder;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	public void openFile(MouseEvent actionEvent){
		File file = new File(getFilePath());

		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			// TODO: handle exception
		}
		hideWindow(actionEvent);
	}

	private void hideWindow(MouseEvent actionEvent) {
		((Node)actionEvent.getSource()).getScene().getWindow().hide();
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
