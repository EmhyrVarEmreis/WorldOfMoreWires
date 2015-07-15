package pl.morecraft.dev.studia.womw.base.gui;

import pl.morecraft.dev.studia.womw.base.EdtModes;
import pl.morecraft.dev.studia.womw.base.WorkerV1;
import pl.morecraft.dev.studia.womw.base.gui.dialogs.*;
import pl.morecraft.dev.studia.womw.io.IOFile;
import pl.morecraft.dev.studia.womw.misc.Configuration;
import pl.morecraft.dev.studia.womw.misc.CoreBasicMain;
import pl.morecraft.dev.studia.womw.misc.LoadFromRes;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static pl.morecraft.dev.studia.womw.misc.LoadFromRes.loadImageAsIconImage;

public class MainPanel extends JPanel implements ActionListener {

    private static int MIN_W = 800, MIN_H = 600;

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final JButton bEditDelete, bEditConductor, bEditHead, bEditTail, bStruct, bNothing;
    private final JButton bToolStart, bToolStep/* , bToolPause */;

    private final JSlider js1, js2;

    private String lastOpenedFile;

    private final JMenuItem mEditClear;
    private final JMenuItem mEditSettings;
    private final JMenuBar menuBar;
    private final JMenu mFile, mHelp, mEdit;
    private final JMenu mFileExport;
    private final JMenuItem mFileExportImage;
    private final JMenu mFileOpen;
    private final JMenuItem mFileOpenCS;
    private final JMenuItem mFileOpenImage;
    private final JMenuItem mFileSave;
    private final JMenu mFileSaveAs;
    private final JMenuItem mFileSaveAsCore;
    private final JMenuItem mFileSaveAsScript;
    private final JMenuItem mHelpInfo;
    private final JMenuItem mHelpManual;
    private final JPanel pView, pToolBox, pInfo, pDown;

    private final HandScrollListener scrollListener;

    private final JScrollPane scrollPanel;
    private final StatusBar statusBar;

    private final ViewPanel viewPanel;

    private final WorkerV1 worker;

    public MainPanel() {

		/*
         * UWAGA!
		 */

        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(MIN_W, MIN_H));
        // this.setPreferredSize( new Dimension( MIN_W, MIN_H ) );

		/*
         * Panele
		 */

        this.pView = new JPanel();
        this.pView.setLayout(new GridLayout(1, 1));
        this.pView.setBorder(BorderFactory.createTitledBorder(null, "WIDOK", TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma", Font.BOLD,
                11)));
        this.pView.setPreferredSize(new Dimension(200, 160));

        this.pToolBox = new JPanel();
        this.pToolBox.setLayout(new GridLayout(6, 1));
        this.pToolBox.setBorder(BorderFactory.createTitledBorder(null, "RYSOWNIK", TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
                Font.BOLD, 11)));
        this.pToolBox.setPreferredSize(new Dimension(80, 160));

        this.pDown = new JPanel();
        this.pDown.setLayout(new BorderLayout());

        this.statusBar = new StatusBar();

        this.pInfo = new JPanel();
        this.pInfo.setLayout(new FlowLayout());
        this.pInfo.setBorder(BorderFactory.createTitledBorder(null, "KONTROLER", TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
                Font.BOLD, 11)));
        this.pInfo.setPreferredSize(new Dimension(0, 60));
        this.pInfo.setMinimumSize(new Dimension(200, 0));

        this.pDown.add(this.pInfo, BorderLayout.PAGE_START);
        this.pDown.add(this.statusBar, BorderLayout.PAGE_END);

        this.add(this.pView, BorderLayout.CENTER);
        this.add(this.pToolBox, BorderLayout.LINE_END);
        this.add(this.pDown, BorderLayout.PAGE_END);

		/*
		 * Przyciski Rysownika
		 */

        this.bEditDelete = new JButton(LoadFromRes.loadImageAsIconImage("icons/draw_delete.png"));
        this.bEditDelete.addActionListener(this);
        this.bEditDelete.setToolTipText("Kasuj");

        this.bEditConductor = new JButton(LoadFromRes.loadImageAsIconImage("icons/draw_conductor.png"));
        this.bEditConductor.addActionListener(this);
        this.bEditConductor.setToolTipText("Rysuj przewodnik");

        this.bEditHead = new JButton(LoadFromRes.loadImageAsIconImage("icons/draw_head.png"));
        this.bEditHead.addActionListener(this);
        this.bEditHead.setToolTipText("Rysuj głowę elektronu");

        this.bEditTail = new JButton(LoadFromRes.loadImageAsIconImage("icons/draw_tail.png"));
        this.bEditTail.addActionListener(this);
        this.bEditTail.setToolTipText("Rysuj ogon elektronu");

        this.bStruct = new JButton(LoadFromRes.loadImageAsIconImage("icons/draw_struct.png"));
        this.bStruct.addActionListener(this);
        this.bStruct.setToolTipText("Rysuj strukturę");

        this.bNothing = new JButton(LoadFromRes.loadImageAsIconImage("icons/draw_none.png"));
        this.bNothing.addActionListener(this);
        this.bNothing.setToolTipText("Wyłącz narzędzie");

        this.pToolBox.add(this.bEditConductor);
        this.pToolBox.add(this.bEditHead);
        this.pToolBox.add(this.bEditTail);
        this.pToolBox.add(this.bStruct);
        this.pToolBox.add(this.bEditDelete);
        this.pToolBox.add(this.bNothing);

		/*
		 * Przyciski Toolboxa
		 */

        this.js1 = new JSlider(1, 1000, Configuration.DELAY_BETWEEN_CYCLES);
        this.js1.setPreferredSize(new Dimension(192, 32));
        this.js1.addChangeListener(e -> MainPanel.this.setDBC(MainPanel.this.js1.getValue()));
        this.js1.setToolTipText("Opóźnienie pomiędzy rundami");

        this.js2 = new JSlider(1, 100, Configuration.CYCLES_PER_ROUND);
        this.js2.setPreferredSize(new Dimension(192, 32));
        this.js2.addChangeListener(e -> MainPanel.this.setCPR(MainPanel.this.js2.getValue()));
        this.js2.setToolTipText("Cykli na rundę");

        this.bToolStart = new JButton(LoadFromRes.loadImageAsIconImage("icons/tool_start.png"));
        this.bToolStart.addActionListener(this);
        this.bToolStart.setPreferredSize(new Dimension(32, 32));
        this.bToolStart.setToolTipText("Start");

        this.bToolStep = new JButton(LoadFromRes.loadImageAsIconImage("icons/tool_step.png"));
        this.bToolStep.addActionListener(this);
        this.bToolStep.setPreferredSize(new Dimension(32, 32));
        this.bToolStep.setToolTipText("Krok naprzód");

        this.pInfo.add(new JLabel("  DBC:"));
        this.pInfo.add(this.js1);
        this.pInfo.add(this.bToolStart);
        this.pInfo.add(this.bToolStep);
        this.pInfo.add(new JLabel("  CPR:"));
        this.pInfo.add(this.js2);

		/*
		 * No i wreszcie król
		 */

        this.worker = new WorkerV1();
        this.viewPanel = new ViewPanel(this.worker.getCellsMap());
        this.worker.setViewer(this.viewPanel);

        this.viewPanel.setStatusBar(this.statusBar);

        this.worker.setCyclesPerViewerUpdate(Configuration.CYCLES_PER_ROUND);
        this.worker.setDelayBetweenCycles(Configuration.DELAY_BETWEEN_CYCLES);
        this.worker.start();

        this.scrollPanel = new JScrollPane(this.viewPanel);

        this.scrollListener = new HandScrollListener(this.viewPanel, this.scrollPanel);
        this.scrollPanel.getViewport().addMouseListener(this.scrollListener);
        this.scrollPanel.getViewport().addMouseMotionListener(this.scrollListener);

        this.pView.add(this.scrollPanel);

		/*
		 * Menu
		 */

        this.menuBar = new JMenuBar();

        this.mFile = new JMenu("Plik");
        this.mFile.setIcon(loadImageAsIconImage("icons/file.png"));
        this.mEdit = new JMenu("Edytuj");
        this.mEdit.setIcon(loadImageAsIconImage("icons/edit.png"));
        this.mHelp = new JMenu("Pomoc");
        this.mHelp.setIcon(loadImageAsIconImage("icons/help.png"));

        this.mFileOpen = new JMenu("Otwórz/Importuj...");
        this.mFileOpen.setIcon(loadImageAsIconImage("icons/open.png"));
        this.mFileOpenCS = new JMenuItem("Otwórz plik .womw/.womws ...");
        this.mFileOpenCS.addActionListener(this);
        this.mFileOpenCS.setIcon(loadImageAsIconImage("icons/file_core.png"));
        this.mFileOpenCS.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        this.mFileOpenImage = new JMenuItem("Importuj plik obrazu...");
        this.mFileOpenImage.addActionListener(this);
        this.mFileOpenImage.setIcon(loadImageAsIconImage("icons/image.png"));
        this.mFileOpenImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
        this.mFileOpen.add(this.mFileOpenCS);
        this.mFileOpen.add(this.mFileOpenImage);

        this.mFileExport = new JMenu("Eksportuj...");
        this.mFileExport.setIcon(loadImageAsIconImage("icons/export.png"));

        this.mFileExportImage = new JMenuItem("Obraz...");
        this.mFileExportImage.addActionListener(this);
        this.mFileExportImage.setIcon(loadImageAsIconImage("icons/image.png"));
        this.mFileExportImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));

        this.mFileExport.add(this.mFileExportImage);

        this.mFileSave = new JMenuItem("Zapisz");
        this.mFileSave.addActionListener(this);
        this.mFileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        this.mFileSave.setIcon(loadImageAsIconImage("icons/save.png"));
        this.mFileSaveAs = new JMenu("Zapisz jako...");
        this.mFileSaveAs.setIcon(loadImageAsIconImage("icons/save.png"));
        this.mFileSaveAsCore = new JMenuItem("Plik .womw ...");
        this.mFileSaveAsCore.addActionListener(this);
        this.mFileSaveAsCore.setIcon(loadImageAsIconImage("icons/file_core.png"));
        this.mFileSaveAsCore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
        this.mFileSaveAsScript = new JMenuItem("Plik .womws ...");
        this.mFileSaveAsScript.addActionListener(this);
        this.mFileSaveAsScript.setIcon(loadImageAsIconImage("icons/script.png"));
        this.mFileSaveAsScript.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK));
        this.mFileSaveAs.add(this.mFileSaveAsCore);
        this.mFileSaveAs.add(this.mFileSaveAsScript);

        this.mFile.add(this.mFileOpen);
        this.mFile.addSeparator();
        this.mFile.add(this.mFileSave);
        this.mFile.add(this.mFileSaveAs);
        this.mFile.addSeparator();
        this.mFile.add(this.mFileExport);

        this.mEditSettings = new JMenuItem("Ustawienia");
        this.mEditSettings.addActionListener(this);
        this.mEditSettings.setIcon(loadImageAsIconImage("icons/tune.png"));
        this.mEditSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK));
        this.mEditClear = new JMenuItem("Czyść mapę");
        this.mEditClear.addActionListener(this);
        this.mEditClear.setIcon(loadImageAsIconImage("icons/erase.png"));
        this.mEditClear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK));

        this.mEdit.add(this.mEditClear);
        this.mEdit.addSeparator();
        this.mEdit.add(this.mEditSettings);

        this.mHelpManual = new JMenuItem("Instrukcja...");
        this.mHelpManual.addActionListener(this);
        this.mHelpManual.setIcon(loadImageAsIconImage("icons/help_book.png"));
        this.mHelpManual.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK));
        this.mHelpInfo = new JMenuItem("Informacje o programie...");
        this.mHelpInfo.addActionListener(this);
        this.mHelpInfo.setIcon(loadImageAsIconImage("icons/info.png"));
        this.mHelpInfo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK));

        this.mHelp.add(this.mHelpManual);
        this.mHelp.addSeparator();
        this.mHelp.add(this.mHelpInfo);

        this.menuBar.add(this.mFile);
        this.menuBar.add(this.mEdit);
        this.menuBar.add(this.mHelp);

        this.add(this.menuBar, BorderLayout.NORTH);

		/*
		 * A takie tam ten tego
		 */

        this.setSize(MIN_W, MIN_H);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object o = e.getSource();

        if (o == this.bEditDelete)
            this.changeEditingMode(EdtModes.DELETE);
        else if (o == this.bEditConductor)
            this.changeEditingMode(EdtModes.DRAW_CONDUCTOR);
        else if (o == this.bEditHead)
            this.changeEditingMode(EdtModes.DRAW_HEAD);
        else if (o == this.bStruct)
            this.showDialogStructChooser();
        else if (o == this.bEditTail)
            this.changeEditingMode(EdtModes.DRAW_TAIL);
        else if (o == this.bNothing)
            this.changeEditingMode(EdtModes.NOTHING);
        else if (o == this.bToolStep) {
            if (this.viewPanel.isInEdtMode())
                this.optimizeViewIfIsInEditingMode();
            this.viewPanel.setEdtMode(EdtModes.NOTHING);
            this.worker.makeCyclesAndStop(1);
        } else if (o == this.bToolStart) {
            if (this.viewPanel.isInEdtMode())
                this.optimizeViewIfIsInEditingMode();
            if (this.worker.isProcessing())
                this.stopProcess();
            else
                this.startProcess();
        } else if (o == this.mFileOpenCS)
            this.openOpenDialog();
        else if (o == this.mFileOpenImage)
            this.openOpenImageDialog(true);
        else if (o == this.mFileExportImage)
            this.showDialogSaveImage();
        else if (o == this.mFileSaveAsScript)
            this.showDialogSaveWoMW("womws");
        else if (o == this.mFileSaveAsCore)
            this.showDialogSaveWoMW("womw");
        else if (o == this.mHelpInfo)
            this.showDialogInfo();
        else if (o == this.mHelpManual)
            this.showDialogManual();
        else if (o == this.mEditSettings)
            this.showDialogSettings();
        else if (o == this.mEditClear)
            this.clearMainWorkerMap();
        else if (o == this.mFileSave) {
            File f = new File(this.lastOpenedFile);
            if (f.exists()) {
                final String extension = this.lastOpenedFile.substring(this.lastOpenedFile.lastIndexOf('.'), this.lastOpenedFile.length());
                new ProgressWaitingFrame(CoreBasicMain.getParentFrameofComponent(this), "Wczytywanie...") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void toDo() throws Exception {
                        IOFile.setProgressableDialog(this);
                        MainPanel.this.viewPanel.updateAll();
                        if (extension.toLowerCase().equals(".womw"))
                            IOFile.saveWoMWFile(MainPanel.this.lastOpenedFile, MainPanel.this.worker.getCellsMap());
                        else if (extension.toLowerCase().equals(".womws"))
                            IOFile.saveWoMWScriptFile(MainPanel.this.lastOpenedFile, MainPanel.this.worker.getCellsMap());
                        else
                            this.dispose();
                    }
                };
            }
        }
    }

    private void changeEditingMode(EdtModes m) {
        this.viewPanel.setEdtMode(m);
        this.stopProcess();
    }

    private void clearMainWorkerMap() {
        this.worker.getCellsMap().clear();
        this.viewPanel.rebuildView();
        this.viewPanel.refreshView();
    }

    private void openOpenDialog() {
        final JFileChooser fc = new JFileChooser();

        fc.setAcceptAllFileFilterUsed(false);

        fc.addChoosableFileFilter(new FileNameExtensionFilter("WoMW FIles (*.womw/*.womws)", "womw", "womws"));

        switch (fc.showOpenDialog(this)) {
            case JFileChooser.APPROVE_OPTION:
                new ProgressWaitingFrame(CoreBasicMain.getParentFrameofComponent(this), "Wczytywanie...") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void toDo() throws Exception {
                        IOFile.setProgressableDialog(this);
                        if (fc.getSelectedFile().getPath().toLowerCase().endsWith(".womw")) {
                            try {
                                IOFile.readWoMWFile(fc.getSelectedFile().getPath(), MainPanel.this.worker.getCellsMap());
                            } catch (ClassNotFoundException | IOException e) {
                                JOptionPane.showMessageDialog(MainPanel.this.getParent(),
                                        "Wczytywanie mapy nie powiodło się! Plik jest uszkodzony!", "Błąd", JOptionPane.ERROR_MESSAGE);
                                this.close();
                                return;
                            }
                        } else
                            IOFile.readWoMWScriptFile(fc.getSelectedFile().getPath(), MainPanel.this.worker.getCellsMap());
                        MainPanel.this.lastOpenedFile = fc.getSelectedFile().getPath();
                        MainPanel.this.viewPanel.refreshView();
                    }
                };
                break;
            default:
                break;
        }
    }

    private void openOpenImageDialog(boolean refreshViewpanel) {
        final JFileChooser fc = new JFileChooser();

        fc.setAcceptAllFileFilterUsed(false);

        fc.addChoosableFileFilter(new FileNameExtensionFilter("All supported Image types", "jpg", "png", "bmp"));

        switch (fc.showOpenDialog(this)) {
            case JFileChooser.APPROVE_OPTION:
                new ProgressWaitingFrame(CoreBasicMain.getParentFrameofComponent(this), "Wczytywanie...") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void toDo() throws Exception {
                        IOFile.setProgressableDialog(this);
                        MainPanel.this.showDialogProcessImage(ImageIO.read(fc.getSelectedFile()));
                        this.setMaximum(1);
                        this.setValue(this.getMaximum());
                    }
                };
                break;
            default:
                return;
        }

    }

    private void optimizeViewIfIsInEditingMode() {
        new ProgressWaitingFrame(CoreBasicMain.getParentFrameofComponent(this), "Optymalizacja...") {
            private static final long serialVersionUID = 1L;

            @Override
            public void toDo() throws Exception {
                this.setValue(0);
                this.setMaximum(1);
                MainPanel.this.viewPanel.updateAll();
                MainPanel.this.viewPanel.refreshView();
                this.setValue(1);
            }
        };
    }

    private void setCPR(int i) {
        this.worker.setCyclesPerViewerUpdate(i);
        Configuration.CYCLES_PER_ROUND = i;
        this.statusBar.showFormConf();
    }

    private void setDBC(int i) {
        this.worker.setDelayBetweenCycles(i);
        Configuration.DELAY_BETWEEN_CYCLES = i;
        this.statusBar.showFormConf();
    }

    private void showDialogInfo() {
        this.showDialog(new AboutDialog(CoreBasicMain.getParentFrameofComponent(this)));
    }

    private void showDialogManual() {
        this.showDialog(new ManualDialog(CoreBasicMain.getParentFrameofComponent(this)));
    }

    private void showDialogProcessImage(BufferedImage img) {
        this.showDialog(new ImageLoadingDialog(CoreBasicMain.getParentFrameofComponent(this), img, this.worker.getCellsMap()));
        this.viewPanel.refreshView();
    }

    private void showDialog(JDialog d) {
        d.setLocation((d.getParent().getLocationOnScreen().x + (d.getParent().getWidth() / 2)) - (d.getWidth() / 2),
                (d.getParent().getLocationOnScreen().y + (d.getParent().getHeight() / 2)) - (d.getHeight() / 2));
        d.setModal(true);
        d.setVisible(true);
    }

    private void showDialogSaveImage() {
        final JFileChooser fc = new JFileChooser();

        fc.setAcceptAllFileFilterUsed(false);

        fc.addChoosableFileFilter(new FileNameExtensionFilter("JPEG Image File", "jpg"));
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Windows Bitmap Image File", "bmp"));
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Portable Network Graphics Image File", "png"));
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Portable Network Graphics Image File with Transparency", "png"));

        switch (fc.showSaveDialog(this)) {
            case JFileChooser.APPROVE_OPTION:
                new ProgressWaitingFrame(CoreBasicMain.getParentFrameofComponent(this), "Zapisywanie...") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void toDo() throws Exception {
                        IOFile.setProgressableDialog(this);
                        MainPanel.this.viewPanel.updateAll();
                        IOFile.saveWoMWImageFile(fc.getSelectedFile().getPath(), MainPanel.this.worker.getCellsMap(), fc.getFileFilter(),
                                MainPanel.this.viewPanel.getScale());
                    }
                };
                break;
            default:
                // log.append("Open command cancelled by user." + newline);
                break;
        }
    }

    private void showDialogSaveWoMW(final String ext) {
        final JFileChooser fc = new JFileChooser();

        fc.setAcceptAllFileFilterUsed(false);

        if (ext.equalsIgnoreCase("womw"))
            fc.addChoosableFileFilter(new FileNameExtensionFilter("World of More Wires Core File", "womw"));
        else
            fc.addChoosableFileFilter(new FileNameExtensionFilter("World of More Wires Script File", "womws"));

        switch (fc.showSaveDialog(this)) {
            case JFileChooser.APPROVE_OPTION:
                new ProgressWaitingFrame(CoreBasicMain.getParentFrameofComponent(this), "Zapisywanie...") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void toDo() throws Exception {
                        IOFile.setProgressableDialog(this);
                        MainPanel.this.viewPanel.updateAll();
                        if (ext.equalsIgnoreCase("womw"))
                            IOFile.saveWoMWFile(fc.getSelectedFile().getPath(), MainPanel.this.worker.getCellsMap());
                        else
                            IOFile.saveWoMWScriptFile(fc.getSelectedFile().getPath(), MainPanel.this.worker.getCellsMap());
                    }
                };
                break;
            default:
                // log.append("Open command cancelled by user." + newline);
                break;
        }
    }

    private void showDialogSettings() {
        JFrame parent = CoreBasicMain.getParentFrameofComponent(this);
        ChangeSettingsDialog d = new ChangeSettingsDialog(parent);
        d.setLocation((parent.getLocationOnScreen().x + (parent.getWidth() / 2)) - (d.getWidth() / 2),
                (parent.getLocationOnScreen().y + (parent.getHeight() / 2)) - (d.getHeight() / 2));
        d.setModal(true);
        d.setVisible(true);
        this.viewPanel.refreshView();
    }

    private void showDialogStructChooser() {
        JFrame parent = CoreBasicMain.getParentFrameofComponent(this);
        StructChooserDialog d = new StructChooserDialog(parent);
        d.setLocation((parent.getLocationOnScreen().x + (parent.getWidth() / 2)) - (d.getWidth() / 2),
                (parent.getLocationOnScreen().y + (parent.getHeight() / 2)) - (d.getHeight() / 2));
        d.setModal(true);
        d.setVisible(true);
        this.viewPanel.setStructureToDraw(d.getMapWithStruct());
        // System.out.println(scd.getFileToRead());
    }

    private void startProcess() {
        this.viewPanel.setEdtMode(EdtModes.NOTHING);
        this.worker.startProcess();
        this.bToolStart.setIcon(LoadFromRes.loadImageAsIconImage("icons/tool_pause.png"));
        this.bToolStep.setEnabled(false);
    }

    private void stopProcess() {
        this.worker.stopProcess();
        this.bToolStart.setIcon(LoadFromRes.loadImageAsIconImage("icons/tool_start.png"));
        this.bToolStep.setEnabled(true);
    }

}
