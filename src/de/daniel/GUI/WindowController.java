package de.daniel.GUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import javax.imageio.ImageIO;

import de.daniel.Field.Field;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * This is the controller class of the FXML file and handles all the events of
 * the GUI elements
 */
public class WindowController implements Initializable {

	// Stage object from the start method
	private Stage myStage;

	@FXML
	private GridPane sudokuGrid;

	private Field playField;
	private TextField[][] sudokuFields;

	/**
	 * This method generates a new play field.
	 * 
	 * @param event
	 */
	@FXML
	private void newGame(ActionEvent event) {

		playField = new Field();
		playField.init();

		setField(playField.getField());
		lockField();

	}

	/**
	 * This method set the play field back to init state.
	 * 
	 * @param event
	 */
	@FXML
	private void restart(ActionEvent event) {

		// set the play field with the initial play field
		playField.setField(playField.getInitField());

		setField(playField.getField());
		lockField();

	}

	/**
	 * This method solve the current Sudoku
	 * 
	 * @param event
	 */
	@FXML
	private void solve(ActionEvent event) {
		// solve the Sudoku
		playField.solve();
		setField(playField.getField());
	}

	/**
	 * This method load a Sudoku from a CSV file
	 * 
	 * @param event
	 */
	@FXML
	private void load(ActionEvent event) {

		// generate a new file chooser object
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Sudoku CSV");

		// set the extension filter to CSV files
		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("CSV files", "*.csv"));

		try {

			// get file to open from file chooser
			File file = fileChooser.showOpenDialog(myStage);

			// if fileChooser was aborted return
			if (file == null)
				return;

			// create an new field array
			int[][] loadField = new int[9][9];

			// read the file with a BufferedReader
			BufferedReader filereader = new BufferedReader(new FileReader(file));

			for (int i = 0; i < 9; i++) {

				// split a line into single fields
				String[] line = filereader.readLine().split(";");

				// if size of columns is wrong throw a exception
				if (line.length != 9) {
					filereader.close();
					throw new IOException("Could not load CSV file in right format");
				}
				// copy the values of this line into the field array
				for (int j = 0; j < 9; j++) {
					loadField[i][j] = Integer.parseInt(line[j]);
				}
			}

			filereader.close();

			// set the field
			playField.setField(loadField);
			playField.setInitField(playField.getField());
			setField(playField.getField());
			lockField();

		} catch (Exception e) {

			// show an error alert
			Alert alert = new Alert(AlertType.ERROR,
					"CSV Datei konnte nicht geladen werden \noder enthält nicht das gewünschte Format!", ButtonType.OK);

			alert.setHeaderText(null);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
		}
	}

	/**
	 * This method saves the play field in a CSV file.
	 * 
	 * @param event
	 */
	@FXML
	private void saveCSV(ActionEvent event) {

		// generate a new file chooser object
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Sudoku");

		// set the extension filter for CSV files
		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("CSV files", "*.csv"));

		try {

			// get the location in which you want to save the Sudoku
			File file = fileChooser.showSaveDialog(myStage);

			// if the dialog was aborted return
			if (file == null)
				return;

			int[][] field = playField.getField();

			// create the string to save in the CSV file
			StringBuilder saveString = new StringBuilder();
			for (int i = 0; i < 9; i++) {
				StringJoiner joiner = new StringJoiner(";");
				for (int j = 0; j < 9; j++) {
					joiner.add(String.valueOf(field[i][j]));
				}
				saveString.append(joiner.toString() + "\n");
			}

			// write Sudoku into file
			PrintWriter filewriter = new PrintWriter(new BufferedWriter((new FileWriter(file))));
			filewriter.write(saveString.toString());
			filewriter.flush();
			filewriter.close();

		} catch (Exception e) {
			// show an error alert
			Alert alert = new Alert(AlertType.ERROR, "Das Sudoku konnte nicht abgespeichert werden!", ButtonType.OK);

			alert.setHeaderText(null);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
		}
	}

	/**
	 * This method saves the Sudoku as a JPG.
	 * 
	 * @param event
	 */
	@FXML
	private void savePNG(ActionEvent event) {

		// generate a new file chooser object
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Sudoku");

		// set the extension filter for CSV files
		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("PNG files", "*.png"));

		try {
			File file = fileChooser.showSaveDialog(myStage);

			// if the dialog was aborted return
			if (file == null)
				return;

			// get the bounds of the field in the window
			Bounds bounds = sudokuGrid.getBoundsInLocal();
			Bounds screenBounds = sudokuGrid.localToScene(bounds);

			// make a screenshot of the window
			WritableImage image = sudokuGrid.getScene().snapshot(null);

			// cut the Sudoku out of the whole screenshot and save it to file
			ImageIO.write(SwingFXUtils.fromFXImage(image, null).getSubimage((int) screenBounds.getMinX(),
					(int) screenBounds.getMinY(), (int) screenBounds.getWidth(), (int) screenBounds.getHeight()), "png",
					file);

		} catch (Exception e) {
			// show an error alert
			Alert alert = new Alert(AlertType.ERROR, "Das Sudoku konnte nicht abgespeichert werden!", ButtonType.OK);

			alert.setHeaderText(null);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
		}
	}

	/**
	 * Exit the main program.
	 * 
	 * @param event
	 */
	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
	}

	/**
	 * This methode get a field array as parameter and write it to the GUI text
	 * fields.
	 * 
	 * @param field
	 *            field array which is written to the GUI text field grid.
	 */
	private void setField(int[][] field) {
		// for every row
		for (int i = 0; i < 9; i++) {
			// for every column
			for (int j = 0; j < 9; j++) {
				int value = field[i][j];

				// if value between 1 and 9 set the value, else set empty string
				if (value >= 1 && value <= 9) {
					sudokuFields[i][j].setText("" + value);
				} else {
					sudokuFields[i][j].setText("");
				}
			}
		}
	}

	/**
	 * This methode get the values of the GUI text fields and write it in the
	 * play field array.
	 * 
	 */
	private void setPlayField() {

		// init a new field array
		int[][] newField = new int[9][9];

		// for every row
		for (int i = 0; i < 9; i++) {
			// for every column
			for (int j = 0; j < 9; j++) {
				String text = sudokuFields[i][j].getText();

				try {
					
					if (text.equals("")) {
						newField[i][j] = 0;
					} else {
						newField[i][j] = Integer.parseInt(text);
					}
				} catch (Exception e) {
					newField[i][j] = 0;
				}
			}
		}
		
		//write back the field to GUI to avoid incoherence
		if (!playField.setField(newField)) {
			setField(playField.getField());
		}
	}

	/**
	 * This method locks the init field values so that their text fields could not be changed.
	 */
	private void lockField() {
		
		//for every text field
		for (TextField[] fields : sudokuFields) {
			for (TextField f : fields) {
				if (f.getText().equals("")) {
					f.setEditable(true);
					f.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

				} else {
					//if text of this field is a correct number than lock it and change background
					f.setEditable(false);
					f.setBackground(
							new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

				}
			}
		}
	}

	/**
	 * get the stage from start method because it is needed for the file chooser
	 * 
	 * @param stage
	 */
	public void setStage(Stage stage) {
		myStage = stage;
	}

	
	/**
	 * This method is executed after loading FXML file.
	 * It initializes the Sudoku grid.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//init the text field array
		sudokuFields = new TextField[9][9];
		
		//for every row
		for (int i = 0; i < 9; i++) {
			//for every column
			for (int j = 0; j < 9; j++) {
				
				//create new text field
				TextField field = new TextField("");
				
				//set the design
				field.setAlignment(Pos.CENTER);
				field.setFont(Font.font(30));
				field.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				field.setBackground(null);
				field.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
				
				//set a listener to update the play field every time a text field is changed
				field.textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {

						setPlayField();
					}
				});

				sudokuFields[i][j] = field;
				sudokuGrid.add(field, j, i);

			}
		}
		
		sudokuGrid.setGridLinesVisible(true);
		
		//generate an new Sudoku on application launch
		newGame(null);
	}

}
