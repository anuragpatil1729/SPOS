
import java.util.*;

public class HospitalScheduling {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of patients: ");
        int n = sc.nextInt();

        int at[] = new int[n];
        int bt[] = new int[n];
        int priority[] = new int[n];

        // Input
        for (int i = 0; i < n; i++) {

            System.out.println("\nPatient P" + (i + 1));

            System.out.print("Arrival Time: ");
            at[i] = sc.nextInt();

            System.out.print("Treatment Time: ");
            bt[i] = sc.nextInt();

            System.out.print("Priority (1=Critical, 2=Moderate, 3=Mild): ");
            priority[i] = sc.nextInt();
        }

        // NON-PREEMPTIVE PRIORITY
        int ct1[] = new int[n];
        int wt1[] = new int[n];
        int tat1[] = new int[n];

        int time = 0;
        int completed = 0;

        System.out.println("\n--- NON-PREEMPTIVE PRIORITY ---");
        System.out.println("Gantt Chart:");

        while (completed < n) {

            int index = -1;

            for (int i = 0; i < n; i++) {

                if (ct1[i] == 0 && at[i] <= time) {

                    if (index == -1 ||
                            priority[i] < priority[index]) {

                        index = i;
                    }
                }
            }

            if (index == -1) {
                time++;
                continue;
            }

            System.out.print("| P" + (index + 1) + " ");

            time = time + bt[index];

            ct1[index] = time;

            tat1[index] = ct1[index] - at[index];

            wt1[index] = tat1[index] - bt[index];

            completed++;
        }

        System.out.println("|");

        // Display Non-Preemptive result
        System.out.println("\nPatient\tAT\tBT\tPriority\tCT\tWT\tTAT");

        for (int i = 0; i < n; i++) {

            System.out.println(
                    "P" + (i + 1) + "\t" +
                            at[i] + "\t" +
                            bt[i] + "\t" +
                            priority[i] + "\t\t" +
                            ct1[i] + "\t" +
                            wt1[i] + "\t" +
                            tat1[i]);
        }

        // PREEMPTIVE PRIORITY
        int remaining[] = new int[n];

        int ct2[] = new int[n];
        int wt2[] = new int[n];
        int tat2[] = new int[n];

        for (int i = 0; i < n; i++) {
            remaining[i] = bt[i];
        }

        time = 0;
        completed = 0;

        System.out.println("\n--- PREEMPTIVE PRIORITY ---");
        System.out.println("Gantt Chart:");

        while (completed < n) {

            int index = -1;

            for (int i = 0; i < n; i++) {

                if (at[i] <= time && remaining[i] > 0) {

                    if (index == -1 ||
                            priority[i] < priority[index]) {

                        index = i;
                    }
                }
            }

            if (index == -1) {
                time++;
                continue;
            }

            System.out.print("| P" + (index + 1) + " ");

            remaining[index]--;
            time++;

            if (remaining[index] == 0) {

                ct2[index] = time;

                tat2[index] = ct2[index] - at[index];

                wt2[index] = tat2[index] - bt[index];

                completed++;
            }
        }

        System.out.println("|");

        // Display Preemptive result
        System.out.println("\nPatient\tAT\tBT\tPriority\tCT\tWT\tTAT");

        for (int i = 0; i < n; i++) {

            System.out.println(
                    "P" + (i + 1) + "\t" +
                            at[i] + "\t" +
                            bt[i] + "\t" +
                            priority[i] + "\t\t" +
                            ct2[i] + "\t" +
                            wt2[i] + "\t" +
                            tat2[i]);
        }

        sc.close();
    }
}
