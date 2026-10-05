import static org.junit.jupiter.api.Assertions.*;

import java.awt.GridLayout;
import java.util.Locale;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("RoomSelectionPanel")
class RoomSelectionPanelTest {

    private Locale originalLocale;
    private RoomSelectionPanel panel;
    private JComboBox<?> roomCombo;
    private JSpinner roomsSpinner;
    private JLabel priceLabel;

    @BeforeEach
    void setUp() {
        // price is formatted with String.format, so fix the locale for stable "$50.00" text
        originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);

        panel = new RoomSelectionPanel();
        roomCombo = SwingTestUtils.find(panel, JComboBox.class, 0);
        roomsSpinner = SwingTestUtils.find(panel, JSpinner.class, 0);
        priceLabel = SwingTestUtils.find(panel, JLabel.class, 3); // 4th label = the price value
    }

    @AfterEach
    void tearDown() {
        panel = null;
        Locale.setDefault(originalLocale);
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
    @DisplayName("defaults to 1 Single room at $50.00")
    void defaults() {
        assertAll(
                () -> assertEquals("Single", panel.getRoomType()),
                () -> assertEquals(1, panel.getNumberOfRooms()),
                () -> assertEquals(50.0, panel.getPricePerNight(), 0.001),
                () -> assertEquals("$50.00", priceLabel.getText()));
    }

    @ParameterizedTest
    @CsvSource({
            "0, Single, 50.0",
            "1, Double, 80.0",
            "2, Family, 120.0",
            "3, Suite, 200.0"
    })
    @DisplayName("each room type has the right price")
    void roomTypeAndPrice(int index, String type, double price) {
        roomCombo.setSelectedIndex(index);

        assertAll(
                () -> assertEquals(type, panel.getRoomType()),
                () -> assertEquals(price, panel.getPricePerNight(), 0.001));
    }

    @Test
    @DisplayName("price label updates when the room type changes")
    void priceLabelUpdates() {
        roomCombo.setSelectedItem("Suite");

        assertEquals("$200.00", priceLabel.getText());
    }

    @Test
    @DisplayName("number of rooms follows the spinner")
    void numberOfRooms() {
        roomsSpinner.setValue(4);

        assertEquals(4, panel.getNumberOfRooms());
    }

    @Test
    @DisplayName("clear goes back to 1 Single room")
    void clearResets() {
        roomCombo.setSelectedItem("Family");
        roomsSpinner.setValue(3);

        panel.clear();

        assertAll(
                () -> assertEquals("Single", panel.getRoomType()),
                () -> assertEquals(1, panel.getNumberOfRooms()),
                () -> assertEquals("$50.00", priceLabel.getText()));
    }
}