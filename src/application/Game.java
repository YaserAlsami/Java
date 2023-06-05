package application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Game extends Application {
	
	//private String shipDirection;


	Image logoImage = new Image("C:\\CST8221\\Assig12\\src/logo.png");
	
    private boolean designMode = true;
    private int[][] grid;
    private Stage primaryStage;
    private BorderPane root;
   // private GridPane leftBoard;
    private GridPane leftBoard = new GridPane();
    
    private static final int EMPTY = 0;
    private static final int SHIP = 1;
  


    private static final int BOARD_SIZE = 10; // Size of the game board
    private static final int CELL_SIZE = 40; // Size of each cell in the game board 
 //   private GridPane leftBoard; 
    private HBox boardContainer = createBoard(BOARD_SIZE);

    public static void main(String[] args) {
        launch(args);
    }
   

    /**
     * Overrides the start method from the Application class.
     * Initializes and sets up the primary stage for the application.
     *
     * @param primaryStage the primary stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("A12 Yaser Alsami Farid Jark"); // Set the title of the page to my team name 

        root = new BorderPane(); // creating a new object from BorderPane class and naming it root 
        root.setTop(createControlPanel()); // Create and set the control panel at the top of the border pane
        root.setCenter(createBoard(3)); // Create and set the game board with a default dimension of 3 at the center of the border pane
        grid = new int[2 * BOARD_SIZE][2 * BOARD_SIZE]; // Initialize the grid for the game board
        root.setPadding(new Insets(10)); // Set the padding of the root border pane
        
        // Load the logo image
        Image logoImage = new Image("C:\\CST8221\\Assig12\\src/logo.png");

        // Create the ImageView for the logo
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(100);
        logoImageView.setPreserveRatio(true);

        // Create a VBox to hold the logo and the control panel
        VBox logoAndControlPanel = new VBox(10);
        logoAndControlPanel.setAlignment(Pos.CENTER);
        logoAndControlPanel.getChildren().addAll(logoImageView, createControlPanel());

        root.setTop(logoAndControlPanel);

        Scene scene = new Scene(root); // Create a new scene with the root border pane
        primaryStage.setScene(scene); // Set the scene of the primary stage
        primaryStage.show(); // Display the primary stage
    }

    
    /**
     * Creates and returns the control panel for the application.
     *
     * @return the VBox control panel containing various components
     */
    private VBox createControlPanel() {
    	Button randomButton = new Button("Random");// creating a new button naming  the button randomButton and labeling it to Random
    	randomButton.setOnAction(event -> {// using lambda and calling setOnAction method to call genrateRandomBoats when clicked  
    		generateRandomBoats(); // Place ships randomly on the grid 
    	});
        VBox controlPanel = new VBox(20);// creating an object from VBox class naming it controlPanel and passing 20 to set the size of the frame
        controlPanel.setAlignment(Pos.BASELINE_CENTER);
        controlPanel.setPadding(new Insets(20));//setting the size of the horizontal portion of the frame  

        // Reset button
        Button resetButton = new Button("Reset"); // creating a new button naming  the button resetButton and labeling it to Reset
        resetButton.setOnAction(event -> {
            System.out.println("Reset button clicked");
        });

        // Dimension choice box
        ChoiceBox<Integer> dimensionChoiceBox = new ChoiceBox<>();
        dimensionChoiceBox.getItems().addAll( 3, 4, 5, 6, 7, 8);
        dimensionChoiceBox.setValue(3);
        dimensionChoiceBox.setOnAction(event -> {
            int selectedDimension = dimensionChoiceBox.getValue();
            System.out.println("Dimension set to: " + selectedDimension);
            if (designMode) {
                HBox board = createBoard(selectedDimension);
                root.setCenter(board);
            }
        });

        // History area
        Label historyLabel = new Label("History Area");
        TextArea historyTextArea = new TextArea();
        historyTextArea.setPrefRowCount(0);
        historyTextArea.setWrapText(true);

        // Add all components to the control panel
        controlPanel.getChildren().addAll(resetButton, dimensionChoiceBox, historyLabel, historyTextArea, randomButton);

        return controlPanel;
    }





   
    private int calculateTotalTilesUsed(int dimension) {
        return (dimension * (dimension + 1) * (dimension + 2)) / 6;
    }

    /**
     * Creates and returns a game board with the specified dimension.
     *
     * @param dimension the dimension of the game board
     * @return the HBox container holding the left and right game boards
     */
    private HBox createBoard(int dimension) {
        int boardSize = 2 * dimension;

        // Create grid boards
        GridPane leftBoard = createGridBoard(boardSize); // Initializing leftboard (Player)
        GridPane rightBoard = createGridBoard(boardSize); // Initializing rightboard (The AI)

        // Create labels for rows and columns
        Label[] leftRowLabels = new Label[boardSize];
        Label[] leftColLabels = new Label[boardSize];
        Label[] rightRowLabels = new Label[boardSize];
        Label[] rightColLabels = new Label[boardSize];

        // Add buttons and labels to the left board
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Button leftCell = createCell(row, col);
                leftBoard.add(leftCell, col, row);
                leftCell.setOnAction(e -> placeShip(leftCell));
            }

            leftRowLabels[row] = new Label(Character.toString((char) ('A' + row)));
            leftRowLabels[row].setPrefSize(CELL_SIZE, CELL_SIZE);
            leftRowLabels[row].setStyle("-fx-font-weight: bold;");
            leftRowLabels[row].setAlignment(Pos.CENTER);
            leftBoard.add(leftRowLabels[row], boardSize, row);
        }

        for (int col = 0; col < boardSize; col++) {
            leftColLabels[col] = new Label(Character.toString((char) ('a' + col)));
            leftColLabels[col].setPrefSize(CELL_SIZE, CELL_SIZE);
            leftColLabels[col].setStyle("-fx-font-weight: bold;");
            leftColLabels[col].setAlignment(Pos.CENTER);
            leftBoard.add(leftColLabels[col], col, boardSize);
        }

        // Add buttons and labels to the right board
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Button rightCell = createCell(row, col);
                rightBoard.add(rightCell, col, row);
            }

            rightRowLabels[row] = new Label(Character.toString((char) ('A' + row)));
            rightRowLabels[row].setPrefSize(CELL_SIZE, CELL_SIZE);
            rightRowLabels[row].setStyle("-fx-font-weight: bold;");
            rightRowLabels[row].setAlignment(Pos.CENTER);
            rightBoard.add(rightRowLabels[row], boardSize, row);
        }

        for (int col = 0; col < boardSize; col++) {
            rightColLabels[col] = new Label(Character.toString((char) ('a' + col)));
            rightColLabels[col].setPrefSize(CELL_SIZE, CELL_SIZE);
            rightColLabels[col].setStyle("-fx-font-weight: bold;");
            rightColLabels[col].setAlignment(Pos.CENTER);
            rightBoard.add(rightColLabels[col], col, boardSize);
        }

        // Create progress bars for left board and right board
        ProgressBar leftProgressBar = new ProgressBar();
        ProgressBar rightProgressBar = new ProgressBar();

        // Create VBox to hold the left board and left progress bar
        VBox leftBoardContainer = new VBox(10);
        leftBoardContainer.setAlignment(Pos.CENTER);
        leftBoardContainer.getChildren().addAll(leftBoard, leftProgressBar);

        // Create VBox to hold the right board and right progress bar
        VBox rightBoardContainer = new VBox(10);
        rightBoardContainer.setAlignment(Pos.CENTER);
        rightBoardContainer.getChildren().addAll(rightBoard, rightProgressBar);

        // Create HBox to hold the left board container, right board container, and progress bars
        HBox boardContainer = new HBox(10);
        boardContainer.setAlignment(Pos.CENTER);
        boardContainer.getChildren().addAll(leftBoardContainer, rightBoardContainer);

        return boardContainer;
    }


    /**
     * Creates a grid-based game board with the specified size.
     *
     * @param boardSize the size of the grid-based game board
     * @return the GridPane representing the game board
     */
    private GridPane createGridBoard(int boardSize) {
        GridPane board = new GridPane();
        board.setAlignment(Pos.CENTER);
        board.setHgap(1);
        board.setVgap(1);

        return board;
    }

    /**
     * Creates a button representing a cell on the game board.
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @return the Button representing the cell
     */
    private Button createCell(int row, int col) {
        Button cell = new Button();
        cell.setPrefSize(CELL_SIZE, CELL_SIZE);
        cell.setStyle("-fx-border-color: red;");

        // Set the action to perform when the button is clicked
        cell.setOnAction(event -> {
            System.out.println("Button clicked: " + cell.getId());
        });
        cell.setId("cell_" + (char) ('A' + row) + (char) ('A' + col));// Set a unique ID for each cell based on its row and column indices

        return cell;
    }




    /**
     * Places a ship on the game board based on the specified cell.
     *
     * @param cell the Button representing the selected cell
     */
    private void placeShip(Button cell) {
       System.out.println(cell);
       // to be implemented in future assignments 
    }

    /**
     * Generates random boats and places them on the game board.
     */
    private void generateRandomBoats() {
       for (int boatSize = 1; boatSize <= BOARD_SIZE; boatSize++) {
          for (int j = 1; j < BOARD_SIZE - boatSize + 1; j++) {
             createRandomBoat(boatSize);
          }
       }
  
       // refreshBoard();
    }

    /**
     * Creates a random boat of the specified size and places it on the game board.
     *
     * @param boatSize the size of the boat to be created
     */
    private void createRandomBoat(int boatSize) {
        Random rand = new Random();
        int randRow, randCol;
        boolean validPosition = false;

        do {
            // Generate random row and column positions
            randRow = rand.nextInt(2 * BOARD_SIZE);
            randCol = rand.nextInt(2 * BOARD_SIZE);

            // Check if the boat can be placed horizontally within the board bounds
            if (randCol + boatSize <= 2 * BOARD_SIZE) {
                validPosition = true;
                // Check if the cells for the boat are empty
                for (int pos = 0; pos < boatSize; pos++) {
                    if (grid[randRow][randCol + pos] != EMPTY) {
                        validPosition = false;
                        break;
                    }
                }
            }

            // If a valid horizontal position is not found, check if the boat can be placed vertically
            if (!validPosition && randRow + boatSize <= 2 * BOARD_SIZE) {
                validPosition = true;
                // Check if the cells for the boat are empty
                for (int pos = 0; pos < boatSize; pos++) {
                    if (grid[randRow + pos][randCol] != EMPTY) {
                        validPosition = false;
                        break;
                    }
                }
            }
        } while (!validPosition);

        // Place the boat on the grid based on the valid position found
        if (randCol + boatSize <= 2 * BOARD_SIZE) {
            for (int pos = 0; pos < boatSize; pos++) {
                grid[randRow][randCol + pos] = SHIP;
            }
        } else {
            for (int pos = 0; pos < boatSize; pos++) {
                grid[randRow + pos][randCol] = SHIP;
            }
        }
    }
    
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    /**
     * Refreshes the game board by recreating the board container and updating the scene.
     */
    private void refreshBoard() {
        if (boardContainer != null) {
            root.getChildren().remove(boardContainer);
        }

        // Recreate the board container with the desired dimension
        boardContainer = createBoard(BOARD_SIZE);

        // Create a new Scene with the root container and update the active scene of the primaryStage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
    }




 

   

 


}
