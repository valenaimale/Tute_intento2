package Vista.VistaGrafica;
import javax.swing.*;
import java.awt.event.ActionListener;

public class TimerUnico {
    private Timer timer;

    public TimerUnico(int delay) {
        timer = new Timer(delay, null);
        timer.setRepeats(false);
    }

    public void iniciar(Runnable accion) {
        // cancela la cuenta anterior (si había una)
        timer.stop();
        for (ActionListener al : timer.getActionListeners()) {
            timer.removeActionListener(al);
        }
        timer.addActionListener(e -> accion.run());
        timer.start();
        System.out.println("timer iniciado en clase timer. Accion:"+accion);
    }

    public void interrumpir() {
        timer.stop();
    }
}


