import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.TimerTask;
import java.lang.Math;

class Info{
    float delay;
    Color color;

    Info(){
        color = new Color(Simulation.random.nextInt(256),
                0, Simulation.random.nextInt(256));
    }

}

public class Simulation{
    static Random random = new Random();

    static Queue mainQueue = new Queue(1);
    static Queue slowQueue = new Queue(4);
    static Queue normalQueue = new Queue(2);
    static Queue fastQueue = new Queue(1);

    public static int min(){
        int normal, slow, fast;
        normal = normalQueue.processTime;
        slow = slowQueue.processTime;
        fast = fastQueue.processTime;
        System.out.printf("%d %d %d\n", slow, normal, fast);
        if (fast <= normal && fast <= slow) return 1;
        else if (normal <= fast && normal <= slow) return 0;
        else return -1;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Load Balancing Simulation") {
            @Override
            public void paint(Graphics g) {
                g.drawRect(387, 90, 240, 300); // main Queue
                g.drawRect(387, 430, 240, 300); // normal Queue
                g.drawRect(75, 430, 240, 300); // slow Queue
                g.drawRect(702, 430, 240, 300); // fast Queue

                int i = 0;
                for (Info info : mainQueue){
                    g.setColor(info.color);
                    g.fillRect(387, 390 - ++i * 300 / mainQueue.size(), 240,
                            300 / mainQueue.size());
                }

                i = 0;
                for (Info info : normalQueue){
                    g.setColor(info.color);
                    g.fillRect(387, 730 - (++i * 300 / normalQueue.size()), 240,
                            300 / normalQueue.size());
                }

                i = 0;
                for (Info info : slowQueue){
                    g.setColor(info.color);
                    g.fillRect(75, 730 - (++i * 300 / slowQueue.size()), 240,
                            300 / slowQueue.size());
                }

                i = 0;
                for (Info info : fastQueue){
                    g.setColor(info.color);
                    g.fillRect(702, 730 - (++i * 300 / fastQueue.size()), 240,
                            300 / fastQueue.size());
                }

                g.setColor(Color.WHITE);
                if (mainQueue.isEmpty()){
                    g.fillRect(388, 91, 239, 299);
                }
                if (normalQueue.isEmpty()){
                    g.fillRect(388, 431, 239, 299);
                }
                if (slowQueue.isEmpty()){
                    g.fillRect(76, 431, 239, 299);
                }
                if (fastQueue.isEmpty()){
                    g.fillRect(703, 431, 239, 299);
                }
            }
        };

        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new Loop(frame), 0, 1000);

        JTextField requestTime = new JTextField();
        requestTime.setBounds(380, 10, 120, 20);
        frame.add(requestTime);

        JButton newRequest = new JButton("New Request");
        newRequest.setBounds(500, 10, 120, 20);
        newRequest.addActionListener(e -> {
            Info info = new Info();
            info.delay = Integer.parseInt(requestTime.getText());
            mainQueue.enqueue(info);
            frame.repaint();
        });
        frame.add(newRequest);

        JButton randomRequest = new JButton("Random Request");
        randomRequest.setBounds(380, 35, 120, 20);
        randomRequest.addActionListener(e -> {
            Info info = new Info();
            info.delay = random.nextInt(5) + 3;
            mainQueue.enqueue(info);
            frame.repaint();
        });
        frame.add(randomRequest);

        JButton toggleRequests = new JButton("Toggle Requests");
        toggleRequests.setBounds(500, 35, 120, 20);
        toggleRequests.addActionListener(e -> {

        });
        frame.add(toggleRequests);

        frame.setSize(1000, 800);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}

class Loop extends TimerTask {
    JFrame frame;

    Loop(JFrame _frame) {
        frame = _frame;
    }

    public void run() {
        Info info = Simulation.mainQueue.dequeue();
        if (info != null) {
            int n = Simulation.min();
            if (n == 1) Simulation.fastQueue.enqueue(info);
            else if (n == -1) Simulation.slowQueue.enqueue(info);
            else Simulation.normalQueue.enqueue(info);
        }
        info = Simulation.normalQueue.peek();
        if (info != null) {
            info.color = new Color(0, 255 / (int)info.delay, 0);
            info.delay -= 1;
            if (info.delay == 0) {
                Simulation.normalQueue.dequeue();
            }
        }
        info = Simulation.slowQueue.peek();
        if (info != null) {
            info.color = new Color(0, 255 / (int)Math.ceil(info.delay), 0);
            info.delay -= 0.5;
            if (info.delay <= 0) {
                Simulation.slowQueue.dequeue();
            }
        }
        info = Simulation.fastQueue.peek();
        if (info != null) {
            info.color = new Color(0, 255 / (int)info.delay, 0);
            info.delay -= 2;
            if (info.delay <= 0) {
                Simulation.fastQueue.dequeue();
            }
        }
        frame.repaint();
    }
}
