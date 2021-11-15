package analyser;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImagesForFruit{
    Image originalImage, editableImage, borderedImage;
    int width, height;
    PixelReader pr; PixelWriter pw; WritableImage wi;

    public ImagesForFruit(FileInputStream userFile, int width, int height) {
        this.originalImage = new Image(userFile, width, height, false, true);
        this.editableImage = originalImage;
        this.width = (int)editableImage.getWidth();
        this.height = (int)editableImage.getHeight();
        this.pr = editableImage.getPixelReader();
        this.wi = new WritableImage(pr, width, height);
        this.pw = wi.getPixelWriter();
        this.borderedImage = editableImage;
    }

    public void clusterBorder(int root, HashMap<Integer, ArrayList<Integer>> map) {
        List<Integer> tmpList = map.get(root);
        int furthestLeftPixel = root, furthestRightPixel = root, bottomPixel = tmpList.get(tmpList.size()-1);
        for(int i : tmpList) {
            furthestLeftPixel = i >= tmpList.size() ? calcFurtherLeftPixel(furthestLeftPixel, i) : furthestLeftPixel;
            furthestRightPixel = i >= tmpList.size() ? calcFurtherRightPixel(furthestRightPixel, i) : furthestRightPixel;
        }
        int leftX = calcXFromIndex(furthestLeftPixel),
                rightX = calcXFromIndex(furthestRightPixel),
                topY = calcYFromIndex(root),
                botY = calcYFromIndex(bottomPixel);
        drawBorder(leftX, rightX, topY, botY);
        borderedImage=wi;
    }

    public int calcFurtherLeftPixel(int posA, int posB) {
        return posA%width < posB%width ? posA : posB;
    }

    public int calcFurtherRightPixel(int posA, int posB) {
        return posA%width > posB%width ? posA : posB;
    }

    public int calcXFromIndex(int i) {
        return (i)%width;
    }

    public int calcYFromIndex(int i) {
        return (i)/width;
    }

    public void drawBorder(int leftX, int rightX, int topY, int botY) {
        for(int x=leftX; x<=rightX; x++)
            pw.setColor(x, topY, Color.BLUE);
        for(int y=topY; y<=botY; y++)
            pw.setColor(rightX, y, Color.BLUE);
        for(int x=rightX; x>=leftX; x--)
            pw.setColor(x, botY, Color.BLUE);
        for(int y=botY; y>=topY; y--)
            pw.setColor(leftX, y, Color.BLUE);
    }

    public void setEditableImage(Image newImage) {
        this.editableImage = newImage;
    }

    public void resetEditableImage() {
        editableImage = originalImage;
        pr = editableImage.getPixelReader();
        wi = new WritableImage(pr, width, height);
        pw = wi.getPixelWriter();
    }

    public void setBorderedImage(Image newImg) {
        this.borderedImage = newImg;
    }

    public void editImagePixels(ColorAdjust colorAdjust) {
        for(int y=0; y<width; y++)
            for(int x=0; x<width; x++) {
                Color c = pr.getColor(x, y);
                c = c.deriveColor(colorAdjust.getHue(), colorAdjust.getSaturation(), colorAdjust.getBrightness(), 1);
                pw.setColor(x, y, c);
            }
    }


}
