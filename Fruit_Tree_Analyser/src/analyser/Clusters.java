package analyser;

import javafx.scene.image.*;
import javafx.scene.paint.Color;
import java.util.*;
import java.util.List;

public class Clusters{
    HashMap<Integer, ArrayList<Integer>> map;

    public Clusters() {
        this.map = new HashMap<>();
    }

    public void createHashMap(ArrayForFruit a) {
        for(int x=0; x<a.array.length; x++)
            if(a.pixelIsWhite(x)) {
                int root = a.find(a.array, x);
                if (rootNotStored(root)) {
                    ArrayList<Integer> tmpList = new ArrayList<>();
                    for (int i=x; i<a.array.length; i++)
                        if (a.pixelIsWhite(i) && currentRootEqualsTempRoot(i, root, a))
                            tmpList.add(i);
                    map.put(root, tmpList);
                }
            }
    }

    public boolean rootNotStored(int root) {
        return !map.containsKey(root);
    }

    public boolean currentRootEqualsTempRoot(int i, int root, ArrayForFruit a) {
        return a.find(a.array, i) == root;
    }

    public void removeOutliers(int min, boolean doIQR) {
        int IQR = calcIQR(createSortedSizeArray(), createSortedSizeArray().size());
        Set<Map.Entry<Integer, ArrayList<Integer>>> setOfEntries = map.entrySet();
        Iterator<Map.Entry<Integer, ArrayList<Integer>>> iterator = setOfEntries.iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, ArrayList<Integer>> entry = iterator.next();
            int size = entry.getValue().size();
            if (size<IQR && doIQR) {
                iterator.remove();
            } else if (size < min) {
                iterator.remove();
            }
        }
    }

    public ArrayList<Integer> createSortedSizeArray() {
        ArrayList<Integer> arrayOfSizes = new ArrayList<>();
        for(int i : map.keySet())
            arrayOfSizes.add(map.get(i).size());
        Collections.sort(arrayOfSizes);
        return arrayOfSizes;
    }

    public int calcIQR(ArrayList<Integer> a, int length) {
        int middleIndex = calcMedian(0, length);
        int Q1 = a.get(calcMedian(0, middleIndex));
        int Q3 = a.get(calcMedian(middleIndex+1, length));
        return (Q3-Q1);
    }

    public int calcMedian(int l, int r) {
        int n = r-l+1;
        n = (n+1)/2-1;
        return n+l;
    }

    public int getClusterSizeRank(int root) {
        return rankSetsBySize().get(root);
    }

    public HashMap<Integer, Integer> createSizeHashMap() {
        HashMap<Integer, Integer> newHM = new HashMap<>();
        for(int i : map.keySet()) {
            newHM.put(i, map.get(i).size());
        }
        return newHM;
    }

    public HashMap<Integer, Integer> sortByValue() {
        HashMap<Integer, Integer> hm = createSizeHashMap();
        List<HashMap.Entry<Integer, Integer>> list = new LinkedList<>(hm.entrySet());
        list.sort(Map.Entry.comparingByValue());
        HashMap<Integer, Integer> sortedHashMap = new LinkedHashMap<>();

        for (HashMap.Entry<Integer, Integer> a : list) {
            sortedHashMap.put(a.getKey(), a.getValue());
        }
        return sortedHashMap;
    }

    public HashMap<Integer, Integer> rankSetsBySize() {
        HashMap<Integer, Integer> sortedMap = sortByValue();
        int newSize = sortedMap.keySet().size();
        for(int i : sortedMap.keySet()) {
            sortedMap.replace(i, newSize);
            newSize--;
        }
        return sortedMap;
    }

    public int totalFruits() {
        return map.keySet().size();
    }

    public void randomlyColorCluster(int root, ImagesForFruit fruitImage, PixelWriter pw, Color color) {
        ArrayList<Integer> tempArray = map.get(root);
        for(int i : tempArray) {
            pw.setColor(fruitImage.calcXFromIndex(i), fruitImage.calcYFromIndex(i), color);
        }
    }

    public void colorAllClusters(BlackAndWhite blackWhiteImage, ImagesForFruit fruitImage) {
        PixelReader pixelReader = blackWhiteImage.coloredImage.getPixelReader();
        WritableImage writableImage = new WritableImage(pixelReader, (int)blackWhiteImage.coloredImage.getWidth(), (int)blackWhiteImage.coloredImage.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        for(int i : map.keySet()) {
            Random rand = new Random();
            float r = rand.nextFloat(), g = rand.nextFloat(), b = rand.nextFloat();
            Color randomColor = new Color(r, g, b, 1);
            randomlyColorCluster(i, fruitImage, pixelWriter, randomColor);
        }
        blackWhiteImage.coloredImage = writableImage;
    }
}
