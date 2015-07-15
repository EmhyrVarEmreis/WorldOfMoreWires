package pl.morecraft.dev.studia.womw.base.gui.dialogs;

import com.gurge.amd.Quantize;
import pl.morecraft.dev.studia.womw.base.CellsMapV1;
import pl.morecraft.dev.studia.womw.base.gui.HandScrollListener;
import pl.morecraft.dev.studia.womw.core.CellState;
import pl.morecraft.dev.studia.womw.core.interfaces.CellsMapInterface;
import pl.morecraft.dev.studia.womw.io.IOFile;
import pl.morecraft.dev.studia.womw.misc.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ImageLoadingDialog extends JDialog implements ActionListener, MouseListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Dimension C_SIZE = new Dimension(110, 30);

    private final JPanel[] col;

    private final BufferedImage image;

    private BufferedImage image1;
    private final JPanel imgP, img2P, imgPP, ctrlP, colP, downP;
    private final JButton quantB, readB, refB;
    private final JLabel tip;
    private final JScrollPane sc1, sc2;

    private Color[] colors;

    private final HandScrollListener scl1, scl2;
    private final int scale = 1;

    private CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> map;

    private final CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> mapTo;

    public ImageLoadingDialog(Frame parent, BufferedImage imagePre, CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> mapTo) {
        super(parent);

        if (imagePre == null) {
            JOptionPane.showMessageDialog(parent, "Wystąpił błąd podczas wczytywania obrazka!", "Błąd", JOptionPane.ERROR_MESSAGE);
            this.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        } else {
            if (imagePre.getColorModel().getPixelSize() != 24)
                JOptionPane.showMessageDialog(parent, "Obrazek może ni był popranie obsługiwany! Posiada " + imagePre.getColorModel().getPixelSize()
                        + "-bitową paletę kolorów. Do poprawnego działania wymagana jest paleta 24-bitowa.", "Uwaga", JOptionPane.WARNING_MESSAGE);
            this.image = new BufferedImage(imagePre.getWidth(), imagePre.getHeight(), imagePre.getType());
        }

        assert imagePre != null;
        this.image.setData(imagePre.getData());
        this.image1 = imagePre;
        this.mapTo = mapTo;

        this.setLayout(new GridLayout(1, 1));
        this.setTitle("Import z pliku graficznego");
        // this.setModal( true );

        this.setMinimumSize(new Dimension(500, 400));
        this.setLayout(new BorderLayout());

        this.imgP = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(ImageLoadingDialog.this.image1, 0, 0, null);
                this.setPreferredSize(new Dimension(ImageLoadingDialog.this.image1.getWidth(), ImageLoadingDialog.this.image1.getHeight()));
            }
        };

        this.img2P = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                if (ImageLoadingDialog.this.map != null) {
                    int i;
                    Map.Entry<Point, CellState> entry;
                    Iterator<Entry<Point, CellState>> it = ImageLoadingDialog.this.map.iterator();
                    while (it.hasNext()) {
                        entry = it.next();
                        switch (entry.getValue()) {
                            case CONDUCTOR:
                                g2d.setColor(Configuration.CONDUCTOR_COLOR);
                                break;
                            case EMPTY:
                                g2d.setColor(Configuration.NONE_COLOR);
                                break;
                            case HEAD:
                                g2d.setColor(Configuration.HEAD_COLOR);
                                break;
                            case TAIL:
                                g2d.setColor(Configuration.TAIL_COLOR);
                                break;
                            default:
                                g2d.setColor(Configuration.CONDUCTOR_COLOR);
                                break;
                        }
                        for (i = 0; i < ImageLoadingDialog.this.scale; i++)
                            g2d.drawLine((entry.getKey().x * ImageLoadingDialog.this.scale) + i, entry.getKey().y * ImageLoadingDialog.this.scale,
                                    (entry.getKey().x * ImageLoadingDialog.this.scale) + i,
                                    ((entry.getKey().y * ImageLoadingDialog.this.scale) + ImageLoadingDialog.this.scale) - 1);
                    }
                }
            }
        };

        this.sc1 = new JScrollPane(this.imgP);
        this.sc2 = new JScrollPane(this.img2P);
        this.scl1 = new HandScrollListener(this.imgP, this.sc1);
        this.scl2 = new HandScrollListener(this.img2P, this.sc2);
        this.sc1.getViewport().addMouseListener(this.scl1);
        this.sc1.getViewport().addMouseMotionListener(this.scl1);
        this.sc2.getViewport().addMouseListener(this.scl2);
        this.sc2.getViewport().addMouseMotionListener(this.scl2);

        this.imgPP = new JPanel();
        this.imgPP.setLayout(new GridLayout(1, 2));
        this.imgPP.add(this.sc1);
        this.imgPP.add(this.sc2);

        this.downP = new JPanel();
        this.downP.setLayout(new BorderLayout());
        this.downP.setPreferredSize(new Dimension(200, 90));

        this.ctrlP = new JPanel();
        this.ctrlP.setPreferredSize(new Dimension(200, 40));
        this.ctrlP.setLayout(new GridLayout(1, 3));

        this.quantB = new JButton("<html>Kwantyfikuj<br>do 4 kolorów</html>");
        this.quantB.addActionListener(this);
        this.refB = new JButton("Odśwież");
        this.refB.addActionListener(this);
        this.refB.setEnabled(false);
        this.readB = new JButton("AKCEPTUJ");
        this.readB.addActionListener(this);
        this.readB.setEnabled(false);

        // this.ctrlP.add( this.spinner );
        this.ctrlP.add(this.quantB);
        this.ctrlP.add(this.refB);
        this.ctrlP.add(this.readB);

        this.colP = new JPanel();
        this.colP.setLayout(new FlowLayout());

        this.col = new JPanel[4];

        String[] cl = new String[]{"PRZEWODNIK", "GŁOWA", "OGON", "TŁO"};

        for (int i = 0; i < this.col.length; i++) {
            this.col[i] = new JPanel();
            this.col[i].setLayout(null);
            this.col[i].setBackground(Color.GRAY);
            this.col[i].setPreferredSize(ImageLoadingDialog.C_SIZE);
            JLabel l = new JLabel(cl[i], JLabel.CENTER);
            l.setFont(new Font("Arial", Font.BOLD, 13));
            l.setBackground(Color.GRAY);
            l.setOpaque(true);
            l.setBounds(0, (ImageLoadingDialog.C_SIZE.height - 16) / 2, ImageLoadingDialog.C_SIZE.width, 16);
            this.col[i].add(l);
            this.col[i].addMouseListener(this);
            this.colP.add(this.col[i]);
        }

        this.tip = new JLabel("Wybierz odpowiednie kolory po przez klikanie myszką", JLabel.CENTER);

        this.downP.add(this.ctrlP, BorderLayout.PAGE_START);
        this.downP.add(this.colP, BorderLayout.CENTER);
        this.downP.add(this.tip, BorderLayout.PAGE_END);

        this.add(this.imgPP, BorderLayout.CENTER);
        this.add(this.downP, BorderLayout.PAGE_END);

        this.revalidate();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == this.quantB) {
            try {
                this.quant(4);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(this, "Wystąpił błąd krytyczny podczas kwantyfikacji kolorów.", "Błąd krytyczny",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            this.sc1.repaint();
        } else if (o == this.refB) {
            this.map = new CellsMapV1();
            IOFile.readImageFile(this.image1, this.map, this.col[0].getBackground().getRGB(), this.col[1].getBackground().getRGB(), this.col[2]
                    .getBackground().getRGB(), this.col[3].getBackground().getRGB());
            this.img2P.repaint();
            this.sc2.revalidate();
            this.readB.setEnabled(true);
        } else if (o == this.readB) {
            this.mapTo.insertMap(this.map, 0, 0);
            this.map.clear();
            this.map = null;
            this.dispose();
        }

    }

    private void quant(int coloursCount) throws IOException {
        int pixels[][] = getPixels(this.image);
        int palette[] = Quantize.quantizeImage(pixels, coloursCount);
        this.findColors(palette);
        this.setImageQ(palette, pixels);
    }

    private static int[][] getPixels(Image image) throws IOException {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        int pix[] = new int[w * h];
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, w, h, pix, 0, w);

        try {
            if (!grabber.grabPixels()) {
                throw new IOException("Grabber returned false: " + grabber.status());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int pixels[][] = new int[w][h];
        for (int x = w; x-- > 0; ) {
            for (int y = h; y-- > 0; ) {
                pixels[x][y] = pix[y * w + x];
            }
        }

        return pixels;
    }

    private void setImageQ(int palette[], int pixels[][]) {
        int w = pixels.length;
        int h = pixels[0].length;
        int pix[] = new int[w * h];

        // convert to RGB
        for (int x = w; x-- > 0; ) {
            for (int y = h; y-- > 0; ) {
                pix[y * w + x] = palette[pixels[x][y]];
            }
        }

        this.setImageQ(w, h, pix);
    }

    private void setImageQ(int w, int h, int pix[]) {
        this.image1 = toBufferedImage(this.createImage(new MemoryImageSource(w, h, pix, 0, w)));
    }

    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    private void findColors(int[] palette) {
        this.colors = new Color[palette.length];
        for (int i = 0; i < palette.length; i++)
            this.colors[i] = new Color(palette[i]);

    }

    private Color getNonUsedColour() {
        for (Color c : this.colors) {
            if (!this.checkIfIsUsedColour(c))
                return c;
        }
        return null;
    }

    private boolean checkIfIsUsedColour(Color c) {
        for (JPanel pp : this.col) {
            if (pp.getBackground().equals(c))
                return true;
        }
        return false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.colors == null)
            return;
        JPanel p = (JPanel) e.getSource();
        p.setBackground(this.getNonUsedColour());
        if (this.getNonUsedColour() == null) {
            this.refB.setEnabled(true);
        } else {
            this.refB.setEnabled(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

}
