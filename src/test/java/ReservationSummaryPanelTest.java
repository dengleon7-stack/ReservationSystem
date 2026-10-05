import static org.junit.jupiter.api.Assertions.*;

import java.awt.GridLayout;
import javax.swing.JTextArea;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ReservationSummaryPanel")
class ReservationSummaryPanelTest {

    private ReservationSummaryPanel panel;
    private JTextArea summaryArea;

    @BeforeEach
    void setUp() {
        panel = new ReservationSummaryPanel();
        summaryArea = SwingTestUtils.find(panel, JTextArea.class, 0);
    }

    @AfterEach
    void tearDown() {
        panel.clear();
        panel = null;
    }

    @Test
    @DisplayName("uses a 1 x 1 GridLayout")
    void usesGridLayout() {
        assertTrue(panel.getLayout() instanceof GridLayout);
        GridLayout layout = (GridLayout) panel.getLayout();
        assertEquals(1, layout.getRows());
        assertEquals(1, layout.getColumns());
    }

    @Test
    @DisplayName("starts empty and read-only")
    void startsEmptyAndReadOnly() {
        assertAll(
                () -> assertEquals("", summaryArea.getText()),
                () -> assertFalse(summaryArea.isEditable()));
    }

    @Test
    @DisplayName("showSummary displays the text and scrolls to the top")
    void showSummaryDisplaysText() {
        panel.showSummary("Guest: Jane Doe\nTOTAL: $50.00");

        assertAll(
                () -> assertEquals("Guest: Jane Doe\nTOTAL: $50.00", summaryArea.getText()),
                () -> assertEquals(0, summaryArea.getCaretPosition()));
    }

    @Test
    @DisplayName("showSummary replaces the previous text")
    void showSummaryReplacesText() {
        panel.showSummary("first");
        panel.showSummary("second");

        assertEquals("second", summaryArea.getText());
    }

    @Test
    @DisplayName("clear empties the summary")
    void clearEmpties() {
        panel.showSummary("something");

        panel.clear();

        assertEquals("", summaryArea.getText());
    }
}