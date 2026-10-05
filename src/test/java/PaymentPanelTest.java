import static org.junit.jupiter.api.Assertions.*;

import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("PaymentPanel")
class PaymentPanelTest {

    private PaymentPanel panel;
    private JComboBox<?> methodCombo;
    private JTextField referenceField;
    private JLabel referenceLabel;

    @BeforeEach
    void setUp() {
        panel = new PaymentPanel();
        methodCombo = SwingTestUtils.find(panel, JComboBox.class, 0);
        referenceField = SwingTestUtils.find(panel, JTextField.class, 0);
        referenceLabel = SwingTestUtils.find(panel, JLabel.class, 1); // 2nd label changes with the method
    }

    @AfterEach
    void tearDown() {
        panel.clear();
        panel = null;
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
    @DisplayName("defaults to Cash with the reference field disabled")
    void defaultsToCash() {
        assertAll(
                () -> assertEquals("Cash", panel.getPaymentMethod()),
                () -> assertFalse(referenceField.isEnabled()),
                () -> assertEquals("Reference no.:", referenceLabel.getText()),
                () -> assertNull(panel.getValidationError()));
    }

    @ParameterizedTest
    @CsvSource({
            "Mobile Money, 'Phone number:'",
            "Credit Card, 'Card number:'"
    })
    @DisplayName("non-cash methods enable the field and change its label")
    void nonCashEnablesField(String method, String expectedLabel) {
        methodCombo.setSelectedItem(method);

        assertAll(
                () -> assertTrue(referenceField.isEnabled()),
                () -> assertEquals(expectedLabel, referenceLabel.getText()));
    }

    @ParameterizedTest
    @CsvSource({
            "Mobile Money, 'Please fill in the phone number.'",
            "Credit Card, 'Please fill in the card number.'"
    })
    @DisplayName("non-cash methods need a reference")
    void nonCashNeedsReference(String method, String expectedError) {
        methodCombo.setSelectedItem(method);

        assertEquals(expectedError, panel.getValidationError());
    }

    @Test
    @DisplayName("validation passes once a reference is entered")
    void validWithReference() {
        methodCombo.setSelectedItem("Credit Card");
        referenceField.setText("4111111111111111");

        assertNull(panel.getValidationError());
    }

    @Test
    @DisplayName("getReference trims whitespace")
    void referenceIsTrimmed() {
        methodCombo.setSelectedItem("Mobile Money");
        referenceField.setText("  0700123456 ");

        assertEquals("0700123456", panel.getReference());
    }

    @Test
    @DisplayName("switching back to Cash clears and disables the field")
    void switchingBackToCash() {
        methodCombo.setSelectedItem("Mobile Money");
        referenceField.setText("0700123456");

        methodCombo.setSelectedItem("Cash");

        assertAll(
                () -> assertEquals("", panel.getReference()),
                () -> assertFalse(referenceField.isEnabled()),
                () -> assertNull(panel.getValidationError()));
    }

    @Test
    @DisplayName("clear goes back to Cash with an empty reference")
    void clearResets() {
        methodCombo.setSelectedItem("Credit Card");
        referenceField.setText("1234");

        panel.clear();

        assertAll(
                () -> assertEquals("Cash", panel.getPaymentMethod()),
                () -> assertEquals("", panel.getReference()));
    }
}