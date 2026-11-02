
import java.util.*;

public class CallCenterScheduling {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of calls: ");
        int n = sc.nextInt();

        int at[] = new int[n];
        int bt[] = new int[n];

        // Take input
        for (int i = 0; i < n; i++) {

            System.out.println("\nCall C" + (i + 1));

            System.out.print("Arrival Time: ");
            at[i] = sc.nextInt();

            System.out.print("Handling Time: ");
            bt[i] = sc.nextInt();
        }

        System.out.print("\nEnter Time Quantum: ");
        int quantum = sc.nextInt();

        // Round Robin Scheduling
        int remaining[] = new int[n];
        int ct[] = new int[n];
        int wt[] = new int[n];
        int tat[] = new int[n];

        for (int i = 0; i < n; i++) {
            remaining[i] = bt[i];
        }

        int time = 0;
        int completed = 0;

        System.out.println("\n--- ROUND ROBIN SCHEDULING ---");
        System.out.println("Gantt Chart:");

        while (completed < n) {

            boolean found = false;

            for (int i = 0; i < n; i++) {

                if (at[i] <= time && remaining[i] > 0) {

                    found = true;

                    System.out.print("| C" + (i + 1) + " ");

                    if (remaining[i] > quantum) {

                        time = time + quantum;
                        remaining[i] = remaining[i] - quantum;

                    } else {

                        time = time + remaining[i];
                        remaining[i] = 0;

                        ct[i] = time;

                        tat[i] = ct[i] - at[i];

                        wt[i] = tat[i] - bt[i];

                        completed++;
                    }
                }
            }

            if (found == false) {
                time++;
            }
        }

        System.out.println("|");

        // Display Result
        System.out.println("\nCall\tAT\tBT\tCT\tWT\tTAT");

        for (int i = 0; i < n; i++) {

            System.out.println(
                    "C" + (i + 1) + "\t" +
                            at[i] + "\t" +
                            bt[i] + "\t" +
                            ct[i] + "\t" +
                            wt[i] + "\t" +
                            tat[i]);
        }

        sc.close();
    }
}
