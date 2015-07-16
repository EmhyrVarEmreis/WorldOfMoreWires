package pl.morecraft.dev.studia.womw.io;

import pl.morecraft.dev.studia.womw.base.gui.Progressable;
import pl.morecraft.dev.studia.womw.core.CellState;
import pl.morecraft.dev.studia.womw.core.interfaces.CellsMapInterface;
import pl.morecraft.dev.studia.womw.misc.Configuration;
import pl.morecraft.dev.tool.pointproccessor.Line;
import pl.morecraft.dev.tool.pointproccessor.LineFinder;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class IOFile {

    private static Progressable progressBar;

    private static void endProgress() {
        if (progressBar != null)
            progressBar.setValue(progressBar.getMaximum());
        progressBar = null;
    }

    public static void readImageFile(BufferedImage img, CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> map, int condutorColor,
                                     int headColor, int tailColor, int bgColor) {
        int color;
        Point position = new Point(0, 0);
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                color = img.getRGB(x, y);
                position.x = x;
                position.y = y;
                if (color == condutorColor) {
                    map.putCell(position, CellState.CONDUCTOR);
                } else if (color == headColor) {
                    map.putCell(position, CellState.HEAD);
                } else if (color == tailColor) {
                    map.putCell(position, CellState.TAIL);
                } //else if (color == bgColor) {
                // todo
                //}

            }
        }
    }

    public static void readWoMWFile(String path, CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> map) throws IOException,
            ClassNotFoundException {

        setProgressMax(2);
        setProgress(0);

        SerializedMap sm;
        FileInputStream fileIn = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        sm = (SerializedMap) in.readObject();
        in.close();
        fileIn.close();

        setProgress(1);

        for (int i = 0; i < sm.getCoordinates().length; i++) {
            map.putCell(sm.getCoordinates()[i], sm.getStates()[i]);
        }

        setProgress(2);

        endProgress();
    }

    public static void readWoMWScriptFile(String path, CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> map)
            throws IOException {

        setProgressMax(4);
        setProgress(0);
        BufferedReader br = new BufferedReader(
                (path.startsWith(":::")) ?
                        new InputStreamReader(
                                Thread.currentThread().getContextClassLoader().getResourceAsStream(path.substring(3))) : new FileReader(path));
        String l;// = br.readLine();
        String[] a;// = l.split(" ");
        Line line;

        map.clear();

        setProgress(1);

        while ((l = br.readLine()) != null) {
            l = l.toLowerCase();
            a = l.split(" ");
            if (l.startsWith("draw_conductor")) {
                if (a.length == 3)
                    map.setCell(new Point(Integer.valueOf(a[1]), Integer.valueOf(a[2])), CellState.CONDUCTOR);
                else if (a.length == 5) {
                    line = new Line(new Point(Integer.valueOf(a[1]), Integer.valueOf(a[2])), new Point(Integer.valueOf(a[3]),
                            Integer.valueOf(a[4])));
                    for (Point p : line.getPoints())
                        map.putCell(p, CellState.CONDUCTOR);
                } else
                    // TODO Exception
                    break;
            } else if (l.startsWith("draw_head")) {
                if (a.length == 3)
                    map.setCell(new Point(Integer.valueOf(a[1]), Integer.valueOf(a[2])), CellState.HEAD);
                else
                    // TODO Exception
                    break;
            } else if (l.startsWith("draw_tail"))
                if (a.length == 3)
                    map.setCell(new Point(Integer.valueOf(a[1]), Integer.valueOf(a[2])), CellState.TAIL);
                else
                    // TODO Exception
                    break;

        }

        setProgress(2);

        map.rebuild();

        endProgress();
    }

    public static void saveWoMWFile(String path, CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> map)
            throws IOException {

        setProgressMax(2);
        setProgress(0);

        SerializedMap sm = new SerializedMap();

        sm.setCoordinates(new Point[map.getNumberOfElements()]);
        sm.setStates(new CellState[map.getNumberOfElements()]);

        Map.Entry<Point, CellState> entry;
        Iterator<Entry<Point, CellState>> it = map.iterator();
        int i = 0;
        while (it.hasNext()) {
            entry = it.next();
            sm.getCoordinates()[i] = entry.getKey();
            sm.getStates()[i] = entry.getValue();
            i++;
        }

        if (!path.toLowerCase().endsWith(".womw"))
            path += ".womw";

        setProgress(1);

        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(sm);
        out.close();
        fileOut.close();

        setProgress(2);

        endProgress();
    }

    public static void saveWoMWImageFile(String path, CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> map, FileFilter ff)
            throws IOException {
        IOFile.saveWoMWImageFile(path, map, ff, 1);
    }

    public static void saveWoMWImageFile(String path, CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> map, FileFilter ff, int scale)
            throws IOException {

        setProgressMax(4);
        setProgress(0);

        if (map == null)
            // TODO Exception
            return;
        BufferedImage image = new BufferedImage(map.getSize().width * scale, map.getSize().height * scale,
                (ff.getDescription().toLowerCase().contains("transparency")) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        Graphics2D imageG = image.createGraphics();

        setProgress(1);

        Map.Entry<Point, CellState> entry;
        Iterator<Entry<Point, CellState>> it = map.iterator();

        if (!ff.getDescription().toLowerCase().contains("transparency")) {
            imageG.setColor(Configuration.NONE_COLOR);
            imageG.fillRect(0, 0, image.getWidth(), image.getHeight());
        }

        while (it.hasNext()) {

            entry = it.next();

            switch (entry.getValue()) {
                case CONDUCTOR:
                    imageG.setColor(Configuration.CONDUCTOR_COLOR);
                    break;
                case EMPTY:
                    imageG.setColor(Configuration.NONE_COLOR);
                    break;
                case HEAD:
                    imageG.setColor(Configuration.HEAD_COLOR);
                    break;
                case TAIL:
                    imageG.setColor(Configuration.TAIL_COLOR);
                    break;
                default:
                    imageG.setColor(Configuration.CONDUCTOR_COLOR);
                    break;
            }

            for (int i = 0; i < scale; i++)
                imageG.drawLine((entry.getKey().x * scale) + i, entry.getKey().y * scale, (entry.getKey().x * scale) + i,
                        ((entry.getKey().y * scale) + scale) - 1);

        }

        setProgress(2);

        String extension = "";

        if (ff.accept(new File("a.png"))) {
            extension = "png";
        } else if (ff.accept(new File("a.jpg"))) {
            extension = "jpg";
        } else if (ff.accept(new File("a.bmp"))) {
            extension = "bmp";
        }
        //else {
        // TODO Exception
        //}

        if (!path.toLowerCase().endsWith("." + extension)) {
            path = path + "." + extension;
        }

        File f = new File(path);

        setProgress(3);

        ImageIO.write(image, extension, f);

        endProgress();
    }

    public static void saveWoMWScriptFile(String path, CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> map)
            throws FileNotFoundException, UnsupportedEncodingException {
        setProgressMax(4);
        setProgress(0);

        ArrayList<Point> toDo = new ArrayList<>();

        Map.Entry<Point, CellState> entry;
        Iterator<Entry<Point, CellState>> it = map.iterator();

        while (it.hasNext()) {
            entry = it.next();
            toDo.add(entry.getKey());
        }

        setProgress(1);

        // TODO Looking for structures

		/*
         * Looking for lines
		 */

        LineFinder lf = new LineFinder(toDo);
        Line line;
        ArrayList<Line> al = new ArrayList<>();
        ArrayList<Point> pl;

        while ((line = lf.getNext()) != null) {
            al.add(line);
        }

        pl = lf.getStandalonePoints();

        setProgress(2);

		/*
         * Writting
		 */

        if (!path.toLowerCase().endsWith(".womws")) {
            path += ".womws";
        }

        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("-- --");

        for (Line l : al) {
            writer.println(
                    "draw_conductor " +
                            l.getStartingPoint().x +
                            " " + l.getStartingPoint().y +
                            " " + l.getEndingPoint().x +
                            " " + l.getEndingPoint().y);
        }
        for (Point p : pl) {
            writer.println("draw_conductor " + p.x + " " + p.y);
        }

        setProgress(3);

        it = map.iterator();

        while (it.hasNext()) {
            entry = it.next();
            if (entry.getValue() == CellState.HEAD) {
                writer.println("draw_head " + entry.getKey().x + " " + entry.getKey().y);
            } else if (entry.getValue() == CellState.TAIL) {
                writer.println("draw_tail " + entry.getKey().x + " " + entry.getKey().y);
            }
        }

        writer.close();

        endProgress();

    }

    private static void setProgress(int progress) {
        if (progressBar != null) {
            progressBar.setValue(progress);
        }
    }

    public static void setProgressableDialog(Progressable pb) {
        progressBar = pb;
    }

    private static void setProgressMax(int progress) {
        if (progressBar != null) {
            progressBar.setMaximum(progress);
        }
    }

}
