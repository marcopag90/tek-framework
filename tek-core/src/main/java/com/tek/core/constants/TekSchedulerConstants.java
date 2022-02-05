package com.tek.core.constants;

/*
A Cron expression consists of six sequential fields:
second, minute, hour, day of month, month, day(s) of week
 */

//------------------------------ Cron scheduling syntax -------------------------------------------
//  "*"           match any            "* * * * * *"          do always
//  "*/x"         every x              "*/5 * * * * *"        do every five seconds
//  "?"           no spec     "0 0 0 25 12 ?"        do every Christmas Day

//------------------------------ Cron scheduling examples -----------------------------------------
//  "0 0 * * * *"                 the top of every hour of every day.
//  "*/10 * * * * *"              every ten seconds.
//  "0 0 8-10 * * *"              8, 9 and 10 o'clock of every day.
//  "0 0/30 8-10 * * *"           8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
//  "0 0 9-17 * * MON-FRI"        on the hour nine-to-five weekdays
//  "0 0 0 25 12 ?"               every Christmas Day at midnight

/**
 * Constants for scheduled cron jobs handling.
 *
 * @author MarcoPagan
 */
public final class TekSchedulerConstants {

  private TekSchedulerConstants() {
  }

  /**
   * Default temporal cron execution
   */
  public static final String CRON_DAILY_MIDNIGHT = "0 0 * * * *";
}
