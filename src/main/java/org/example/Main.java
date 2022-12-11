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

    private Integer quantum;

    public Integer getQuantum() {
        return quantum;
    }

    public void setQuantum(Integer quantum) {
        this.quantum = quantum;
    }

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
                ", isDone="+isDone+
                ", quantum="+quantum+
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
        String executionOrder="";
        System.out.println("-------------------SJF (execution info)-------------------");
        System.out.println("Process executed at each unit time:");
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
            System.out.println(tempP.getProcessName()+" is executing");
            tempP.setRemainingBurstTime(tempP.getRemainingBurstTime()-1);
            processes.set(processes.indexOf(p),tempP);
            if(processes.get(processes.indexOf(tempP)).getRemainingBurstTime()==0){
                processes.get(processes.indexOf(tempP)).setDone(true);
                processes.get(processes.indexOf(tempP)).setIsAt(processes.get(processes.indexOf(tempP)).getIsAt()+1);
                executionOrder= executionOrder+" "+processes.get(processes.indexOf(tempP)).getProcessName()+" ";
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
        System.out.println("Execution order (=>): "+executionOrder);
        return processes;
    }
}
class AGScheduler{
    private List<Process> processes;
    private Queue<Process> readyQueue;
    public AGScheduler(List<Process>agProcesses) {
        List<String> exeOrder= new ArrayList<String>();
        System.out.println("----------------------AG----------------------");
        Scanner scanner= new Scanner(System.in);
        for (int i = 0; i < agProcesses.size(); i++) {
            System.out.print("Process "+ (i+1) +" quantum: ");
            agProcesses.get(i).setQuantum(scanner.nextInt());
        }
        Util.sortAccordingToArrivalTime(agProcesses);
        processes = agProcesses;

        readyQueue = new LinkedList<Process>();
//        for (int i = 0; i < processes.size(); i++) {
//            readyQueue.add(processes.get(i));
//        }
        Boolean executing = true;
        Integer time=0;
        readyQueue.add(processes.get(0));
        Process previousProcess = new Process("",null,null,null,null,null,null,null);
        Process currentProcess = new Process();
        Boolean isFCFS = true;
        String historyOfQuantum="";
        historyOfQuantum+="Quantum (";
        for (int i = 0; i < processes.size(); i++) {
            if(i==processes.size()-1){
//                        System.out.print(processes.get(i).getQuantum()+")");
                historyOfQuantum+=(processes.get(i).getQuantum()+")");
                continue;
            }
//                    System.out.print(processes.get(i).getQuantum()+",");
            historyOfQuantum+=(processes.get(i).getQuantum()+",");
        }
        List<String> historyOfQuantumAllProcessesForPrint= new ArrayList<String>();
        historyOfQuantumAllProcessesForPrint.add(historyOfQuantum);
        historyOfQuantum="";
        while(executing){
            if(allAreProcessesAreDone()){
                executing=false;
                break;
            }
            if(isFCFS==true){
                //System.out.println("FCFS, "+time);

                //System.out.println("\n Ready Queue: "+readyQueue.toString());
                currentProcess=readyQueue.remove();
                //System.out.println("FCFS: current:"+currentProcess.getProcessName()+" , previous:"+previousProcess.getProcessName());
                //System.out.println("fcfs's process: "+currentProcess.getProcessName()+" its quantum: "+currentProcess.getQuantum());
            }
            if(previousProcess.getProcessName()!=currentProcess.getProcessName()){
//                if(currentProcess.getRemainingBurstTime()==0){
//                    isFCFS=true;
//                    processes.get(processes.indexOf(currentProcess)).setDone(true);
//                    readyQueue.remove(currentProcess);
//                    continue;
//                }

//                System.out.print("Quantum (");
                historyOfQuantum+="Quantum (";
                for (int i = 0; i < processes.size(); i++) {
                    if(i==processes.size()-1){
//                        System.out.print(processes.get(i).getQuantum()+")");
                        historyOfQuantum+=(processes.get(i).getQuantum()+")");
                        continue;
                    }
//                    System.out.print(processes.get(i).getQuantum()+",");
                    historyOfQuantum+=(processes.get(i).getQuantum()+",");
                }
//                System.out.println("\n-----");
                //historyOfQuantum+="\n-----\n";
                historyOfQuantumAllProcessesForPrint.add(historyOfQuantum);
                historyOfQuantum="";

                //25% uninterrupted when new process
                Integer startTime=time;
                time+=(int) Math.ceil(((float)currentProcess.getQuantum())*0.25);
                checkForArrivals(startTime,time);
                Integer remainingQuantum=currentProcess.getQuantum()-((int) Math.ceil(((float)currentProcess.getQuantum())*0.25));
                //System.out.println("time: "+time+", "+currentProcess.getProcessName()+", Remaining Quantum (in 25%)= "+remainingQuantum);
                currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime()-((int) Math.ceil(((float)currentProcess.getQuantum())*0.25)));
                //System.out.println("Before if(currentProcess.getRemainingBurstTime()<=0)");
                //System.out.println("Process name:"+currentProcess.getProcessName()+" ,Remaining burst: "+currentProcess.getRemainingBurstTime());
                if(currentProcess.getRemainingBurstTime()<=0){
                    isFCFS=true;
                    processes.get(processes.indexOf(currentProcess)).setDone(true);
                    processes.get(processes.indexOf(currentProcess)).setQuantum(0);
                    processes.get(processes.indexOf(currentProcess)).setRemainingBurstTime(0);
                    //readyQueue.remove(currentProcess);
                    removeFromReadyQueue(currentProcess);
                    previousProcess=currentProcess;
                    exeOrder.add(currentProcess.getProcessName());
                    continue;
                }
                //System.out.println("After if(currentProcess.getRemainingBurstTime()<=0)");
                //50% non-preemptive priority
                Process highPriorityProcess = getHighestPriorityProcess(time); //inside uses readyQueue NO NOT FROM QUEUE, HIGHEST PRIORITY OF THAT TIME
                if(currentProcess.getProcessName()!=highPriorityProcess.getProcessName()){
                    currentProcess.setQuantum((int) Math.ceil(((double) currentProcess.getQuantum()+ ((double) remainingQuantum)/2)));
                    //System.out.println("non-pre priority switch: current:"+currentProcess.getProcessName()+" , minBurst:"+highPriorityProcess.getProcessName());
                    removeFromReadyQueue(currentProcess);
                    readyQueue.add(currentProcess);
                    //System.out.println("Ready Queue in non-pre priority switch: "+readyQueue);
                    previousProcess=currentProcess;
                    currentProcess=highPriorityProcess;
                    isFCFS=false;
                    continue;
                }
                currentProcess=highPriorityProcess;
                    //The actual stuff 50%
                time-=(int) Math.ceil(((float)currentProcess.getQuantum())*0.25);
                currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime()+((int) Math.ceil(((float)currentProcess.getQuantum())*0.25)));
                startTime=time;
                time+=(int) Math.ceil(((float)currentProcess.getQuantum())*0.5);
                checkForArrivals(startTime,time);
                remainingQuantum=currentProcess.getQuantum()-((int) Math.ceil(((float)currentProcess.getQuantum())*0.5));
                //System.out.println("time: "+time+" ,"+currentProcess.getProcessName()+", Remaining Quantum (in 50%)= "+remainingQuantum);
                currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime()-((int) Math.ceil(((float)currentProcess.getQuantum())*0.5)));

                if(currentProcess.getRemainingBurstTime()<=0){
                    isFCFS=true;
                    processes.get(processes.indexOf(currentProcess)).setDone(true);
                    processes.get(processes.indexOf(currentProcess)).setQuantum(0);
                    processes.get(processes.indexOf(currentProcess)).setRemainingBurstTime(0);
                    //readyQueue.remove(currentProcess);
                    removeFromReadyQueue(currentProcess);
                    previousProcess=currentProcess;
                    exeOrder.add(currentProcess.getProcessName());
                    continue;
                }
                //Check SWITCH FROM SJF (In example, no one continues), just for now
                Process minBurstProcess = getProcessOfMinBurst(time);
                if(currentProcess.getProcessName()!=minBurstProcess.getProcessName()){
                    currentProcess.setQuantum((int) Math.ceil(((double) currentProcess.getQuantum()+ (double) remainingQuantum)));
                    //System.out.println("SJF switch: current:"+currentProcess.getProcessName()+" , minBurst:"+minBurstProcess.getProcessName());
                    removeFromReadyQueue(currentProcess);
                    readyQueue.add(currentProcess);
                    //System.out.println("Ready Queue in sjf switch: "+readyQueue);
                    previousProcess=currentProcess;
                    currentProcess=minBurstProcess;
                    isFCFS=false;
                    continue;
                }
                currentProcess=minBurstProcess;//same process so sjf it, time++ till remaining quantum==0
                previousProcess=new Process("End",null,null,null,null,null,null,null);
                isFCFS=false;

            }
        }
//        System.out.println("After execution");
//        for (int i = 0; i < processes.size(); i++) {
//            System.out.println(processes.get(i).toString());
//        }
        System.out.print("Execution order (=>):");
        for (int i = 0; i < exeOrder.size(); i++) {
            System.out.println(" "+exeOrder.get(i));
        }
        historyOfQuantumAllProcessesForPrint = new ArrayList<String>(new LinkedHashSet<String>(historyOfQuantumAllProcessesForPrint));
        for (int i = 0; i < historyOfQuantumAllProcessesForPrint.size(); i++) {
            historyOfQuantum=historyOfQuantumAllProcessesForPrint.get(i);
            System.out.println(historyOfQuantum);

        }
    }

    private void removeFromReadyQueue(Process currentProcess) {
        Queue<Process> cpRQ= new LinkedList<Process>(readyQueue);
        Queue<Process> updatedRQ= new LinkedList<Process>();
        while(!cpRQ.isEmpty()){
            if(cpRQ.peek().getProcessName()==currentProcess.getProcessName()){
                cpRQ.remove();
                continue;
            }
            updatedRQ.add(cpRQ.remove());
        }
        readyQueue =updatedRQ;
    }

    private boolean allAreProcessesAreDone() {
        boolean allDone=true;
        for (int i = 0; i < processes.size(); i++) {
            if(processes.get(i).getDone()==false){
                allDone=false;
                return allDone;
            }
        }
        return allDone;
    }

    private void checkForArrivals(Integer beforeTimeIncrement, Integer time) {
//        for (int i = beforeTimeIncrement; i <= time; i++) {
//            if(processes)
//            readyQueue.add(processes.get(i));
//        }
        for (int i = 0; i < processes.size(); i++) {
            if(processes.get(i).getProcessArrivalTime()>beforeTimeIncrement && processes.get(i).getProcessArrivalTime()<=time){
                readyQueue.add(processes.get(i));
            }
        }
    }

    private Process getHighestPriorityProcess(Integer time) {
        Process hp = new Process("",null,null,null,Integer.MAX_VALUE,null,null,null);
        for (int i = 0; i < processes.size(); i++) {
            if(processes.get(i).getDone()==false && processes.get(i).getProcessArrivalTime()<=time){
                if(processes.get(i).getProcessPriority()< hp.getProcessPriority()){
                    hp=processes.get(i);
                }
            }
        }
        //System.out.println("Highest priority: "+hp.getProcessName()+" ,priority:"+hp.getProcessPriority());
        return hp;
    }
    private Process getProcessOfMinBurst(Integer time){
        List<Process> notDoneProcesses = new ArrayList<Process>();
        for(int i=0;i< processes.size();i++){
            if(processes.get(i).getDone()==true){
                continue;
            }
            if(processes.get(i).getIsAt()<=time) {
                notDoneProcesses.add(processes.get(i));
            }
        }
        Util.sortAccordingToBurstTime(notDoneProcesses);
        if(notDoneProcesses.size()==0){return null;}
        //System.out.println("Min burst process: "+notDoneProcesses.get(0).getProcessName()+" ,remaining burst time:"+notDoneProcesses.get(0).getRemainingBurstTime());
        return notDoneProcesses.get(0);
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

            System.out.print("Process "+ (i+1) +" priority: ");
            process.setProcessPriority(scanner.nextInt());

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

        AGScheduler agScheduler = new AGScheduler(agProcesses);


    }
}