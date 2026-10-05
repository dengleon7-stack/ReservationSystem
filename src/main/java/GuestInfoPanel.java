import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/** Panel for the guest's personal details. */
public class GuestInfoPanel extends JPanel {

    private final JTextField nameField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JSpinner guestsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

    public GuestInfoPanel() {
        setLayout(new GridLayout(4, 2, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Guest Information"));

        add(new JLabel("Full name:"));
        add(nameField);
        add(new JLabel("Phone:"));
        add(phoneField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("No. of guests:"));
        add(guestsSpinner);
    }

    public String getGuestName() {
        return nameField.getText().trim();
    }

    public String getPhone() {
        return phoneField.getText().trim();
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public int getNumberOfGuests() {
        return (Integer) guestsSpinner.getValue();
    }

    /** Returns an error message, or null if the input is fine. */
    public String getValidationError() {
        if (getGuestName().isEmpty()) {
            return "Please enter the guest's full name.";
        }
        if (getPhone().isEmpty()) {
            return "Please enter a phone number.";
        }
        return null;
    }

    public void clear() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        guestsSpinner.setValue(1);
    }
}