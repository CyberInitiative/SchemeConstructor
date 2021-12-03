package models;

/**
 *
 * @author Miroslav Levdikov
 */
public class MyThread implements Runnable {

    private String parametr;
    private String secondParametr;

    public MyThread(){
        
    }
    
    public MyThread(String parametr, String secondParametr) {
        this.parametr = parametr;
        this.secondParametr = secondParametr;
    }

    public void setParametr(String parametr) {
        this.parametr = parametr;
    }

    public void setSecondParametr(String secondParametr) {
        this.secondParametr = secondParametr;
    }
       
    @Override
    public void run() {
        System.out.println(parametr);
        System.out.println(secondParametr);
    }

}
