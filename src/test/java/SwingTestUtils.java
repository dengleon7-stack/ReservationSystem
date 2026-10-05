import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSpinner;

/**
 * Helper for the tests. The panels keep their components private, so the
 * tests find them by walking the component tree (in the order they were added).
 * It does not look inside JSpinner / JComboBox, so their internal text fields
 * and arrow buttons are not mixed up with the ones we care about.
 */
final class SwingTestUtils {

    private SwingTestUtils() {
    }

    static <T extends Component> List<T> findAll(Container root, Class<T> type) {
        List<T> found = new ArrayList<>();
        collect(root, type, found);
        return found;
    }

    static <T extends Component> T find(Container root, Class<T> type, int index) {
        return findAll(root, type).get(index);
    }

    static JButton findButton(Container root, String text) {
        for (JButton button : findAll(root, JButton.class)) {
            if (text.equals(button.getText())) {
                return button;
            }
        }
        throw new AssertionError("No button with text: " + text);
    }

    private static <T extends Component> void collect(Container container, Class<T> type, List<T> found) {
        for (Component child : container.getComponents()) {
            if (type.isInstance(child)) {
                found.add(type.cast(child));
            }
            boolean opaqueWidget = child instanceof JSpinner || child instanceof JComboBox;
            if (child instanceof Container && !opaqueWidget) {
                collect((Container) child, type, found);
            }
        }
    }
}