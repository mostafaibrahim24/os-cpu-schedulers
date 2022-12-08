package org.example;

import java.util.*;

class Process{
    private String processName;
    private Integer processArrivalTime;
    private Integer processBurstTime;
    private Integer remainingBurstTime;
    private Integer processPriority;
    private Integer isAt;
    private Integer waitingTime;

    private  Integer turnaroundTime;

    public Integer getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(Integer turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

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

    public Integer getRemainingBurstTime() {
        return remainingBurstTime;
    }

    public void setRemainingBurstTime(Integer remainingBurstTime) {
        this.remainingBurstTime = remainingBurstTime;
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

    public Process() {
    }

    public Process(String processName, Integer processArrivalTime, Integer processBurstTime, Integer remainingBurstTime, Integer processPriority, Integer isAt, Integer waitingTime, Boolean isDone) {
        this.processName = processName;
        this.processArrivalTime = processArrivalTime;
        this.processBurstTime = processBurstTime;
        this.remainingBurstTime = remainingBurstTime;
        this.processPriority = processPriority;
        this.isAt = isAt;
        this.waitingTime = waitingTime;
        this.isDone = isDone;
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
                ", waitingTime=" + waitingTime +
                ", turnaroundTime=" + turnaroundTime +
                '}';
    }
}

class ShortestJobFirstScheduler{

    public static  Process getProcessOfMinWaitingTime(List<Process> processes,Integer minBurstValue){
        List<Process> sameMinBurstProcesses = new ArrayList<Process>();
        for (int i = 0; i < processes.size(); i++) {
            if(processes.get(i).getRemainingBurstTime()==minBurstValue){
                sameMinBurstProcesses.add(processes.get(i));
            }
        }
        Util.sortAccordingToWaitingTime(sameMinBurstProcesses);
        return  sameMinBurstProcesses.get(0);
    }
    public static Process getProcessOfMinBurst(List<Process> processes, Integer time){
        //System.out.println("In getProcessOfMinBurst");
        //for (int i = 0; i < processes.size(); i++) {
        //    System.out.println(processes.get(i).toString());
        //}
        List<Process> notDoneProcesses = new ArrayList<Process>();
        for(int i=0;i< processes.size();i++){
            //System.out.println("IN LOOP OF MIN BURST: "+processes.get(i).toString());
            if(processes.get(i).getDone()==true){
                continue;
            }
            if(processes.get(i).getIsAt()<=time) {
                processes.get(i).toString();
                notDoneProcesses.add(processes.get(i));
            }
        }
        Util.sortAccordingToBurstTime(notDoneProcesses);
        if(notDoneProcesses.size()==0){return null;}
        return  getProcessOfMinWaitingTime(notDoneProcesses,notDoneProcesses.get(0).getRemainingBurstTime());
    }
    ShortestJobFirstScheduler(List<Process>processes){
//        System.out.println("In constructor");
//        for (int i = 0; i < processes.size(); i++) {
//            System.out.println(processes.get(i).toString());
//        }

        Util.sortAccordingToArrivalTime(processes);//Sort according to arrival time

//       System.out.println("In constructor, after sort according to arrival time");
//        for (int i = 0; i < processes.size(); i++) {
//            System.out.println(processes.get(i).toString());
//       }

        Boolean executing= true;
        Integer time =0;
        while(executing){
            Process p = getProcessOfMinBurst(processes,time);

            Process tempP = new Process(p.getProcessName(),p.getProcessArrivalTime(),p.getProcessBurstTime(),p.getRemainingBurstTime(),p.getProcessPriority(),p.getIsAt(),p.getWaitingTime(),p.getDone());
            tempP.setRemainingBurstTime(tempP.getRemainingBurstTime()-1);
            processes.set(processes.indexOf(p),tempP);
            if(processes.get(processes.indexOf(tempP)).getRemainingBurstTime()==0){
                processes.get(processes.indexOf(tempP)).setDone(true);
                processes.get(processes.indexOf(tempP)).setIsAt(processes.get(processes.indexOf(tempP)).getIsAt()+1);
            }
            for(int i=0;i<processes.size();i++){
                if(processes.get(i).getDone()==false && processes.get(i).getIsAt()==time){
                    processes.get(i).setIsAt(processes.get(i).getIsAt()+1);
                }
            }
            time++;
            Boolean isAllDone=true;
            for(int i=0;i<processes.size();i++){
                if(processes.get(i).getDone()==false){
                    isAllDone=false;
                }
            }
            if(isAllDone==true){
                executing=false;
            }
        }
        for(int i=0;i<processes.size();i++){
            //System.out.println("Right Before Process print: "+processes.get(i).toString());
          //processes.get(i).setWaitingTime((processes.get(i).getIsAt()-processes.get(i).getProcessBurstTime())-processes.get(i).getProcessArrivalTime());

            Integer tempIsAt=processes.get(i).getIsAt();
            Integer tempArrivalTime=processes.get(i).getProcessArrivalTime();
            Integer tempBurstTime=processes.get(i).getProcessBurstTime();
            processes.get(i).setWaitingTime(tempIsAt-(tempArrivalTime+tempBurstTime));
            processes.get(i).setTurnaroundTime(tempBurstTime+processes.get(i).getWaitingTime());
            System.out.println("Process print: "+processes.get(i).toString());
        }

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
    public static void sortAccordingToBurstTime(List<Process> processes){
        Collections.sort(processes, new Comparator<Process>() {
                    @Override
                    public int compare(Process p1, Process p2) {
                        return p1.getRemainingBurstTime().compareTo(p2.getRemainingBurstTime());
                    }
                }
        );
    }
    public static void sortAccordingToWaitingTime(List<Process> processes){
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
            System.out.print("Process "+ (i+1) +" name: ");
            process.setProcessName(scanner.next());

            System.out.print("Process "+ (i+1) +" arrival time: ");
            process.setProcessArrivalTime(scanner.nextInt());
            process.setIsAt(process.getProcessArrivalTime());

            System.out.print("Process "+ (i+1) +" burst time: ");
            process.setProcessBurstTime(scanner.nextInt());
            process.setRemainingBurstTime(process.getProcessBurstTime());

            //System.out.print("Process "+ (i+1) +" priority: ");
            //process.setProcessPriority(scanner.nextInt());

            process.setDone(false);
            process.setWaitingTime(0);
            processes.add(process);
        }
//        for (int i = 0; i < processes.size(); i++) {
//            System.out.println(processes.get(i).toString());
//        }
        ShortestJobFirstScheduler shortestJobFirstScheduler = new ShortestJobFirstScheduler(processes);
    }
}