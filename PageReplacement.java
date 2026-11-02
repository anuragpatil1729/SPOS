
import java.util.*;

public class PageReplacement {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of pages: ");
        int n = sc.nextInt();

        int pages[] = new int[n];

        System.out.println("Enter page reference string:");

        for (int i = 0; i < n; i++) {
            pages[i] = sc.nextInt();
        }

        System.out.print("Enter number of frames: ");
        int frames = sc.nextInt();

        int memory[] = new int[frames];

        // Initially all frames are empty
        for (int i = 0; i < frames; i++) {
            memory[i] = -1;
        }

        int pointer = 0;
        int pageFaults = 0;

        // FIFO PAGE REPLACEMENT
        System.out.println("\n--- FIFO PAGE REPLACEMENT ---");

        for (int i = 0; i < n; i++) {

            boolean found = false;

            // Check whether page is already present
            for (int j = 0; j < frames; j++) {

                if (memory[j] == pages[i]) {
                    found = true;
                    break;
                }
            }

            // Page Fault
            if (found == false) {

                memory[pointer] = pages[i];

                pointer = (pointer + 1) % frames;

                pageFaults++;

                System.out.print("Page " + pages[i] + " -> Fault\t");
            }

            else {
                System.out.print("Page " + pages[i] + " -> Hit\t");
            }

            // Display memory
            for (int j = 0; j < frames; j++) {

                if (memory[j] == -1) {
                    System.out.print("- ");
                }

                else {
                    System.out.print(memory[j] + " ");
                }
            }

            System.out.println();
        }

        // RESULT
        System.out.println("\nTotal Page Faults: " + pageFaults);

        sc.close();
    }
}
