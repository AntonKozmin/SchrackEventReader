package EvntFileReader;

import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLStreamException;

public class EvntFileReader extends JPanel implements ActionListener {

    static Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
    static Dimension framesize;
    static private final String NEWLINE = "\n";
    JButton evntButton; // A file saved by ServiceAssistant
    JButton itxButton; // itxButton -> the .xml text exported by IDC
    JButton setButton; // A Text window output setting
    List<JButton> buttons;
    JCheckBox localStationBox, comAllBox, devAddressBox, subTypeBox, stateChangeBox, StateSubTypeBox;
    List<JCheckBox> checkBoxes;
    JFileChooser fcEVNT_IDC; // An evnt file choose
    JFileChooser fcXML_IDC; // An exported text file choose
    JTextArea log, dbg;// The main output window and debug window	
    StAXforXML_ITX xmlTxt; // The STaX builder for customer texts
    boolean ITXHasText = false; // Is an ITX file exist?
    boolean isShowingSetWindow = false; // Do open Setting window at this time
    int totalOfTexts; // A customer texts total lines

//An output setting window objects 
    JFrame setWindow;
    Boolean[] outputSet = {true, //localStationSet
        false, //comAllSet
        false, //devAddressSet
        true, //subTypeSet
        true,//stateChangeSet
        true//StateSubTypeSet
    };

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Программа обработки файлов протокола Schrack.");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new EvntFileReader());
        frame.pack();
        frame.setLocation((screensize.width - frame.getWidth()) / 2, (screensize.height - frame.getHeight()) / 2);
        framesize = new Dimension(frame.getWidth(), frame.getHeight() - 120);
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                // 0 => "javax.swing.plaf.metal.MetalLookAndFeel"
                // 3 => the Windows Look and Feel
                String GUImng = UIManager.getInstalledLookAndFeels()[0].getClassName();
                UIManager.setLookAndFeel(GUImng);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException
                    | UnsupportedLookAndFeelException ex) {
            }
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            createAndShowGUI();
        });
    }

    EvntFileReader() {

        super(new BorderLayout());

//Main output and debug windows create
        log = new JTextArea(30, 80);
        log.setMargin(new Insets(3, 3, 3, 3));
        log.setEditable(false);
        log.setSelectedTextColor(Color.BLACK);
        log.setSelectionColor(Color.LIGHT_GRAY);

        dbg = new JTextArea(3, 80);
        dbg.setMargin(new Insets(3, 3, 5, 5));
        dbg.setEditable(false);
        dbg.setBackground(Color.BLACK);
        dbg.setForeground(Color.GREEN);
        dbg.setSelectionColor(Color.GREEN);
        dbg.setSelectedTextColor(Color.BLACK);

        JScrollPane logScrollPane = new JScrollPane(log);
        JScrollPane dbgScrollPane = new JScrollPane(dbg);

//File choosers create 
        fcEVNT_IDC = new JFileChooser();
        fcEVNT_IDC.setDialogTitle("Открыть файл протокола b5evntl, b5evntb");
        fcEVNT_IDC.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filterEVNT;
        filterEVNT = new FileNameExtensionFilter("*.b5evtl, *.b5evtb файл", "b5evtl", "b5evtb");
        fcEVNT_IDC.addChoosableFileFilter(filterEVNT);

        fcXML_IDC = new JFileChooser();
        fcXML_IDC.setDialogTitle("Открыть файл экспортированных текстов .xml");
        fcXML_IDC.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filterXML;
        filterXML = new FileNameExtensionFilter("*.xml файл", "xml");
        fcXML_IDC.addChoosableFileFilter(filterXML);

//Buttons create
        itxButton = new JButton("Открыть файл текстов .xml");
        evntButton = new JButton("Открыть файл протокола .b5evt");
        setButton = new JButton("Настройки вывода текста.");
        buttons = Arrays.asList(evntButton, itxButton, setButton);
        JPanel buttonPanel = new JPanel();
        buttons.forEach((JButton b) -> {
            b.setBackground(Color.WHITE);
            b.addActionListener(this);
            buttonPanel.add(b);
        });

        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
        add(dbgScrollPane, BorderLayout.PAGE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle open button action.
        if (e.getSource() == evntButton) {
            eventReader();//a messages reader
        } else if (e.getSource() == itxButton) {
            eventReaderITX();// cutomer texts
        } else if (e.getSource() == setButton) {
            appSet();//program setup
        }
    }

    private void eventReader() {

        log.setText(null);

        buttons.forEach(b -> b.setEnabled(false));

        int returnVal = fcEVNT_IDC.showOpenDialog(EvntFileReader.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            File fileEVNT = fcEVNT_IDC.getSelectedFile();

            if (fileEVNT.getAbsolutePath().contains("b5evtl")) {

                dbg.append("Открываю файл - список событий станции Integral Schrack: " + fileEVNT.getAbsolutePath()
                        + "." + NEWLINE);
                System.out.println(NEWLINE + "Открываю файл - список событий станции Integral Schrack: "
                        + fileEVNT.getAbsolutePath() + "." + NEWLINE);
            } else {

                dbg.append("Открываю файл буфера событий станции Integral Schrack: " + fileEVNT.getAbsolutePath()
                        + "." + NEWLINE);
                System.out.println(NEWLINE + "Открываю файл буфера событий станции Integral Schrack: "
                        + fileEVNT.getAbsolutePath() + "." + NEWLINE);
            }

//The header of evnt file check, a lines counter read
            EventfileFirstLineDetector firstLineDetect = new EventfileFirstLineDetector();
            firstLineDetect.firstLineParser(fileEVNT.getAbsolutePath());
            int evntLinesCounter = firstLineDetect.getTotalLinesOfEvntFile();
            String firstEvntLine = firstLineDetect.getFirstlineOfEvntFile();

            if (firstEvntLine.equals("Выбранный файл поврежден либо имеет неверный формат.")) {

                dbg.append(firstEvntLine + " Выберите другой файл." + NEWLINE);
                System.out.println(firstEvntLine);

                buttons.forEach(b -> b.setEnabled(true));
            }

            if (evntLinesCounter > 0) {

                if (!ITXHasText) {

                    dbg.append(firstEvntLine + NEWLINE);

//A StAX EVNT will have created for choosen file
                    StAXforXML_EVNT StAXevtn = new StAXforXML_EVNT();

                    try {
                        StAXevtn.StAXforEVNT(fileEVNT.getAbsolutePath(), evntLinesCounter, outputSet);
                    } catch (XMLStreamException | IOException ex) {
                        buttons.forEach(b -> b.setEnabled(true));
                    }
                    StAXevtn.getAllMessages().forEach(msg -> {
                        if (msg != null) {
                            log.append(msg + NEWLINE);
                        }
                    });
                    System.out.print(StAXevtn.getMessageState() + NEWLINE);
                    dbg.append(StAXevtn.getMessageState() + NEWLINE);

                    buttons.forEach(b -> b.setEnabled(true));

                } else {

                    dbg.append("XML файл содержит всего " + totalOfTexts + " текстовых описаний. ");
                    System.out.print("XML файл содержит всего " + totalOfTexts + " текстовых описаний. ");

//A StAX EVNT will have created for choosen file
                    StAXforXML_EVNT StAXevtn_itx = new StAXforXML_EVNT();

                    StAXevtn_itx.setITXhasLoaded(ITXHasText, totalOfTexts, xmlTxt.getCutomerTexts());

                    try {
                        StAXevtn_itx.StAXforEVNT(fileEVNT.getAbsolutePath(), evntLinesCounter, outputSet);
                    } catch (XMLStreamException | IOException ex) {
                        buttons.forEach(b -> b.setEnabled(true));
                    }
                    StAXevtn_itx.getAllMessages().forEach(msg -> {
                        if (msg != null) {
                            log.append(msg + NEWLINE);
                        }
                    });
                    System.out.print(StAXevtn_itx.getMessageState() + NEWLINE);
                    dbg.append(StAXevtn_itx.getMessageState() + NEWLINE);
                    buttons.forEach(b -> b.setEnabled(true));
                }
            } else {
                dbg.append("Выбранный файл протокола " + fileEVNT.getAbsolutePath() + " не содержит записей."
                        + NEWLINE);
                System.out.println("Выбранный файл протокола " + fileEVNT.getAbsolutePath()
                        + " не содержит записей." + NEWLINE);
                buttons.forEach(b -> b.setEnabled(true));
            }

        } else {

            fcEVNT_IDC.doLayout();
            System.out.println("Файл .b5evt протокола событий не был выбран пользователем." + NEWLINE);
            dbg.append("Файл .b5evt протокола событий не был выбран пользователем." + NEWLINE);
            buttons.forEach(b -> b.setEnabled(true));
        }
    }

    //The ITX-XML chooser for exported from Schrack IDC
    private void eventReaderITX() {

        log.setText(null);
        buttons.forEach(b -> b.setEnabled(false));
        int returnVal = fcXML_IDC.showOpenDialog(EvntFileReader.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fileITX = fcXML_IDC.getSelectedFile();
            dbg.append(
                    "Открываю файл экспортированных текстов - xml : " + fileITX.getAbsolutePath() + "." + NEWLINE);
            System.out.println(NEWLINE + "Открываю файл экспортированных текстов - xml : "
                    + fileITX.getAbsolutePath() + "." + NEWLINE);
            //The XML customer text file check
            XMLitxCheck checkXMLtext = new XMLitxCheck();
            try {
                checkXMLtext.itxFileCheck(fileITX.getAbsolutePath());
            } catch (FileNotFoundException ex) {
                System.out.print("Файл экспортированных текстов не найден по указанному пути." + NEWLINE);
                dbg.append("Файл экспортированных текстов не найден по указанному пути." + NEWLINE);
                buttons.forEach(b -> b.setEnabled(true));
            } catch (XMLStreamException ex) {
                System.out.print("Выбранный файл поврежден или имеет неверный формат." + NEWLINE);
                dbg.append("Выбранный файл поврежден или имеет неверный формат." + NEWLINE);
                buttons.forEach(b -> b.setEnabled(true));
            }
            // Total of signif. text lines take
            if (null != checkXMLtext.getTextState()) {
                ITXHasText = true; // TEXTs exist and it has ability to be read
                totalOfTexts = checkXMLtext.totalTexts;// set a customer texts total
                System.out.print(checkXMLtext.getTextState() + NEWLINE);
                dbg.append(checkXMLtext.getTextState() + NEWLINE);
            } else {
                System.out.print("Ошибка файла текстов XML." + NEWLINE);
                dbg.append("Ошибка файла текстов XML." + NEWLINE);
                buttons.forEach(b -> b.setEnabled(true));
            }
            if (ITXHasText) {
                xmlTxt = new StAXforXML_ITX();
                try {
                    xmlTxt.StAXforXML_ITXtext(fileITX.getAbsolutePath(), totalOfTexts);
                } catch (FileNotFoundException | XMLStreamException ex) {
                    buttons.forEach(b -> b.setEnabled(true));
                }
            }
            buttons.forEach(b -> b.setEnabled(true));
        } else {
            fcEVNT_IDC.doLayout();
            System.out.println("Файл .xml экспортированных текстов не был выбран пользователем." + NEWLINE);
            dbg.append("Файл .xml экспортированных текстов не был выбран пользователем." + NEWLINE);
            buttons.forEach(b -> b.setEnabled(true));
        }
    }

    private void appSet() {

        log.setText(null);
        setWindow = new JFrame("Установка параметров отображения файла протокола Schrack.");

        List<JTextArea> txtLabels = Arrays.asList(
                new JTextArea(
                        "Для станции IntegralLAN.\n" + "Если прочитан протокол\nлокальной "
                        + "станции\n(без сети SecoNET),\nто все записи " + "соотнесены\nс локальной станцией."),
                new JTextArea(
                        "Команды отправленные\nс пультов операторами\n" + "и команды отправленные\nавтоматически системой\n"
                        + "ПС не будут показаны в\nпротоколе. Также из "
                        + "протокола\nбудет убран столбец\n\"сообщение устройства\"."),
                new JTextArea(
                        "Вместо “адрес устройства\nXXXXX/XX”" + " будет выведен\nадрес устройства без "
                        + "текста.\nНапример, “2134/14”" + " вместо\n“адрес устройства 2134/14”."),
                new JTextArea("Для устройства будет\nвыведен только основной\nтип, "
                        + "например  “выход”.\nСтолбец с указанием\nтипа "
                        + "выхода (\'АУПТ\',\n\'зона оповещения\'...)\nбудет пропущен."),
                new JTextArea("Подразумевается изменение\nсостояния самого "
                        + "устройства,\nчто явилось причиной\nпоявления " + "записи в протоколе.\nНапример, при"
                        + " недостаточном\nнапряжении питания шлейфа\nв протокол"
                        + " должно быть\nдобавлено \"питание шлейфа\"."),
                new JTextArea("Например, если это шлейф,\nто для состояния\n"
                        + "неисправности существует\nподТип состояния\nнеисправности-"
                        + " ошибка запуска,\nошибка конфигурации, обрыв..."));

        localStationBox = new JCheckBox("Убрать столбец “локальная станция”");
        comAllBox = new JCheckBox("Убрать строки “команда”");
        devAddressBox = new JCheckBox("Убрать текст “адрес устройства”");
        subTypeBox = new JCheckBox("Убрать столбец подТип устройства");
        stateChangeBox = new JCheckBox("Убрать столбец изменение состояния");
        StateSubTypeBox = new JCheckBox("Убрать столбец подТип состояния");
        checkBoxes = Arrays.asList(localStationBox, comAllBox, devAddressBox, subTypeBox, stateChangeBox, StateSubTypeBox);

        Iterator<Boolean> setIterator = Arrays.asList(outputSet).iterator();
        Iterator<JTextArea> labelsIterator = txtLabels.iterator();

        checkBoxes.forEach((JCheckBox b) -> {
            JTextArea currentTXTLabel = labelsIterator.next();
            currentTXTLabel.setEditable(false);
            currentTXTLabel.setMargin(new Insets(3, 3, 3, 3));
            JScrollPane currentPane = new JScrollPane(currentTXTLabel);
            currentPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            b.setForeground(Color.WHITE);
            b.setBackground(Color.GRAY);
            JPanel currentJPanel = new JPanel();
            currentJPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            currentJPanel.setLayout(new BorderLayout());
            currentJPanel.add(b, NORTH);
            currentJPanel.add(currentPane, CENTER);
            setWindow.add(currentJPanel);
            b.setSelected(setIterator.next());
            b.addActionListener((ActionEvent l) -> {
                if (b.getText().equals(localStationBox.getText())) {
                    if (b.isSelected()) {
                        outputSet[0] = true;
                        System.out.println("Включена опция убрать столбец \"локальная станция\" из протокола.");
                        dbg.append("Включена опция убрать столбец \"локальная станция\" из протокола." + NEWLINE);
                    } else {
                        outputSet[0] = false;
                        System.out.println("Отлючена опция убрать столбец \"локальная станция\" из протокола.");
                        dbg.append("Отлючена опция убрать столбец \"локальная станция\" из протокола." + NEWLINE);
                    }
                } else if (b.getText().equals(comAllBox.getText())) {
                    if (b.isSelected()) {
                        outputSet[1] = true;
                        System.out.println("Включена опция убрать строки \"команда\" из протокола.");
                        dbg.append("Включена опция убрать строки \"команда\" из протокола." + NEWLINE);
                    } else {
                        outputSet[1] = false;
                        System.out.println("Отлючена опция убрать строки \"команда\" из протокола.");
                        dbg.append("Отлючена опция убрать строки \"команда\" из протокола." + NEWLINE);
                    }
                } else if (b.getText().equals(devAddressBox.getText())) {
                    if (b.isSelected()) {
                        outputSet[2] = true;
                        System.out.println("Включена опция убрать текст \"адрес устройства\" из протокола.");
                        dbg.append("Включена опция убрать текст \"адрес устройства\" из протокола." + NEWLINE);
                    } else {
                        outputSet[2] = false;
                        System.out.println("Отлючена опция убрать текст \"адрес устройства\" из протокола.");
                        dbg.append("Отлючена опция убрать текст \"адрес устройства\" из протокола." + NEWLINE);
                    }
                } else if (b.getText().equals(subTypeBox.getText())) {
                    if (b.isSelected()) {
                        outputSet[3] = true;
                        System.out.println("Включена опция убрать столбец подТип устройства из протокола.");
                        dbg.append("Включена опция убрать столбец подТип устройства из протокола." + NEWLINE);
                    } else {
                        outputSet[3] = false;
                        System.out.println("Отлючена опция убрать столбец подТип устройства из протокола.");
                        dbg.append("Отлючена опция убрать столбец подТип устройства из протокола." + NEWLINE);
                    }
                } else if (b.getText().equals(stateChangeBox.getText())) {
                    if (b.isSelected()) {
                        outputSet[4] = true;
                        System.out.println("Включена опция убрать столбец изменение состояния из протокола.");
                        dbg.append("Включена опция убрать столбец изменение состояния из протокола." + NEWLINE);
                    } else {
                        outputSet[4] = false;
                        System.out.println("Отлючена опция убрать столбец изменение состояния из протокола.");
                        dbg.append("Отлючена опция убрать столбец изменение состояния из протокола." + NEWLINE);
                    }
                } else if (b.getText().equals(StateSubTypeBox.getText())) {
                    if (b.isSelected()) {
                        outputSet[5] = true;
                        System.out.println("Включена опция убрать столбец подТип состояния из протокола.");
                        dbg.append("Включена опция убрать столбец подТип состояния из протокола." + NEWLINE);
                    } else {
                        outputSet[5] = false;
                        System.out.println("Отлючена опция убрать столбец подТип состояния из протокола.");
                        dbg.append("Отлючена опция убрать столбец подТип состояния из протокола." + NEWLINE);
                    }
                }
            });
        });

        setWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setWindow.setLayout(new GridLayout(2, 3, 4, 4));

        setWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setWindow.setVisible(false);
                setWindow.dispose();
                log.append(NEWLINE + "  Установлены параметры вывода текста протокола:" + NEWLINE + NEWLINE);
                System.out.println(NEWLINE + "  Установлены параметры вывода текста протокола:" + NEWLINE);
                for (int i = 0; i < outputSet.length; i++) {
                    log.append(checkBoxes.get(i).getText() + " - " + String.valueOf(outputSet[i]).replace("true", "ВКЛ.")
                            .replace("false", "ВЫКЛ.") + NEWLINE);
                    System.out.println(checkBoxes.get(i).getText() + " - " + String.valueOf(outputSet[i]).replace("true", "ВКЛ.")
                            .replace("false", "ВЫКЛ."));
                }
            }
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                isShowingSetWindow = false;
                buttons.forEach(b -> b.setEnabled(true));
                dbg.append("Окно настроек вывода файла протокола событий было закрыто пользователем." + NEWLINE);
                System.out.println("Окно настроек вывода файла протокола событий было закрыто пользователем.");
            }
            @Override
            public void windowOpened(java.awt.event.WindowEvent windowEvent) {
                isShowingSetWindow = true;
                buttons.forEach(b -> b.setEnabled(false));
                dbg.append("Окно настроек вывода файла протокола событий было открыто пользователем." + NEWLINE);
                System.out.println("Окно настроек вывода файла протокола событий было открыто пользователем.");
            }
        });

        setWindow.setPreferredSize(new Dimension(framesize));
        setWindow.setAlwaysOnTop(true);
        setWindow.pack();
        setWindow.setLocation((screensize.width - setWindow.getWidth()) / 2,
                (screensize.height - setWindow.getHeight()) / 2);
        setWindow.setVisible(true);
    }

}
