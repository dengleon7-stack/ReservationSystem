import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class ReservationSummaryPanel extends JPanel {

    private final JTextArea summaryArea = new JTextArea();

    public ReservationSummaryPanel() {
        setLayout(new GridLayout(1, 1));
        setBorder(BorderFactory.createTitledBorder("Reservation Summary"));

        summaryArea.setEditable(false);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        add(new JScrollPane(summaryArea));
    }

    public void showSummary(String text) {
        summaryArea.setText(text);
        summaryArea.setCaretPosition(0);
    }

    public void clear() {
        summaryArea.setText("");
    }
}