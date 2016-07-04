package de.daniel.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import de.daniel.GUI.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringJoiner;

import javax.imageio.ImageIO;

import de.daniel.Field.*;

/**
 * Main class and application start point
 */

public class Sudoku extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		
		//load the fxml layout from file
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI.fxml"));
		Parent root = loader.load();
		
		//get the controller of the application
		WindowController controller = loader.getController();
		
		//set the stage for further use in the controller
		controller.setStage(stage);

		stage.setTitle("Sudoku");
		stage.setScene(new Scene(root, 900, 800));
		
		//show the main window
		stage.show();

	}

	
	public static void main(String[] args) {
		
		//launch the application
		Application.launch(Sudoku.class, args);
	}
}