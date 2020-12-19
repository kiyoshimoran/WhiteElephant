package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Main extends Application {

    ImageView[] viewList;
    ArrayList<Image> imList = new ArrayList<>();
    TilePane imBox;

    private static int max_row_len = 6;

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(20);
        grid.toBack();
        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
        primaryStage.setTitle("Tom White Elephant 2020");
        primaryStage.setScene(new Scene(root, 1200, 900));

        //getPics(grid);

        primaryStage.show();


    }

    private ArrayList<Image> getPics(GridPane grid)
    {
        File folder = new File("C:\\Users\\kiyos\\Documents\\My shit\\WhiteElephant\\src\\giftpics");
        File[] listpics = folder.listFiles();
        int row, col, count = 0;
        for(File f : listpics) {
            String path = f.getPath();
            System.out.println(f.toURI().toString());
            Image im = new Image((f.toURI().toString()));
            imList.add(im);
        }
        viewList = new ImageView[listpics.length];
        for(int i = 0; i < viewList.length; i++)
        {

            viewList[i] = new ImageView(imList.get(i));
            viewList[i].setFitHeight(200);
            viewList[i].setFitWidth(120);
            viewList[i].setPreserveRatio(true);
            viewList[i].setSmooth(true);
            row = count / max_row_len;
            col = count % max_row_len;
            grid.add(viewList[i], col, row);
            System.out.println("Putting image " + count +  "in position " + row + " " + col);
            count++;
            //viewList[i].
        }

        return imList;
    }



    public static void main(String[] args) {
        launch(args);
    }
}
