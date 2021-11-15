package analyser;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Window;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class Controller {
    @FXML
    ImageView chosenImageView;
    @FXML ImageView blackWhiteImageView;
    @FXML
    Slider hueSlider, saturationSlider, brightnessSlider;
    @FXML
    Label yourImage, bwVersion, start;
    @FXML
    StackPane stack, sizePane;
    @FXML
    RadioButton radio, outliers, colorRadio;
    @FXML
    Button bwBut;
    @FXML
    AnchorPane noMenu;
    @FXML
    HBox menu, recogSettings, colorSettings;
    @FXML
    TextField minClusterSize , totalFruits;
    Color fruitColor;
    ImagesForFruit fruitImage;
    BlackAndWhite blackWhiteImage;
    Clusters clusterMap;
    Boolean sizesAreShown = false;

    public void fileChooser() throws FileNotFoundException {
            resetImageGUI();
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPEG Files", "*.jpg"), new FileChooser.ExtensionFilter("PNG Files", "*.png"));
            File file = fc.showOpenDialog(null);
            fruitImage = new ImagesForFruit(new FileInputStream(file), (int) chosenImageView.getFitWidth(), (int) chosenImageView.getFitHeight());
            chosenImageView.setImage(fruitImage.originalImage);
            yourImage.setVisible(true);
            start.setVisible(true);

    }

    public void createTooltip(Node placement, String text) {
        Tooltip tooltip = new Tooltip();
        tooltip.setText(text);
        tooltip.setShowDelay(Duration.millis(0));
        tooltip.setHideDelay(Duration.millis(0));
        Tooltip.install(placement, tooltip);
    }

    public void resetImageGUI() {
        bwVersion.setVisible(false);
        yourImage.setVisible(false);
        chosenImageView.setImage(null);
        blackWhiteImageView.setImage(null);
        start.setVisible(false);
        outliers.disarm();
    }

    public void getColourAtMouse(javafx.scene.input.MouseEvent mouseEvent) {
            if (chosenImageView!=null) {
                start.setVisible(false);
                fruitColor = chosenImageView.getImage().getPixelReader().getColor((int)mouseEvent.getX(), (int)mouseEvent.getY());
                if (blackWhiteImageView.getImage() != null)
                    displayBlackWhiteImage();
            }
    }

    public void getClusterAtMouse(javafx.scene.input.MouseEvent event) {
            int x=(int)event.getX(), y=(int)event.getY();
            if(blackWhiteImage!=null)
                if(blackWhiteImage.fruitArray.pixelIsWhite(blackWhiteImage.fruitArray.calculateArrayPosition(y, x))) {
                    int root = blackWhiteImage.fruitArray.find(blackWhiteImage.fruitArray.array, blackWhiteImage.fruitArray.calculateArrayPosition(y, x));
                    Tooltip tooltip = new Tooltip();
                    int rank = clusterMap.getClusterSizeRank(root);
                    tooltip.setText("Fruit/Cluster number: " + rank + "\n" + "Estimated size (pixel units): " + clusterMap.map.get(root).size());
                    if(sizesAreShown)
                        Tooltip.install(sizePane, tooltip);
                    else Tooltip.install(chosenImageView, tooltip);
                }
    }

    public void displayBlackWhiteImage() {
            initialiseBlackWhiteImage();
            blackWhiteImageView.setImage(blackWhiteImage.bwImage);
            bwVersion.setVisible(true);
            colorRadio.setSelected(false);
            sizePane.getChildren().removeAll();
            fruitImage.setBorderedImage(fruitImage.editableImage);
            chosenImageView.setImage(fruitImage.borderedImage);

    }

    public void initialiseBlackWhiteImage() {
        blackWhiteImage = new BlackAndWhite(fruitImage.editableImage, fruitColor, fruitImage.width, fruitImage.height);
    }

    public Alert createAlert(String title, String header) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title);
        a.setHeaderText(header);
        ImageView imageView = new ImageView(new Image(String.valueOf(getClass().getResource("/resources/warning.png"))));
        a.setGraphic(imageView);
        return a;
    }

    public void showOnScreenSizes() {
        if(radio.isSelected()) {
            sizePane.toFront();
            sizesAreShown=true;
        } else {
            sizePane.toBack();
            sizesAreShown=false;
        }
    }

    public void createSizePane() {
        sizePane=new StackPane();
        stack.getChildren().add(sizePane);
        sizePane.toBack();
        for (int i : clusterMap.map.keySet()) {
            Font font = Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 10);
            Label label = new Label(String.valueOf(clusterMap.getClusterSizeRank(i)));
            label.setTextFill(Color.WHITE);
            label.setFont(font);
            sizePane.getChildren().add(label);
            label.setTranslateX(fruitImage.calcXFromIndex(i) - (fruitImage.width >> 1)+3);
            label.setTranslateY(fruitImage.calcYFromIndex(i) - (fruitImage.width >> 1)+3);
        }
    }

    public void setPixelBorders() {
            fruitImage.resetEditableImage();
            clusterMap = new Clusters();
            clusterMap.createHashMap(blackWhiteImage.fruitArray);

            if(outliers.isSelected() && !minClusterSize.getText().equals(""))
                clusterMap.removeOutliers(Integer.parseInt(minClusterSize.getText()), true);
            else if(outliers.isSelected())
                clusterMap.removeOutliers(2, true);
            else if(!minClusterSize.getText().equals(""))
                clusterMap.removeOutliers(Integer.parseInt(minClusterSize.getText()), false);

            for(int i : clusterMap.map.keySet())
                fruitImage.clusterBorder(i, clusterMap.map);
            chosenImageView.setImage(fruitImage.borderedImage);
            createSizePane();
            colorFruits();
            totalFruits.setText(clusterMap.totalFruits() + " Fruits/Clusters");
        }


    public void displayMenu() {
        menu.setVisible(true);
        noMenu.setVisible(false);
    }


    public void displayRecogMenu() {
        recogSettings.setVisible(true);
        colorSettings.setVisible(false);
    }

    public void displayColorMenu() {
        recogSettings.setVisible(false);
        colorSettings.setVisible(true);
    }

    public void colorFruits() {
        try{
            clusterMap.colorAllClusters(blackWhiteImage, fruitImage);
        } catch (Exception e) {
            createAlert("Uh oh..", "Perform fruit recognition first!");
        }
    }

    public void showColoredFruits() {
        if(chosenImageView.getImage()!=null && blackWhiteImageView.getImage()!=null) {
            if (colorRadio.isSelected())
                if (blackWhiteImage.coloredImage == blackWhiteImage.bwImage) {
                    createAlert("Uh oh..", "Try fruit recognition first!");
                } else blackWhiteImageView.setImage(blackWhiteImage.coloredImage);
            else blackWhiteImageView.setImage(blackWhiteImage.bwImage);
        } else {
            createAlert("Uh oh..", "Try choosing and converting an image first!");
        }
    }

    public void colourIndividualCluster(javafx.scene.input.MouseEvent event) {
        try {
            blackWhiteImage.coloredImage=blackWhiteImage.bwImage;
            int x=(int)event.getX(), y=(int)event.getY();
            Random rand = new Random();
            float r = rand.nextFloat(), g = rand.nextFloat(), b = rand.nextFloat();
            Color randomColor = new Color(r, g, b, 1);
            clusterMap.randomlyColorCluster(blackWhiteImage.fruitArray.find(blackWhiteImage.fruitArray.array, blackWhiteImage.fruitArray.calculateArrayPosition(y,x)), fruitImage, blackWhiteImage.pw, randomColor);
            blackWhiteImageView.setImage(blackWhiteImage.coloredImage);
        } catch (Exception ignore) {}
    }

}
