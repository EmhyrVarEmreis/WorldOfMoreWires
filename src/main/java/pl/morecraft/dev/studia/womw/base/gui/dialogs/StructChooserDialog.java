package pl.morecraft.dev.studia.womw.base.gui.dialogs;

import pl.morecraft.dev.studia.womw.base.CellsMapV1;
import pl.morecraft.dev.studia.womw.base.gui.HandScrollListener;
import pl.morecraft.dev.studia.womw.base.gui.TextAreaSelectable;
import pl.morecraft.dev.studia.womw.core.CellState;
import pl.morecraft.dev.studia.womw.core.interfaces.CellsMapInterface;
import pl.morecraft.dev.studia.womw.io.IOFile;
import pl.morecraft.dev.studia.womw.misc.OtherStuff;
import pl.morecraft.dev.studia.womw.misc.Translator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public class StructChooserDialog extends JDialog implements ActionListener, MouseListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final JPanel prev;
    private final JPanel p1;
    private final JButton bA;
    private final JScrollPane prevSP, tasSP;
    private final HandScrollListener prevHSL;
    private final TextAreaSelectable tas;
    private CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> map;
    private BufferedImage image = null;
    private static int scale = 8;

    public StructChooserDialog(Frame parent) {
        super(parent);

        this.map = new CellsMapV1();

        this.setTitle(Translator.getString("STRUCT_CHOOSE"));
        this.setSize(400, 300);
        this.setMinimumSize(new Dimension(400, 300));
        this.setLayout(new BorderLayout());

        this.tas = new TextAreaSelectable();
        this.tas.setEditable(false);
        this.tas.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.tas.addMouseListener(this);
        this.prev = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                if (StructChooserDialog.this.image == null)
                    return;
                g2d.drawImage(StructChooserDialog.this.image, 0, 0, StructChooserDialog.this.image.getWidth() * StructChooserDialog.scale,
                        StructChooserDialog.this.image.getHeight() * StructChooserDialog.scale, 0, 0, StructChooserDialog.this.image.getWidth(),
                        StructChooserDialog.this.image.getHeight(), null);
            }
        };

        this.p1 = new JPanel();
        this.p1.setLayout(new BorderLayout());
        this.p1.setPreferredSize(new Dimension(100, 300));
        this.bA = new JButton(Translator.getString("ACCEPT"));
        this.bA.addActionListener(this);
        this.bA.setPreferredSize(new Dimension(100, 30));

        this.prevSP = new JScrollPane(this.prev);
        this.prevHSL = new HandScrollListener(this.prev, this.prevSP);
        this.prevSP.getViewport().addMouseListener(this.prevHSL);
        this.prevSP.getViewport().addMouseMotionListener(this.prevHSL);

        this.p1.add(this.prevSP, BorderLayout.CENTER);
        this.p1.add(this.bA, BorderLayout.PAGE_END);

        this.tasSP = new JScrollPane(this.tas);

        // this.prevSP.setPreferredSize( new Dimension( 100, 300 ) );

        this.add(this.tasSP, BorderLayout.CENTER);
        this.add(this.p1, BorderLayout.LINE_END);

        URL url = Thread.currentThread().getContextClassLoader().getResource("structs");
        if (url == null) {
            JOptionPane.showMessageDialog(
                    null,
                    Translator.getString("STRUCT_ERROR_LOADING_RESOURCES"),
                    Translator.getString("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
        } else {
            File dir;
            try {
                dir = new File(url.toURI());
                if (dir.isDirectory()) {
                    for (File nextFile : dir.listFiles()) {
                        this.tas.append(nextFile.getName().substring(0, nextFile.getName().lastIndexOf('.')) + "\n");
                    }
                }
            } catch (URISyntaxException e) {
                // e.printStackTrace();
            }
        }

    }

    public CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> getMapWithStruct() {
        return this.map;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == this.bA) {
            this.dispose();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if (o == this.tas && this.tas.isTextChanged()) {
            try {
                IOFile.readWoMWScriptFile(":::structs/" + this.tas.getSelectedText() + ".womws", this.map);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(
                        this,
                        Translator.getString("STRUCT_ERROR_LOADING_STRUCT"),
                        Translator.getString("WARNING"),
                        JOptionPane.WARNING_MESSAGE);
                //System.exit( 1 );
            }
            this.image = new BufferedImage(StructChooserDialog.this.map.getSize().width, StructChooserDialog.this.map.getSize().height,
                    BufferedImage.TYPE_INT_ARGB);
            OtherStuff.drawMapOnBufferedImageFromIterator(this.image, this.map.iterator(), 0, 0);
            this.prev.repaint();
            this.prevSP.revalidate();
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
