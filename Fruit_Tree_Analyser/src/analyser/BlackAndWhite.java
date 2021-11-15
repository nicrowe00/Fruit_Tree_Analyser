package analyser;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class BlackAndWhite{
    Image bwImage, coloredImage;
    PixelReader pr; PixelWriter pw; WritableImage wi;
    double hueDifference, saturationDifference, brightnessDifference;
    ArrayForFruit fruitArray;

    public BlackAndWhite(Image image, Color fruitColor, int width, int height) {
        this.bwImage = image;
        this.pr = bwImage.getPixelReader();
        this.wi = new WritableImage(pr, width, height);
        this.pw = wi.getPixelWriter();
        this.hueDifference = 360;
        this.saturationDifference = 0.4;
        this.brightnessDifference = 0.3;
        this.fruitArray = new ArrayForFruit(width, height);
        this.bwImage = createBlackWhiteImage(fruitColor);
        this.coloredImage = bwImage;
    }

    public Image createBlackWhiteImage(Color fruitColor) {
        boolean moreRed = decideRGBofFruit(fruitColor.getRed(), fruitColor.getGreen(), fruitColor.getBlue());
        for(int y=0; y<bwImage.getHeight(); y++)
            for(int x=0; x<bwImage.getWidth(); x++) {
                Color pixelColor = pr.getColor(x,y);
                if(moreRed)
                    moreRedFruitRecog(fruitColor, pixelColor, x, y);
                else
                    moreBlueFruitRecog(fruitColor, pixelColor, x, y);
            }
        fruitArray.unionFruitPixels();
        coloredImage = bwImage;
        return wi;
    }

    public void moreRedFruitRecog(Color fruitColor, Color pixelColor, int x, int y) {
        double r = pixelColor.getRed();
        if(pixelIsPartOfFruit(fruitColor, pixelColor) && r>pixelColor.getGreen() && r>pixelColor.getGreen()) {
            pw.setColor(x, y, Color.WHITE);
            fruitArray.addFruitToArray(x, y);
        } else {
            pw.setColor(x, y, Color.BLACK);
            fruitArray.addNonFruitToArray(x, y);
        }
    }

    public void moreBlueFruitRecog(Color fruitColor, Color pixelColor, int x, int y) {
        double b = pixelColor.getBlue();
        if (pixelIsPartOfFruit(fruitColor, pixelColor) && b > pixelColor.getRed() && b > pixelColor.getGreen()) {
            pw.setColor(x, y, Color.WHITE);
            fruitArray.addFruitToArray(x, y);
        } else {
            pw.setColor(x, y, Color.BLACK);
            fruitArray.addNonFruitToArray(x, y);
        }
    }

    public boolean decideRGBofFruit(double r, double g, double b) {
        return r > g && r > b;
    }

    public boolean pixelIsPartOfFruit(Color fruitColor, Color pixelColor) {
        return compareHue(fruitColor.getHue(), pixelColor.getHue(), hueDifference)
                && compareSaturation(fruitColor.getSaturation(), pixelColor.getSaturation(), saturationDifference)
                && compareBrightness(fruitColor.getBrightness(), pixelColor.getBrightness(), brightnessDifference);
    }

    public boolean compareHue(double firstHue, double secondHue, double difference) {
        return (Math.abs(firstHue - secondHue) < difference);
    }

    public boolean compareSaturation(double firstSaturation, double secondSaturation, double difference) {
        return (Math.abs(firstSaturation - secondSaturation) < difference);
    }

    public boolean compareBrightness(double firstBrightness, double secondBrightness, double difference) {
        return (Math.abs(firstBrightness - secondBrightness) < difference);
    }

    public void setHueDifference(double hueDiff) {
        hueDifference = hueDiff;
    }

    public void setSaturationDifference(double saturationDiff) {
        saturationDifference = saturationDiff;
    }

    public void setBrightnessDifference(double brightnessDiff) {
        brightnessDifference = brightnessDiff;
    }

    public double getHueDifference() {
        return hueDifference;
    }

    public double getSaturationDifference() {
        return saturationDifference;
    }

    public double getBrightnessDifference() {
        return brightnessDifference;
    }
}

