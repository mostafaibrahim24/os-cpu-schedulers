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

    private List<Process> processes;
    public static Process getProcessOfMinBurst(List<Process> processes, Integer time){
        List<Process> notDoneProcesses = new ArrayList<Process>();
        for(int i=0;i< processes.size();i++){
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
        return notDoneProcesses.get(0);
    }
    ShortestJobFirstScheduler(List<Process>sjfProcesses){
        processes=sjfProcesses;
        Util.sortAccordingToArrivalTime(processes);//Sort according to arrival time
    }
    public void printProcesses(){
        for (int i = 0; i < processes.size(); i++) {
            System.out.println(processes.get(i).toString());
        }
    }
    public List<Process> run(Integer contextSwitch){
        Boolean executing= true;
        Integer time =0;
        Boolean samePreviousProcess=true;
        Process previousProcess= new Process();
        while(executing){
            Process p = getProcessOfMinBurst(processes,time);
            if(time!=0&&previousProcess.getProcessName()!=p.getProcessName()) {
                for (int i = 0; i < processes.size(); i++) {
                    if (processes.get(i).getDone() == false) {
                        processes.get(i).setIsAt(processes.get(i).getIsAt() + contextSwitch);
                    }
                }
                time += contextSwitch;
                p = getProcessOfMinBurst(processes, time);
            }
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
            previousProcess = new Process(tempP.getProcessName(),tempP.getProcessArrivalTime(),tempP.getProcessBurstTime(),tempP.getRemainingBurstTime(),tempP.getProcessPriority(),tempP.getIsAt(),tempP.getWaitingTime(),tempP.getDone());

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
            Integer tempIsAt=processes.get(i).getIsAt();
            Integer tempArrivalTime=processes.get(i).getProcessArrivalTime();
            Integer tempBurstTime=processes.get(i).getProcessBurstTime();
            processes.get(i).setWaitingTime(tempIsAt-(tempArrivalTime+tempBurstTime));
            processes.get(i).setTurnaroundTime(tempBurstTime+processes.get(i).getWaitingTime());
        }
        return processes;
    }
}
class Util{
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
}
public class Main {
    public static void main(String[] args) {

        System.out.print("Number of processes: ");
        Scanner scanner= new Scanner(System.in);
        int numberOfProcesses = scanner.nextInt();
        System.out.print("Round robin Time Quantum: ");
        int roundRobinTimeQuantum = scanner.nextInt();
        System.out.print("Context switching: ");
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
        List<Process> sjfProcesses = new ArrayList<Process>();
        List<Process> rrProcesses = new ArrayList<Process>();
        List<Process> fcfsProcesses = new ArrayList<Process>();
        List<Process> agProcesses = new ArrayList<Process>();
        for (int i = 0; i < processes.size(); i++) {
            sjfProcesses.add(new Process(processes.get(i).getProcessName(),
                    processes.get(i).getProcessArrivalTime(),
                    processes.get(i).getProcessBurstTime(),
                    processes.get(i).getRemainingBurstTime(),
                    processes.get(i).getProcessPriority(),
                    processes.get(i).getIsAt(),
                    processes.get(i).getWaitingTime(),
                    processes.get(i).getDone()));
            rrProcesses.add(new Process(processes.get(i).getProcessName(),
                    processes.get(i).getProcessArrivalTime(),
                    processes.get(i).getProcessBurstTime(),
                    processes.get(i).getRemainingBurstTime(),
                    processes.get(i).getProcessPriority(),
                    processes.get(i).getIsAt(),
                    processes.get(i).getWaitingTime(),
                    processes.get(i).getDone()));
            fcfsProcesses.add(new Process(processes.get(i).getProcessName(),
                    processes.get(i).getProcessArrivalTime(),
                    processes.get(i).getProcessBurstTime(),
                    processes.get(i).getRemainingBurstTime(),
                    processes.get(i).getProcessPriority(),
                    processes.get(i).getIsAt(),
                    processes.get(i).getWaitingTime(),
                    processes.get(i).getDone()));
            agProcesses.add(new Process(processes.get(i).getProcessName(),
                    processes.get(i).getProcessArrivalTime(),
                    processes.get(i).getProcessBurstTime(),
                    processes.get(i).getRemainingBurstTime(),
                    processes.get(i).getProcessPriority(),
                    processes.get(i).getIsAt(),
                    processes.get(i).getWaitingTime(),
                    processes.get(i).getDone()));
        }
        ShortestJobFirstScheduler shortestJobFirstScheduler = new ShortestJobFirstScheduler(sjfProcesses);
        sjfProcesses=shortestJobFirstScheduler.run(contextSwitching);
        Float sumOfWaitingTime= 0.0F;
        Float sumOfTurnaroundTime= 0.0F;
        for (int i = 0; i < sjfProcesses.size(); i++) {
            System.out.println("Process print: "+sjfProcesses.get(i).toString());
            sumOfWaitingTime+=sjfProcesses.get(i).getWaitingTime();
            sumOfTurnaroundTime+=sjfProcesses.get(i).getTurnaroundTime();
        }
        //Average waiting time
        Float averageWaitingTime= sumOfWaitingTime/(float)sjfProcesses.size();
        System.out.println("Average waiting time: "+averageWaitingTime);
        //Average turnaround time
        Float averageTurnaroundTime = sumOfTurnaroundTime/(float)sjfProcesses.size();
        System.out.println("Average turnaround time: "+averageTurnaroundTime);


    }
}