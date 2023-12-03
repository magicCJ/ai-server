package com.zhibai.ai.util;


import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author xunbai
 * @Date 2023-05-21 15:01
 * @description
 **/
public class DateUtils {

    private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy_MM_dd");

    private static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getToday(){
        LocalDate now = LocalDate.now();
        return now.format(YYYY_MM_DD);
    }

    public static boolean moreThanTenMinutes(LocalDateTime start){
        LocalDateTime end = LocalDateTime.now();

        Duration duration = Duration.between(start, end);
        return duration.compareTo(Duration.ofMinutes(10)) > 0;
    }

    public static String format(LocalDateTime localDateTime){
        return localDateTime.format(YYYY_MM_DD_HH_MM_SS);
    }

    public static String format(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static int days(LocalDateTime startTime, LocalDateTime endTime) {
        return (int) (endTime.toLocalDate().toEpochDay() - startTime.toLocalDate().toEpochDay());
    }


}
