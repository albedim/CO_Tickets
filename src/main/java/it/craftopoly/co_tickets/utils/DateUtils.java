package it.craftopoly.co_tickets.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils
{
    public static String fixDate(String date)
    {
        String[] dates = date.split("-");
        return dates[2] + "/" + dates[1] + "/" + dates[0];
    }

    public static String formatDate(String inputDate) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = dateFormat.parse(inputDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Get the current date
            Date currentDate = new Date();

            // Calculate the time difference in milliseconds
            long timeDifferenceMillis = currentDate.getTime() - date.getTime();

            // Calculate days and weeks
            long daysDifference = timeDifferenceMillis / (24 * 60 * 60 * 1000);
            long weeksDifference = daysDifference / 7;

            if (daysDifference <= 0) {
                return "Oggi";
            } else if (daysDifference == 1) {
                return "Ieri";
            } else if (daysDifference <= 7) {
                return daysDifference + " giorni fa";
            } else if (weeksDifference == 1) {
                return weeksDifference + " settimana fa";
            } else if (weeksDifference <= 4) {
                return weeksDifference + " settimane fa";
            } else {
                return inputDate;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date format";
        }
    }
}
