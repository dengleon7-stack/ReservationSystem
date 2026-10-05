import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;


public class RoomSelectionPanel extends JPanel {

    private static final String[] ROOM_TYPES = {"Single", "Double", "Family", "Suite"};
    private static final double[] PRICES = {50.0, 80.0, 120.0, 200.0};

    private final JComboBox<String> roomCombo = new JComboBox<>(ROOM_TYPES);
    private final JSpinner roomsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
    private final JLabel priceLabel = new JLabel();

    public RoomSelectionPanel() {
        setLayout(new GridLayout(3, 2, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Room Selection"));

        add(new JLabel("Room type:"));
        add(roomCombo);
        add(new JLabel("No. of rooms:"));
        add(roomsSpinner);
        add(new JLabel("Price per night:"));
        add(priceLabel);

        roomCombo.addActionListener(e -> updatePriceLabel());
        updatePriceLabel();
    }

    private void updatePriceLabel() {
        priceLabel.setText(String.format("$%.2f", getPricePerNight()));
    }

    public String getRoomType() {
        return (String) roomCombo.getSelectedItem();
    }

    public int getNumberOfRooms() {
        return (Integer) roomsSpinner.getValue();
    }

    public double getPricePerNight() {
        return PRICES[roomCombo.getSelectedIndex()];
    }

    public void clear() {
        roomCombo.setSelectedIndex(0);
        roomsSpinner.setValue(1);
    }
}