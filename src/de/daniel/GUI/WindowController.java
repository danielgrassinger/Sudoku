package de.daniel.GUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import javax.imageio.ImageIO;

import de.daniel.Field.FieldSolver;
import de.daniel.Field.PlayField;
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
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class WindowController implements Initializable {

	private Stage myStage;

	@FXML
	private GridPane sudokuGrid;
	private PlayField playField;

	private TextField[][] sudokuFields;

	@FXML
	protected void newGame(ActionEvent event) {

		playField = new PlayField();
		playField.init();

		setField(playField.getField());
		lockField();

	}

	@FXML
	protected void restart(ActionEvent event) {
		playField.setField(playField.getInitField());
		setField(playField.getField());
		lockField();

	}

	@FXML
	protected void solve(ActionEvent event) {
		FieldSolver solver = new FieldSolver();
		solver.setField(playField.getField());
		solver.solveSudokuExtern();
		playField.setField(solver.getField());
		setField(playField.getField());
	}

	@FXML
	protected void load(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Sudoku");
		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("CSV files", "*.csv"));
		try {
			File file = fileChooser.showOpenDialog(myStage);

			int[][] loadField = new int[9][9];
			BufferedReader filereader = new BufferedReader(new FileReader(file));
			for (int i = 0; i < 9; i++) {
				String[] line = filereader.readLine().split(";");
				if (line.length != 9) {
					filereader.close();
					return;
				}
				for (int j = 0; j < 9; j++) {
					loadField[i][j] = Integer.parseInt(line[j]);
				}
			}
			filereader.close();
			playField.setField(loadField);
			playField.setInitField(playField.getField());
			setField(playField.getField());
			lockField();
		} catch (Exception e) {
			// Ignore opening errors
			// e.printStackTrace();
		}
	}

	@FXML
	protected void saveCSV(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Sudoku");
		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("CSV files", "*.csv"));
		try {
			File file = fileChooser.showSaveDialog(myStage);

			int[][] field = playField.getField();

			String saveString = "";
			for (int i = 0; i < 9; i++) {
				StringJoiner joiner = new StringJoiner(";");
				for (int j = 0; j < 9; j++) {
					joiner.add(String.valueOf(field[i][j]));
				}
				saveString = saveString + joiner.toString() + "\n";
			}

			PrintWriter filewriter = new PrintWriter(new BufferedWriter((new FileWriter(file))));
			filewriter.write(saveString);
			filewriter.flush();
			filewriter.close();

		} catch (Exception e) {
			// Ignore opening errors
			// e.printStackTrace();
		}
	}

	@FXML
	protected void savePNG(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Sudoku");
		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("PNG files", "*.png"));
		try {
			File file = fileChooser.showSaveDialog(myStage);

			Bounds bounds = sudokuGrid.getBoundsInLocal();
			Bounds screenBounds = sudokuGrid.localToScene(bounds);
			WritableImage image = sudokuGrid.getScene().snapshot(null);

			ImageIO.write(SwingFXUtils.fromFXImage(image, null).getSubimage((int) screenBounds.getMinX(),
					(int) screenBounds.getMinY(), (int) screenBounds.getWidth(), (int) screenBounds.getHeight()), "png",
					file);

		} catch (Exception e) {
			// Ignore opening errors
			// e.printStackTrace();
		}
	}

	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
	}

	private void setField(int[][] field) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int value = field[i][j];
				if (value >= 1 && value <= 9) {
					sudokuFields[i][j].setText("" + value);
				} else {
					sudokuFields[i][j].setText("");
				}
			}
		}
	}

	private void setPlayField() {
		int[][] newField = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				String text = sudokuFields[i][j].getText();
				if (text.equals("")) {
					newField[i][j] = 0;
				} else {
					newField[i][j] = Integer.parseInt(text);
				}
			}
		}
		if (!playField.setField(newField)) {
			setField(playField.getField());
		}
	}

	private void lockField() {
		for (TextField[] fields : sudokuFields) {
			for (TextField f : fields) {
				if (f.getText().equals("")) {
					f.setEditable(true);
					f.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

				} else {
					f.setEditable(false);
					f.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

				}
			}
		}
	}

	public void setStage(Stage stage) {
		myStage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sudokuFields = new TextField[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				TextField field = new TextField("");
				field.setAlignment(Pos.CENTER);

				field.setFont(Font.font(30));
				field.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				field.setBackground(null);
				field.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
				field.textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {

						setPlayField();
					}
				});

				// field.setBorder(null);
				sudokuFields[i][j] = field;
				sudokuGrid.add(field, j, i);

			}
		}
		sudokuGrid.setGridLinesVisible(true);
		newGame(null);
	}

}
