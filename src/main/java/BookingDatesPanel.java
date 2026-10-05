import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;


public class BookingDatesPanel extends JPanel {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private final JSpinner checkInSpinner = createDateSpinner(0);
    private final JSpinner checkOutSpinner = createDateSpinner(1);
    private final JLabel nightsLabel = new JLabel();

    public BookingDatesPanel() {
        setLayout(new GridLayout(3, 2, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Booking Dates"));

        add(new JLabel("Check-in:"));
        add(checkInSpinner);
        add(new JLabel("Check-out:"));
        add(checkOutSpinner);
        add(new JLabel("Nights:"));
        add(nightsLabel);

        checkInSpinner.addChangeListener(e -> updateNightsLabel());
        checkOutSpinner.addChangeListener(e -> updateNightsLabel());
        updateNightsLabel();
    }

    private static JSpinner createDateSpinner(int daysFromToday) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, daysFromToday);
        JSpinner spinner = new JSpinner(new SpinnerDateModel(cal.getTime(), null, null, Calendar.DAY_OF_MONTH));
        spinner.setEditor(new JSpinner.DateEditor(spinner, DATE_FORMAT));
        return spinner;
    }

    private static LocalDate toLocalDate(JSpinner spinner) {
        Date date = (Date) spinner.getValue();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void updateNightsLabel() {
        long nights = getNights();
        nightsLabel.setText(nights > 0 ? String.valueOf(nights) : "-");
    }

    public LocalDate getCheckIn() {
        return toLocalDate(checkInSpinner);
    }

    public LocalDate getCheckOut() {
        return toLocalDate(checkOutSpinner);
    }

    public long getNights() {
        return ChronoUnit.DAYS.between(getCheckIn(), getCheckOut());
    }


    public String getValidationError() {
        if (getCheckIn().isBefore(LocalDate.now())) {
            return "Check-in date cannot be in the past.";
        }
        if (getNights() <= 0) {
            return "Check-out must be after the check-in date.";
        }
        return null;
    }

    public void clear() {
        Calendar cal = Calendar.getInstance();
        checkInSpinner.setValue(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        checkOutSpinner.setValue(cal.getTime());
    }
}