package org.example;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        System.out.println("Number of processes: ");
        Scanner sc= new Scanner(System.in);
        int numberOfProcesses = sc.nextInt();
        System.out.println("Round robin Time Quantum: ");
        int roundRobinTimeQuantum = sc.nextInt();
        System.out.println("Context switching: ");
        int contextSwitching = sc.nextInt();

        String[] processesNames = new String[numberOfProcesses];
        int[] processesArrivalTime = new int[numberOfProcesses];
        int[] processesBurstTime = new int[numberOfProcesses];
        int[] processesPriority = new int[numberOfProcesses];

        for(int i=0;i<numberOfProcesses;i++){
            System.out.println("Process "+ (i+1) +" name: ");
            processesNames[i]=sc.next();
            System.out.println("Process "+ (i+1) +" arrival time: ");
            processesArrivalTime[i]=sc.nextInt();
            System.out.println("Process "+ (i+1) +" burst time: ");
            processesBurstTime[i]=sc.nextInt();
            System.out.println("Process "+ (i+1) +" priority: ");
            processesPriority[i]=sc.nextInt();
        }

    }
}