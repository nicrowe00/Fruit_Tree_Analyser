package analyser;

public class ArrayForFruit {
    int[] array;
    int imgWidth, imgHeight;

    public ArrayForFruit(int imgWidth, int imgHeight) {
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.array = new int[imgWidth*imgHeight];
    }

    public void addFruitToArray(int x, int y) {
        int pos = calculateArrayPosition(y, x);
        array[pos] = pos;
    }

    public void addNonFruitToArray(int x, int y) {
        array[calculateArrayPosition(y, x)] = -1;
    }

    public int calculateArrayPosition(int y, int x) {
        return (y*imgWidth) + x;
    }

    public void unionFruitPixels() {
        for (int i=0; i<array.length; i++) {
            if (pixelIsWhite(i)) {
                int top = i-imgWidth, right = i+1, bottom = i+imgWidth, left = i-1;
                if (!atTopEdge(i) && pixelIsWhite(top))
                    unionPixels(i, top);
                if (!atRightEdge(i) && pixelIsWhite(right))
                    unionPixels(i, right);
                if (!atBottomEdge(i) && pixelIsWhite(bottom))
                    unionPixels(i, bottom);
                if (!atLeftEdge(i) && pixelIsWhite(left))
                    unionPixels(i, left);
            }
        }
    }

    public boolean pixelIsWhite(int i) {
        return array[i] != -1;
    }

    public boolean atTopEdge(int i) {
        return i-imgWidth<0;
    }

    public boolean atRightEdge(int i) {
        return (2*(i+1)) % imgWidth == 0;
    }

    public boolean atBottomEdge(int i) {
        return i+imgWidth > imgWidth*imgHeight;
    }

    public boolean atLeftEdge(int i) {
        return i%imgWidth==0;
    }

    public void unionPixels(int a, int b) {
        if(find(array, a) < find(array, b))
            quickUnion(array, a, b);
        else quickUnion(array, b, a);
    }

    public void quickUnion(int[] a, int p, int q) {
        array[find(a, q)] = find(a, p);
    }

    public int find(int[] a, int id) {
        return a[id] == id ? id : (a[id] = find(a, a[id]));
    }
}

