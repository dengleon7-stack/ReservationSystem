import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



public class ReservationFrame extends JFrame {

    private final GuestInfoPanel guestPanel = new GuestInfoPanel();
    private final RoomSelectionPanel roomPanel = new RoomSelectionPanel();
    private final BookingDatesPanel datesPanel = new BookingDatesPanel();
    private final PaymentPanel paymentPanel = new PaymentPanel();
    private final ReservationSummaryPanel summaryPanel = new ReservationSummaryPanel();

    public ReservationFrame() {
        super("Hotel Reservation System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridLayout(3, 2, 10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(guestPanel);
        add(roomPanel);
        add(datesPanel);
        add(paymentPanel);
        add(summaryPanel);
        add(createButtonPanel());

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private JPanel createButtonPanel() {
        JButton confirmButton = new JButton("Confirm Reservation");
        JButton clearButton = new JButton("Clear");

        confirmButton.addActionListener(e -> confirmReservation());
        clearButton.addActionListener(e -> clearAll());

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 60));
        panel.add(confirmButton);
        panel.add(clearButton);
        return panel;
    }

    private void confirmReservation() {
        String error = firstError();
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long nights = datesPanel.getNights();
        double total = roomPanel.getPricePerNight() * roomPanel.getNumberOfRooms() * nights;

        StringBuilder sb = new StringBuilder();
        sb.append("Guest: ").append(guestPanel.getGuestName()).append("\n");
        sb.append("Phone: ").append(guestPanel.getPhone()).append("\n");
        if (!guestPanel.getEmail().isEmpty()) {
            sb.append("Email: ").append(guestPanel.getEmail()).append("\n");
        }
        sb.append("Guests: ").append(guestPanel.getNumberOfGuests()).append("\n\n");
        sb.append("Room: ").append(roomPanel.getNumberOfRooms()).append(" x ")
                .append(roomPanel.getRoomType()).append("\n");
        sb.append("Check-in: ").append(datesPanel.getCheckIn()).append("\n");
        sb.append("Check-out: ").append(datesPanel.getCheckOut()).append("\n");
        sb.append("Nights: ").append(nights).append("\n\n");
        sb.append("Payment: ").append(paymentPanel.getPaymentMethod()).append("\n");
        sb.append(String.format("TOTAL: $%.2f", total));

        summaryPanel.showSummary(sb.toString());
    }

    private String firstError() {
        String[] errors = {
                guestPanel.getValidationError(),
                datesPanel.getValidationError(),
                paymentPanel.getValidationError()
        };
        for (String err : errors) {
            if (err != null) {
                return err;
            }
        }
        return null;
    }

    private void clearAll() {
        guestPanel.clear();
        roomPanel.clear();
        datesPanel.clear();
        paymentPanel.clear();
        summaryPanel.clear();
    }
}