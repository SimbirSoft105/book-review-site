package com.simbirsoft.practice.bookreviewsite.validation.validator;

import com.simbirsoft.practice.bookreviewsite.validation.annotation.ValidYear;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Anvar Khasanov
 * student of ITIS KFU
 * group 11-905
 */
public class YearValidator implements ConstraintValidator<ValidYear, Integer> {

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        return year != null && year > 1000 && year <= getCurrentYear();
    }

    private int getCurrentYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }
}
