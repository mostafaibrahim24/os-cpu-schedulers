package org.example;

import java.util.*;

class Process{
    private String processName;
    private Integer processArrivalTime;
    private Integer processBurstTime;
    private Integer processPriority;
    private Integer isAt;
    private Integer waitingTime;

    public Integer getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(Integer waitingTime) {
        this.waitingTime = waitingTime;
    }

    private Boolean isDone;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Integer getProcessArrivalTime() {
        return processArrivalTime;
    }

    public void setProcessArrivalTime(Integer processArrivalTime) {
        this.processArrivalTime = processArrivalTime;
    }

    public Integer getProcessBurstTime() {
        return processBurstTime;
    }

    public void setProcessBurstTime(Integer processBurstTime) {
        this.processBurstTime = processBurstTime;
    }

    public Integer getProcessPriority() {
        return processPriority;
    }

    public void setProcessPriority(Integer processPriority) {
        this.processPriority = processPriority;
    }

    public Integer getIsAt() {
        return isAt;
    }

    public void setIsAt(Integer isAt) {
        this.isAt = isAt;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return "Process{" +
                "processName='" + processName + '\'' +
                ", processArrivalTime=" + processArrivalTime +
                ", processBurstTime=" + processBurstTime +
                ", processPriority=" + processPriority +
                ", isAt=" + isAt +
                ", waitingTime=" + waitingTime +
                ", isDone=" + isDone +
                '}';
    }
}

class ShortestJobFirstScheduler{

    ShortestJobFirstScheduler(List<Process>processes){
        Util.sortAccordingToArrivalTime(processes);//Sort according to arrival time

    }
}
class Util{
    public static List<Process> extractSameTimeSubarray(List<Process> processes,Integer time, String typeOfExtraction){//To sort according to burst time if they arrived at the same place
        Process[] arrProcesses = processes.toArray(new Process[processes.size()]);
        List<Process> sub=new ArrayList<Process>();
        if(typeOfExtraction=="arrivalTime"){
            for(int i=0;i<arrProcesses.length;i++){
                if(arrProcesses[i].getProcessArrivalTime()==time){
                    sub.add(arrProcesses[i]);
                }
            }
        }
        else if (typeOfExtraction=="isAt"){
            for(int i=0;i<arrProcesses.length;i++){
                if(arrProcesses[i].getIsAt()==time){
                    sub.add(arrProcesses[i]);
                }
            }
        }
        return sub;

    }
    public static void sortAccordingToArrivalTime(List<Process> processes){
        Collections.sort(processes, new Comparator<Process>() {
                    @Override
                    public int compare(Process p1, Process p2) {
                        return p1.getProcessArrivalTime().compareTo(p2.getProcessArrivalTime());
                    }
                }
        );
    }
    public static void sortAccordingToBurstTime(ArrayList<Process> processes){
        Collections.sort(processes, new Comparator<Process>() {
                    @Override
                    public int compare(Process p1, Process p2) {
                        return p1.getProcessBurstTime().compareTo(p2.getProcessBurstTime());
                    }
                }
        );
    }
    public static void sortAccordingToWaitingTime(ArrayList<Process> processes){
        Collections.sort(processes, new Comparator<Process>() {
                    @Override
                    public int compare(Process p1, Process p2) {
                        return p1.getWaitingTime().compareTo(p2.getWaitingTime());
                    }
                }
        );
    }
}
public class Main {
    public static void main(String[] args) {

        System.out.println("Number of processes: ");
        Scanner scanner= new Scanner(System.in);
        int numberOfProcesses = scanner.nextInt();
        System.out.println("Round robin Time Quantum: ");
        int roundRobinTimeQuantum = scanner.nextInt();
        System.out.println("Context switching: ");
        int contextSwitching = scanner.nextInt();

        List<Process> processes = new ArrayList<Process>();

        for(int i=0;i<numberOfProcesses;i++){
            Process process = new Process();
            System.out.println("Process "+ (i+1) +" name: ");
            process.setProcessName(scanner.next());

            System.out.println("Process "+ (i+1) +" arrival time: ");
            process.setProcessArrivalTime(scanner.nextInt());
            process.setIsAt(process.getProcessArrivalTime());

            System.out.println("Process "+ (i+1) +" burst time: ");
            process.setProcessBurstTime(scanner.nextInt());

            System.out.println("Process "+ (i+1) +" priority: ");
            process.setProcessPriority(scanner.nextInt());

            process.setDone(false);

            processes.add(process);
        }


    }
}