package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import javafx.stage.Stage;
import sample.Gift;

import java.io.*;
import java.util.*;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class GameController implements Initializable {

    @FXML AnchorPane pane;
    @FXML GridPane grid;
    @FXML ImageView rosie;
    @FXML ImageView iv;
    @FXML Rectangle cover1;
    @FXML Text t;
    @FXML Text instructions;

    ImageView[] viewList;
    Label[] labelList;
    Pane[] borderList;
    ArrayList<Image> imList = new ArrayList<>();
    ArrayList<Gift> giftList = new ArrayList<>();
    ArrayList<String> order = new ArrayList<>();
    TilePane imBox;

    private static int max_row_len = 3;
    private static int cell_width = 140;
    private static int cell_height = 200;
    private static double ratio = .7;
    private int turn = 0;
    private String activePlayer = "";

    @Override public void initialize(URL url, ResourceBundle rb)  {
        ColumnConstraints c1 = new ColumnConstraints(100);
        RowConstraints r1 = new RowConstraints(120);
        grid.setGridLinesVisible(true);
        grid.setMaxSize(1000, 800);
        grid.setAlignment(Pos.CENTER);

        //grid.getColumnConstraints().add(c1);
       // grid.getRowConstraints().add(r1);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.toBack();


        ArrayList<Image> pics;
        try {
             order = getOrder();
             pics = getPics(grid);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        borderList = new Pane[order.size()];
        for(int i = 0; i < borderList.length; i++)
        {
            borderList[i] = new Pane();
            borderList[i].setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
        displayOrder(order);
        activePlayer = order.get(turn);
        instructions.setText(activePlayer + " may now select a present.");
    }

    /*@FXML
    public void trackMouse(MouseEvent e)
    {
        System.out.println("mouse click detected");
        int row, col;
        Node source = (Node)e.getSource();
        row = grid.getRowIndex(source);
        col = grid.getColumnIndex(source);
        System.out.println("mouse clicked at " + row + " " + col);
    }*/

    public void trackMouse(MouseEvent event) {
        int row, col, index;
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != grid) {
            // click on descendant node
            col = grid.getColumnIndex(clickedNode);
            row = grid.getRowIndex(clickedNode);
            System.out.println("Mouse clicked cell: " + col + " And: " + row);
            index = row * max_row_len + col;
            System.out.println(index);
            if(giftList.get(index).opened)
            {
                System.out.println("Stealing gift...");
                String temp = giftList.get(index).holder;
                labelList[index].setText(Integer.toString(index + 1) +". " + activePlayer);
                giftList.get(index).holder = activePlayer;
                activePlayer = temp;
                System.out.println("active player is now " + activePlayer);
            }
            else
            {
                System.out.println("Opening new gift...");
                //borderList[index] = new Pane();
                borderList[index].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
                borderList[index].setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(15))));
                grid.getChildren().remove(this);
                grid.add(borderList[index], col, row);
                giftList.get(index).holder = activePlayer;
                giftList.get(index).opened = true;
                labelList[index].setText(Integer.toString(index + 1) +". " + activePlayer);
                turn++;
                imagePopup(imList.get(index));

                //pane.getChildren().remove(imTemp);
                activePlayer = order.get(turn);
            }
        }
        instructions.setText(activePlayer + " may now steal someone's present or open a new one.");
    }

    public void imagePopup(Image image)
    {
        System.out.println("Popping up " + image.toString());
        Stage tStage = new Stage();
        ImageView iv = new ImageView(image);
        iv.setFitHeight(800);
        iv.setFitWidth(1000);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        Scene tScene = new Scene(new Group(iv));
        //ImageView imTemp = new ImageView(imList.get(index));
        //imTemp.setFitHeight(500);
        //Popup pTemp = new Popup();
        //pTemp.getContent().add(imTemp);
        tStage.setMaxHeight(800);
        tStage.setMaxWidth(1000);
        tStage.setScene(tScene);
        tStage.show();

    }

    private ArrayList<Image> getPics(GridPane grid)
    {
        File folder = new File("C:\\Users\\kiyos\\Documents\\My shit\\WhiteElephant\\src\\giftpics");
        File[] listpics = folder.listFiles();
        int row, col, count = 0;
        double height, width;
        for(File f : listpics) {
            String path = f.toURI().toString();
            String what = path.substring(path.lastIndexOf("/") + 1, path.indexOf("."));
            System.out.println(path.substring(path.lastIndexOf("/") + 1, path.indexOf(".")));
            Image im = new Image((f.toURI().toString()));
            imList.add(im);
            giftList.add(new Gift(what));
        }
        viewList = new ImageView[listpics.length];
        labelList = new Label[listpics.length];
        borderList = new Pane[listpics.length];
        for(int i = 0; i < viewList.length; i++)
        {
            Image im = imList.get(i);
            viewList[i] = new ImageView(im);

            //Label label = new Label(Integer.toString(i + 1));
            //label.setFont(Font.font("Garamond", 16));
            /*height = im.getHeight();
            width = im.getWidth();
            System.out.println(width / height);
            if(width / height < ratio) {viewList[i].setFitHeight(cell_height); System.out.println("set width to " + im.getWidth());}
            else {viewList[i].setFitWidth(cell_width); System.out.println("set width to " + im.getWidth());}*/

            viewList[i].setFitHeight(cell_height);
            viewList[i].setFitWidth(cell_width);
            viewList[i].setPreserveRatio(true);
            viewList[i].setSmooth(true);
            labelList[i] = new Label(Integer.toString(i + 1));

            borderList[i] = new Pane();
            try {
                Image bg = new Image(new FileInputStream("C:\\Users\\kiyos\\Documents\\My shit\\WhiteElephant\\src\\closedPresent.jpg"));
                //borderList[i].setBackground(new Background(new BackgroundImage(bg, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

            //borderList[i].setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(15))));

            row = count / max_row_len;
            col = count % max_row_len;
            grid.add(viewList[i], col, row);
            grid.add(makeRect(cell_width, cell_height - 20), col, row);
            grid.add(labelList[i], col, row);
            grid.add(borderList[i], col, row);
            grid.setHalignment(labelList[i], HPos.CENTER);
            grid.setValignment(labelList[i], VPos.BOTTOM);
            System.out.println("Putting image " + count +  " in position " + row + " " + col);
            count++;
            //viewList[i].
        }
        return imList;
    }


    public Rectangle makeRect(int width, int height)
    {
        Rectangle r = new Rectangle(width, height);
        r.setFill(Color.RED);
        r.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(r.getOpacity() != 0) {
                    //r.setOpacity(0);
                    grid.getChildren().remove(r);
                }
                else
                {

                }

            }
        });
        return r;
    }

    private void displayOrder(ArrayList<String> order)
    {
        int count = 1;
        String turnOrder = "Turn Order\n\n";
        for(String name : order)
        {
            System.out.println(name);
            turnOrder += Integer.toString(count) + ". " + name + "\n";
            count++;
        }
        t.setText(turnOrder);
    }

    private ArrayList<String> getOrder() throws IOException {
        String name;
        ArrayList<String> order = new ArrayList<String>();
        /*File f = new File("C:\\Users\\kiyos\\Documents\\My shit\\WhiteElephant\names.txt");
        FileInputStream fs = new FileInputStream(f);
        */

        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\kiyos\\Documents\\My shit\\WhiteElephant\\names.txt"));
        while ((name = reader.readLine()) != null) {
            order.add(name);
        }
        Collections.shuffle(order);
        return order;
    }

    @FXML void remove()
    {
        cover1.setOpacity(0);
        System.out.println("removing");
    }
}
