import java.awt.*;
import javax.swing.JTextField;

class Task extends Thread {

    JTextField tf;  // Textfield to be used by this task

    public Task(JTextField t) {
        tf = t;
    }

    void setMyText(final String s) {
        /*
         *  Checks if called by GUI thread (= dispatch thread) or not.
         *  Although using invokeLater is always sound, calling setText directly
         *  is more efficient when called by the GUI thread.
         */
        if (EventQueue.isDispatchThread()) {
            // On GUI thread -- OK to set text directly
            tf.setText(s);
        } else {
            // not on GUI thread -- put update request on event queue
            EventQueue.invokeLater( () -> { tf.setText(s); } );
        }
    }
        public void run () {
            int cols = tf.getColumns();

            boolean useCPU = false;   // Set true to consume CPU-cycles

            int basespeed = 5000;  // millisecs to do task
            int variation = (useCPU ? 0 : 60);  // Speed variation in percent

            long delay = Math.round(((Math.random() - 0.5) * variation / 50 + 1.0) * basespeed / cols);

            String s = "";

            setMyText(s);
            while (s.length() < cols) {

                if (useCPU) {
                    for (int j = 0; j < 1000000 * delay; j++) {
                    }
                } else {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                    }
                }

                s = s + "#";
                setMyText(s);
            }
        }
}

public class TaskControl {

    static final int N = 6;   // Number of Textfields

    static int h = 0;         // Number of 'hello'-s

    public static void main(String[] argv) {
        try {

            // Create window with N JTextFields: (d.tf[0], ... , d.tf[N-1])
            TaskDisplay d = new TaskDisplay("Task Control", N);
            Task[] taskArray = new Task[6];
            d.println("Type command (x to exit):");

            // Main command interpretation loop
            W: while (true) {

                char c = d.getKey();

                switch (c) {

                    case 'x':
                        break W;

                    case 'h':
                        d.println("Hello " + (h++));
                        break;

                    case 't':
                        for (int i = 0; i < 6; i++) {

                            if(taskArray[i] == null || !taskArray[i].isAlive()) {
                                d.println("Runs a task");
                                taskArray[i] = new Task(d.tf[i]);
                                taskArray[i].start();
                                break;
                            }
                            else {
                                if (i < 5) {
                                    d.println("Task " + (i + 1) + " is occupied trying next one");
                                } else {
                                    d.println("All task occupied pls wait");
                                }
                            }
                        }
/*                        if(!task0.isAlive()) {
                            d.println("Runs a task");
                            task0 = new Task(d.tf[0]);
                            task0.start();
                        } else {
                            d.println("Sorry the task is currently occupied!");
                        }*/
                        break;

                    default:
                        d.println("Don't know '" + c + "'");
                }
            }
/*            task[i].join();
            if (task0.isAlive()) {
                d.println("Program terminates after process is finished");
            } else {
                d.println("Program terminates");
            }*/
//            d.println("Program terminates");
            Thread.sleep(500);
            System.exit(0);

        } catch (Exception e) {System.out.println("Exception in Task Control: " + e); e.printStackTrace(); }
    }
}
