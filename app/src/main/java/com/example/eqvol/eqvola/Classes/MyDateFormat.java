package com.example.eqvol.eqvola.Classes;

import java.util.ArrayList;

/**
 * Created by eqvol on 24.10.2017.
 */



public class MyDateFormat {
    enum MonthOfYear { January, February, March, April, May, June,
        July, August, September, October, November, December};

    private ArrayList<String> days;
    private ArrayList<String> months;
    private ArrayList<String> years;
    private ArrayList<String> monthsIndex;


    public MyDateFormat()
    {
        days = new ArrayList<String>();
        months = new ArrayList<String>();
        years = new ArrayList<String>();
        monthsIndex = new ArrayList<String>();

        for(int i = 1901; i < 2018; i++)
        {
            years.add(Integer.toString(i));
        }

        for(MonthOfYear month: MonthOfYear.values())
        {
            if(month.ordinal()+1 < 10)
                monthsIndex.add("0" + Integer.toString(month.ordinal()+1));
            else
                monthsIndex.add(Integer.toString(month.ordinal()+1));
            months.add(month.toString());
        }

        for(int i = 1; i < 32; i++)
        {
            if(i < 10)
                days.add("0" + Integer.toString(i));
            else
                days.add(Integer.toString(i));
        }
    }

    public String getMonthByIndex(String index)
    {
        for(int i = 0; i<monthsIndex.size();i++)
        {
            if(monthsIndex.get(i).contentEquals(index))
                return months.get(i).toString();
        }
        return "";
    }

    public ArrayList<String> getYears()
    {
        return years;
    }
    public ArrayList<String> getMonths()
    {
        return months;
    }
    public ArrayList<String> getDays(String year, String month)
    {
        int count = 0;
            for(int i=0;i<months.size(); i++)
            {
                if(month.contentEquals(this.months.get(i)))
                {
                    if(i == 1)
                    {
                        int y = Integer.parseInt(year);

                        if (y % 4 == 0) {
                            if ((y % 100 != 0) || (y % 400 == 0)) {
                                count = 29;
                                break;
                            }
                        }
                        count = 28;
                        break;
                    }
                    if(i<7) {
                        if (i % 2 == 0)
                            count = 31;
                        else
                            count = 30;
                    } else {
                        if (i % 2 == 1)
                            count = 31;
                        else
                            count = 30;
                    }
                    break;
                }
            }

        if(count > days.size())
            for(int i = days.size()+1; i < count+1; i++)
            {
                days.add(Integer.toString(i));
            }
        else if(count < days.size())
            for(int i = days.size()-1; i > count-1; i--) {
                days.remove(i);
            }

        return days;
    }
}
