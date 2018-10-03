package objavi.samo.android.studentskiposlovi.utils;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

public class DateAndTime {

    public static String currentTime(){
        ZoneId zoneId = ZoneId.of("Europe/Paris");
        LocalDateTime localTime = LocalDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy - HH:mm");
        return localTime.format(formatter);
    }
}
