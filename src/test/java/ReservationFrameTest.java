import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JFrame cannot be created on a machine with no display, so these tests are
 * skipped automatically in that case (for example on some build servers).
 *
 * Only the VALID path is clicked here: an invalid submit opens a JOptionPane
 * which would block the test waiting for someone to press OK.
 */
@DisplayName("ReservationFrame")
class ReservationFrameTest {

    private Locale originalLocale;
    private ReservationFrame frame;
    private GuestInfoPanel guestPanel;
    private RoomSelectionPanel roomPanel;
    private BookingDatesPanel datesPanel;
    private PaymentPanel paymentPanel;
    private ReservationSummaryPanel summaryPanel;

    @BeforeEach
    void setUp() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "No display available - skipping frame tests");

        originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);

        frame = new ReservationFrame();
        guestPanel = SwingTestUtils.find(frame.getContentPane(), GuestInfoPanel.class, 0);
        roomPanel = SwingTestUtils.find(frame.getContentPane(), RoomSelectionPanel.class, 0);
        datesPanel = SwingTestUtils.find(frame.getContentPane(), BookingDatesPanel.class, 0);
        paymentPanel = SwingTestUtils.find(frame.getContentPane(), PaymentPanel.class, 0);
        summaryPanel = SwingTestUtils.find(frame.getContentPane(), ReservationSummaryPanel.class, 0);
    }

    @AfterEach
    void tearDown() {
        if (frame != null) {
            frame.dispose();
            frame = null;
        }
        if (originalLocale != null) {
            Locale.setDefault(originalLocale);
        }
    }

    private void fillValidGuest() {
        SwingTestUtils.find(guestPanel, JTextField.class, 0).setText("Jane Doe");
        SwingTestUtils.find(guestPanel, JTextField.class, 1).setText("0700123456");
    }

    private String summaryText() {
        return SwingTestUtils.find(summaryPanel, JTextArea.class, 0).getText();
    }

    private void clickConfirm() {
        SwingTestUtils.findButton(frame.getContentPane(), "Confirm Reservation").doClick();
    }

    @Test
    @DisplayName("has the right title and closes the app on exit")
    void titleAndCloseOperation() {
        assertAll(
                () -> assertEquals("Hotel Reservation System", frame.getTitle()),
                () -> assertEquals(JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation()));
    }

    @Test
    @DisplayName("arranges the panels in a 3 x 2 GridLayout")
    void usesGridLayout() {
        assertTrue(frame.getContentPane().getLayout() instanceof GridLayout);
        GridLayout layout = (GridLayout) frame.getContentPane().getLayout();

        assertAll(
                () -> assertEquals(3, layout.getRows()),
                () -> assertEquals(2, layout.getColumns()),
                () -> assertEquals(6, frame.getContentPane().getComponentCount()));
    }

    @Test
    @DisplayName("has Confirm and Clear buttons")
    void hasButtons() {
        assertAll(
                () -> assertNotNull(SwingTestUtils.findButton(frame.getContentPane(), "Confirm Reservation")),
                () -> assertNotNull(SwingTestUtils.findButton(frame.getContentPane(), "Clear")));
    }

    @Test
    @DisplayName("summary is empty before confirming")
    void summaryStartsEmpty() {
        assertEquals("", summaryText());
    }

    @Test
    @DisplayName("confirm shows the reservation summary and total")
    void confirmShowsSummary() {
        fillValidGuest();
        clickConfirm();

        String summary = summaryText();
        assertAll(
                () -> assertTrue(summary.contains("Guest: Jane Doe")),
                () -> assertTrue(summary.contains("Phone: 0700123456")),
                () -> assertTrue(summary.contains("Room: 1 x Single")),
                () -> assertTrue(summary.contains("Nights: 1")),
                () -> assertTrue(summary.contains("Payment: Cash")),
                () -> assertTrue(summary.contains("TOTAL: $50.00")));
    }

    @Test
    @DisplayName("total = price x rooms x nights")
    void totalIsCalculatedCorrectly() {
        fillValidGuest();
        JComboBox<?> roomCombo = SwingTestUtils.find(roomPanel, JComboBox.class, 0);
        roomCombo.setSelectedItem("Suite");
        SwingTestUtils.find(roomPanel, JSpinner.class, 0).setValue(2);
        JSpinner checkOut = SwingTestUtils.find(datesPanel, JSpinner.class, 1);
        checkOut.setValue(Date.from(LocalDate.now().plusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        clickConfirm();

        // 200 per night x 2 rooms x 3 nights
        assertTrue(summaryText().contains("TOTAL: $1200.00"));
    }

    @Test
    @DisplayName("Clear resets every panel and empties the summary")
    void clearResetsEverything() {
        fillValidGuest();
        clickConfirm();
        assertFalse(summaryText().isEmpty());

        SwingTestUtils.findButton(frame.getContentPane(), "Clear").doClick();

        assertAll(
                () -> assertEquals("", summaryText()),
                () -> assertEquals("", guestPanel.getGuestName()),
                () -> assertEquals("", guestPanel.getPhone()),
                () -> assertEquals("Single", roomPanel.getRoomType()),
                () -> assertEquals("Cash", paymentPanel.getPaymentMethod()));
    }
}