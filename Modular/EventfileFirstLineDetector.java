package EvntFileReader;

/*
 * здесь производится обработка файла буфера событий 
 * или файла списка событий станции Schrack Integral IP
 * при условии, что файл содержит корректный заголовок в 
 * первой строке
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

class EventfileFirstLineDetector {

    //a first line scanner
    Scanner firstLineScanner;
    String resultLine = "Выбранный файл поврежден либо имеет неверный формат.";
    private int linesCounter;

    // a file header parse
    void firstLineParser(String evntFile) {

        String firstLine;
        String bufferOrlist = "";
        String totalQuantity = "0";// the total lines in .evnt file

        try {

            firstLineScanner = new Scanner(new BufferedReader(new FileReader(evntFile)));

        } catch (FileNotFoundException ex) {

        }

        if (firstLineScanner.hasNextLine()) {

            firstLine = firstLineScanner.nextLine();
            firstLineScanner.close();
            firstLine = firstLine.replaceAll("<", "");
            firstLine = firstLine.replaceAll(">", "");
            firstLine = firstLine.replaceAll("\" ", "\":");

            String[] SplitingLines = firstLine.split(":");

            for (String tempLine : SplitingLines) {

                if (tempLine.contains("totalQuantity")) {

                    totalQuantity = tempLine.replace("totalQuantity=\"", "");
                    totalQuantity = totalQuantity.replace("\"", "");

                }

                if (tempLine.contains("list") || tempLine.contains("List")) {

                    bufferOrlist = " списка событий";

                }

                if (tempLine.contains("buffer") || tempLine.contains("Buffer")) {

                    bufferOrlist = " буфера событий";

                }

            }

            linesCounter = Integer.parseInt(totalQuantity);

            if (linesCounter > 0) {

                switch (Integer.parseInt(totalQuantity.substring(totalQuantity.length() - 1))) {
                    case 1:
                        resultLine = "Файл" + bufferOrlist + " содержит " + totalQuantity + " запись.";
                        break;
                    case 2:
                    case 3:
                    case 4:
                        resultLine = "Файл" + bufferOrlist + " содержит " + totalQuantity + " записи.";
                        break;

                    default:
                        resultLine = "Файл" + bufferOrlist + " содержит " + totalQuantity + " записей.";
                }
                System.out.println("Параметры файла протокола: ");
                for (String printHeadLine : SplitingLines) {
                    System.out.print("[" + printHeadLine + "] ");
                }
                System.out.println("\n" + resultLine + "\n");
            }
        }

    }

    String getFirstlineOfEvntFile() {
        return resultLine;
    }

    int getTotalLinesOfEvntFile() {
        return linesCounter;
    }
}

/*
 * //Lists IDs <id="0" "AlarmList"/> списка тревог <id="1" "FaultList"/> списка
 * неисправностей <id="2" "DisablementList"/> списка отключений <id="3"
 * "ActivationList"/> списка активаций выходов <id="4" "ActiveList"/> списка
 * активаций входов <id="5" "WarningList"/> списка предупреждений <id="6"
 * "DelayLayerList"/> списка акт. задержек <id="254" "Trigger-Buffer"/> списка
 * триггреа событий <id="255" "Repeat-Buffer"/> списка повторов
 */
