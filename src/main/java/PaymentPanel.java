import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class PaymentPanel extends JPanel {

    private static final String[] METHODS = {"Cash", "Mobile Money", "Credit Card"};

    private final JComboBox<String> methodCombo = new JComboBox<>(METHODS);
    private final JTextField referenceField = new JTextField();
    private final JLabel referenceLabel = new JLabel();

    public PaymentPanel() {
        setLayout(new GridLayout(3, 2, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Payment Details"));

        add(new JLabel("Payment method:"));
        add(methodCombo);
        add(referenceLabel);
        add(referenceField);

        methodCombo.addActionListener(e -> updateReferenceState());
        updateReferenceState();
    }

    private boolean isCash() {
        return "Cash".equals(getPaymentMethod());
    }

    private void updateReferenceState() {
        referenceField.setEnabled(!isCash());
        if (isCash()) {
            referenceField.setText("");
            referenceLabel.setText("Reference no.:");
        } else if ("Mobile Money".equals(getPaymentMethod())) {
            referenceLabel.setText("Phone number:");
        } else {
            referenceLabel.setText("Card number:");
        }
    }

    public String getPaymentMethod() {
        return (String) methodCombo.getSelectedItem();
    }

    public String getReference() {
        return referenceField.getText().trim();
    }


    public String getValidationError() {
        if (!isCash() && getReference().isEmpty()) {
            return "Please fill in the " + referenceLabel.getText().replace(":", "").toLowerCase() + ".";
        }
        return null;
    }

    public void clear() {
        methodCombo.setSelectedIndex(0);
        referenceField.setText("");
    }
}