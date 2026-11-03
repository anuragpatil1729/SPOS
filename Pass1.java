
import java.io.*;
import java.util.*;

class Pass1 {
    public static void main(String args[]) throws Exception {

        BufferedReader br = new BufferedReader(
                new FileReader("initial.txt"));

        String IS[] = {
                "STOP", "ADD", "SUB", "MULT", "MOVER",
                "MOVEM", "COMP", "BC", "DIV", "READ", "PRINT"
        };

        String REG[] = { "AREG", "BREG", "CREG", "DREG" };
        String AD[] = { "START", "END" };
        String DL[] = { "DC", "DS" };

        String symbol[] = new String[10];
        int symbolAddress[] = new int[10];

        String literal[] = new String[10];
        int literalAddress[] = new int[10];

        int symbolCount = 0;
        int literalCount = 0;
        int address = 0;

        String line;

        System.out.println("\nINTERMEDIATE CODE");
        System.out.println("-----------------");

        while ((line = br.readLine()) != null) {

            StringTokenizer stoken = new StringTokenizer(line, " ,\t");

            if (!stoken.hasMoreTokens())
                continue;

            String word = stoken.nextToken();

            // START
            if (word.equals("START")) {
                address = Integer.parseInt(stoken.nextToken());
                System.out.println(address + " AD 1");
                continue;
            }

            // END
            if (word.equals("END")) {
                System.out.println(address + " AD 2");
                break;
            }

            StringBuilder output = new StringBuilder();

            // Check LABEL
            if (!isOperation(word, IS, AD, DL)) {

                symbol[symbolCount] = word;
                symbolAddress[symbolCount] = address;

                output.append(address)
                        .append(" S")
                        .append(symbolCount)
                        .append(" ");

                symbolCount++;

                word = stoken.nextToken();

            } else {
                output.append(address).append(" ");
            }

            // Instructions
            for (int i = 0; i < IS.length; i++) {

                if (word.equals(IS[i])) {

                    output.append("IS ")
                            .append(i + 1)
                            .append(" ");

                    while (stoken.hasMoreTokens()) {

                        String operand = stoken.nextToken();

                        // Register
                        for (int j = 0; j < REG.length; j++) {
                            if (operand.equals(REG[j])) {
                                output.append(j + 1).append(" ");
                            }
                        }

                        // Literal
                        if (operand.startsWith("=")) {

                            literal[literalCount] = operand;

                            output.append("L")
                                    .append(literalCount)
                                    .append(" ");

                            literalCount++;
                        }

                        // Symbol
                        if (isSymbol(operand)) {

                            int found = -1;

                            for (int j = 0; j < symbolCount; j++) {
                                if (symbol[j].equals(operand)) {
                                    found = j;
                                    break;
                                }
                            }

                            if (found == -1) {

                                symbol[symbolCount] = operand;
                                symbolAddress[symbolCount] = 0;

                                output.append("S")
                                        .append(symbolCount)
                                        .append(" ");

                                symbolCount++;

                            } else {

                                output.append("S")
                                        .append(found)
                                        .append(" ");
                            }
                        }
                    }

                    address++;
                }
            }

            // DC
            if (word.equals("DC")) {

                output.append("DL 1 ");
                stoken.nextToken();

                address++;
            }

            // DS
            if (word.equals("DS")) {

                output.append("DL 2 ");

                int size = Integer.parseInt(stoken.nextToken());

                address += size;
            }

            System.out.println(output);
        }

        // Assign literal addresses
        for (int i = 0; i < literalCount; i++) {
            literalAddress[i] = address;
            address++;
        }

        br.close();

        // SYMBOL TABLE
        System.out.println("\nSYMBOL TABLE");
        System.out.println("-------------------------");
        System.out.println("Index\tSymbol\tAddress");

        for (int i = 0; i < symbolCount; i++) {
            System.out.println(i + "\t" +
                    symbol[i] + "\t" +
                    symbolAddress[i]);
        }

        // LITERAL TABLE
        System.out.println("\nLITERAL TABLE");
        System.out.println("-------------------------");
        System.out.println("Index\tLiteral\tAddress");

        for (int i = 0; i < literalCount; i++) {
            System.out.println(i + "\t" +
                    literal[i] + "\t" +
                    literalAddress[i]);
        }

        System.out.println("\nPass 1 Completed Successfully!");
    }

    static boolean isOperation(
            String word,
            String IS[],
            String AD[],
            String DL[]) {

        for (String x : IS)
            if (word.equals(x))
                return true;

        for (String x : AD)
            if (word.equals(x))
                return true;

        for (String x : DL)
            if (word.equals(x))
                return true;

        return false;
    }

    static boolean isSymbol(String word) {

        if (word.startsWith("="))
            return false;

        if (word.equals("AREG") ||
                word.equals("BREG") ||
                word.equals("CREG") ||
                word.equals("DREG"))
            return false;

        return Character.isLetter(word.charAt(0));
    }
}
