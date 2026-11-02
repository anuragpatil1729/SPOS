
import java.io.*;
import java.util.*;

class Pass2 {

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

        ArrayList<String> intermediate = new ArrayList<>();

        int symbolCount = 0;
        int literalCount = 0;
        int address = 0;

        String line;

        // PASS 1: Generate Intermediate Code
        while ((line = br.readLine()) != null) {

            StringTokenizer t = new StringTokenizer(line, " ,\t");

            if (!t.hasMoreTokens())
                continue;

            String word = t.nextToken();

            if (word.equals("START")) {
                address = Integer.parseInt(t.nextToken());
                intermediate.add(address + " AD 1");
                continue;
            }

            if (word.equals("END")) {
                intermediate.add(address + " AD 2");
                break;
            }

            StringBuilder output = new StringBuilder();

            if (!isOperation(word, IS, AD, DL)) {

                symbol[symbolCount] = word;
                symbolAddress[symbolCount] = address;

                output.append(address).append(" S")
                        .append(symbolCount).append(" ");

                symbolCount++;

                word = t.nextToken();

            } else {
                output.append(address).append(" ");
            }

            for (int i = 0; i < IS.length; i++) {

                if (word.equals(IS[i])) {

                    output.append("IS ").append(i + 1).append(" ");

                    while (t.hasMoreTokens()) {

                        String operand = t.nextToken();

                        for (int j = 0; j < REG.length; j++) {
                            if (operand.equals(REG[j]))
                                output.append(j + 1).append(" ");
                        }

                        if (operand.startsWith("=")) {

                            literal[literalCount] = operand;

                            output.append("L")
                                    .append(literalCount).append(" ");

                            literalCount++;
                        }

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
                                        .append(symbolCount).append(" ");

                                symbolCount++;

                            } else {
                                output.append("S")
                                        .append(found).append(" ");
                            }
                        }
                    }

                    address++;
                }
            }

            if (word.equals("DC")) {
                output.append("DL 1 ");
                t.nextToken();
                address++;
            }

            if (word.equals("DS")) {
                output.append("DL 2 ");
                int size = Integer.parseInt(t.nextToken());
                address += size;
            }

            intermediate.add(output.toString());
        }

        // Assign literal addresses
        for (int i = 0; i < literalCount; i++) {
            literalAddress[i] = address;
            address++;
        }

        br.close();

        // PASS 2: Generate Machine Code
        System.out.println("\nINTERMEDIATE CODE");
        System.out.println("-------------------------");

        for (String s : intermediate)
            System.out.println(s);

        System.out.println("\nMACHINE CODE");
        System.out.println("-------------------------");

        int lc = 0;

        for (String s : intermediate) {

            StringTokenizer t = new StringTokenizer(s);

            String word = t.nextToken();

            if (word.equals("AD")) {
                continue;
            }

            lc = Integer.parseInt(word);

            word = t.nextToken();

            if (word.equals("AD")) {
                t.nextToken();
                System.out.println(lc + " No Machine Code");
                continue;
            }

            if (!word.equals("IS") && !word.equals("DL")) {
                word = t.nextToken();
            }

            if (word.equals("IS")) {

                int opcode = Integer.parseInt(t.nextToken());

                int reg = 0;
                int operand = 0;

                if (t.hasMoreTokens()) {

                    String x = t.nextToken();

                    if (x.matches("[1-4]")) {
                        reg = Integer.parseInt(x);

                        if (t.hasMoreTokens())
                            x = t.nextToken();
                    }

                    if (x.startsWith("S")) {
                        int n = Integer.parseInt(x.substring(1));
                        operand = symbolAddress[n];
                    }

                    if (x.startsWith("L")) {
                        int n = Integer.parseInt(x.substring(1));
                        operand = literalAddress[n];
                    }
                }

                String op = (opcode < 10 ? "0" : "") + opcode;

                String opnd = String.format("%03d", operand);

                System.out.println(
                        lc + " " + op + " " + reg + " " + opnd);
            }

            if (word.equals("DL")) {

                int code = Integer.parseInt(t.nextToken());

                if (code == 1) {
                    System.out.println(lc + " 00 0 000");
                }
            }
        }

        // Symbol Table
        System.out.println("\nSYMBOL TABLE");
        System.out.println("-------------------------");
        System.out.println("Index\tSymbol\tAddress");

        for (int i = 0; i < symbolCount; i++) {
            System.out.println(i + "\t" +
                    symbol[i] + "\t" + symbolAddress[i]);
        }

        // Literal Table
        System.out.println("\nLITERAL TABLE");
        System.out.println("-------------------------");
        System.out.println("Index\tLiteral\tAddress");

        for (int i = 0; i < literalCount; i++) {
            System.out.println(i + "\t" +
                    literal[i] + "\t" + literalAddress[i]);
        }

        System.out.println("\nPass 2 completed successfully!");
    }

    static boolean isOperation(
            String word, String IS[], String AD[], String DL[]) {

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
