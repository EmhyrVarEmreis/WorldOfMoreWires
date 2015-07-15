package pl.morecraft.dev.studia.womw.misc;

import pl.morecraft.dev.studia.womw.core.CellState;
import pl.morecraft.dev.studia.womw.core.interfaces.CellsMapInterface;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class OtherStuff {

    public static void drawMapOnBufferedImageFromIterator(BufferedImage img, Iterator<Entry<Point, CellState>> iterator, int translateX,
                                                          int translateY) {
        int color;
        Map.Entry<Point, CellState> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            switch (entry.getValue()) {
                case CONDUCTOR:
                    color = Configuration.CONDUCTOR_COLOR.getRGB();
                    break;
                case EMPTY:
                    color = Configuration.NONE_COLOR.getRGB();
                    break;
                case HEAD:
                    color = Configuration.HEAD_COLOR.getRGB();
                    break;
                case TAIL:
                    color = Configuration.TAIL_COLOR.getRGB();
                    break;
                default:
                    color = Configuration.CONDUCTOR_COLOR.getRGB();
                    break;
            }
            img.setRGB(entry.getKey().x + translateX, entry.getKey().y + translateY, color);
        }
    }

    public static void rotateMap(CellsMapInterface<CellState, Point, Entry<Point, CellState>> map) {
        int x;
        Map.Entry<Point, CellState> entry;
        for (Entry<Point, CellState> aMap : map) {
            entry = aMap;
            x = entry.getKey().x;
            entry.getKey().x = map.getSize().height - entry.getKey().y - 1;
            entry.getKey().y = x;
        }
        map.rebuild();
    }

}
