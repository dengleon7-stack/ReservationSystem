import static org.junit.jupiter.api.Assertions.*;

import java.awt.GridLayout;
import java.util.List;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("GuestInfoPanel")
class GuestInfoPanelTest {

    private GuestInfoPanel panel;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JSpinner guestsSpinner;

    @BeforeEach
    void setUp() {
        panel = new GuestInfoPanel();
        List<JTextField> fields = SwingTestUtils.findAll(panel, JTextField.class);
        nameField = fields.get(0);
        phoneField = fields.get(1);
        emailField = fields.get(2);
        guestsSpinner = SwingTestUtils.find(panel, JSpinner.class, 0);
    }

    @AfterEach
    void tearDown() {
        panel.clear();
        panel = null;
    }

    @Test
    @DisplayName("uses a 4 x 2 GridLayout")
    void usesGridLayout() {
        assertTrue(panel.getLayout() instanceof GridLayout);
        GridLayout layout = (GridLayout) panel.getLayout();
        assertEquals(4, layout.getRows());
        assertEquals(2, layout.getColumns());
    }

    @Test
    @DisplayName("starts empty with 1 guest")
    void startsWithDefaults() {
        assertAll(
                () -> assertEquals("", panel.getGuestName()),
                () -> assertEquals("", panel.getPhone()),
                () -> assertEquals("", panel.getEmail()),
                () -> assertEquals(1, panel.getNumberOfGuests()));
    }

    @Test
    @DisplayName("getters return the typed values, trimmed")
    void gettersReturnTrimmedValues() {
        nameField.setText("  Jane Doe ");
        phoneField.setText(" 0700123456 ");
        emailField.setText(" jane@example.com ");
        guestsSpinner.setValue(3);

        assertAll(
                () -> assertEquals("Jane Doe", panel.getGuestName()),
                () -> assertEquals("0700123456", panel.getPhone()),
                () -> assertEquals("jane@example.com", panel.getEmail()),
                () -> assertEquals(3, panel.getNumberOfGuests()));
    }

    @Test
    @DisplayName("validation fails when name is empty")
    void errorWhenNameEmpty() {
        phoneField.setText("0700123456");

        assertEquals("Please enter the guest's full name.", panel.getValidationError());
    }

    @Test
    @DisplayName("validation fails when name is only spaces")
    void errorWhenNameBlank() {
        nameField.setText("    ");
        phoneField.setText("0700123456");

        assertEquals("Please enter the guest's full name.", panel.getValidationError());
    }

    @Test
    @DisplayName("validation fails when phone is empty")
    void errorWhenPhoneEmpty() {
        nameField.setText("Jane Doe");

        assertEquals("Please enter a phone number.", panel.getValidationError());
    }

    @Test
    @DisplayName("validation passes without an email (email is optional)")
    void noErrorWhenValid() {
        nameField.setText("Jane Doe");
        phoneField.setText("0700123456");

        assertNull(panel.getValidationError());
    }

    @Test
    @DisplayName("clear resets every field")
    void clearResetsFields() {
        nameField.setText("Jane Doe");
        phoneField.setText("0700123456");
        emailField.setText("jane@example.com");
        guestsSpinner.setValue(5);

        panel.clear();

        assertAll(
                () -> assertEquals("", panel.getGuestName()),
                () -> assertEquals("", panel.getPhone()),
                () -> assertEquals("", panel.getEmail()),
                () -> assertEquals(1, panel.getNumberOfGuests()));
    }
}