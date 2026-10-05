import static org.junit.jupiter.api.Assertions.*;

import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JSpinner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BookingDatesPanel")
class BookingDatesPanelTest {

    private BookingDatesPanel panel;
    private JSpinner checkInSpinner;
    private JSpinner checkOutSpinner;
    private JLabel nightsLabel;

    @BeforeEach
    void setUp() {
        panel = new BookingDatesPanel();
        checkInSpinner = SwingTestUtils.find(panel, JSpinner.class, 0);
        checkOutSpinner = SwingTestUtils.find(panel, JSpinner.class, 1);
        nightsLabel = SwingTestUtils.find(panel, JLabel.class, 3); // 4th label = nights value
    }

    @AfterEach
    void tearDown() {
        panel.clear();
        panel = null;
    }

    private static Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Test
    @DisplayName("uses a 3 x 2 GridLayout")
    void usesGridLayout() {
        assertTrue(panel.getLayout() instanceof GridLayout);
        GridLayout layout = (GridLayout) panel.getLayout();
        assertEquals(3, layout.getRows());
        assertEquals(2, layout.getColumns());
    }

    @Test
    @DisplayName("defaults to today -> tomorrow (1 night)")
    void defaults() {
        assertAll(
                () -> assertEquals(LocalDate.now(), panel.getCheckIn()),
                () -> assertEquals(LocalDate.now().plusDays(1), panel.getCheckOut()),
                () -> assertEquals(1, panel.getNights()),
                () -> assertEquals("1", nightsLabel.getText()),
                () -> assertNull(panel.getValidationError()));
    }

    @Test
    @DisplayName("nights are calculated from the two dates")
    void calculatesNights() {
        checkOutSpinner.setValue(toDate(LocalDate.now().plusDays(5)));

        assertAll(
                () -> assertEquals(5, panel.getNights()),
                () -> assertEquals("5", nightsLabel.getText()));
    }

    @Test
    @DisplayName("check-out on the same day as check-in is invalid")
    void sameDayIsInvalid() {
        checkOutSpinner.setValue(toDate(LocalDate.now()));

        assertAll(
                () -> assertEquals(0, panel.getNights()),
                () -> assertEquals("-", nightsLabel.getText()),
                () -> assertEquals("Check-out must be after the check-in date.", panel.getValidationError()));
    }

    @Test
    @DisplayName("check-out before check-in is invalid")
    void checkOutBeforeCheckInIsInvalid() {
        checkOutSpinner.setValue(toDate(LocalDate.now().minusDays(2)));

        assertAll(
                () -> assertTrue(panel.getNights() < 0),
                () -> assertEquals("-", nightsLabel.getText()),
                () -> assertEquals("Check-out must be after the check-in date.", panel.getValidationError()));
    }

    @Test
    @DisplayName("check-in in the past is invalid")
    void pastCheckInIsInvalid() {
        checkInSpinner.setValue(toDate(LocalDate.now().minusDays(1)));
        checkOutSpinner.setValue(toDate(LocalDate.now().plusDays(1)));

        assertEquals("Check-in date cannot be in the past.", panel.getValidationError());
    }

    @Test
    @DisplayName("a future stay is valid")
    void futureStayIsValid() {
        checkInSpinner.setValue(toDate(LocalDate.now().plusDays(10)));
        checkOutSpinner.setValue(toDate(LocalDate.now().plusDays(14)));

        assertAll(
                () -> assertEquals(4, panel.getNights()),
                () -> assertNull(panel.getValidationError()));
    }

    @Test
    @DisplayName("clear goes back to today -> tomorrow")
    void clearResets() {
        checkInSpinner.setValue(toDate(LocalDate.now().plusDays(10)));
        checkOutSpinner.setValue(toDate(LocalDate.now().plusDays(20)));

        panel.clear();
        assertAll(
                () -> assertEquals(LocalDate.now(), panel.getCheckIn()),
                () -> assertEquals(LocalDate.now().plusDays(1), panel.getCheckOut()),
                () -> assertEquals(1, panel.getNights()));
    }
}