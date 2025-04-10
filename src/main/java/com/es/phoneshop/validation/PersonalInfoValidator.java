package com.es.phoneshop.validation;

import com.es.phoneshop.enums.PaymentMethod;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.function.Consumer;

public class PersonalInfoValidator {

    public static void setCorrectString(String parameter, String parameterName, Map<String, String> errors,
                                        Consumer<String> consumer)
    {
        if(parameter == null || parameter.isEmpty()){
            errors.put(parameterName, "Value is required");
        } else {
            consumer.accept(parameter);
        }
    }

    public static void setPaymentMethod(String parameter, Map<String, String> errors, Consumer<PaymentMethod> consumer)
    {
        if(parameter == null || parameter.isEmpty()){
            errors.put("paymentMethod", "Value is required");
        } else {
            consumer.accept(PaymentMethod.valueOf(parameter));
        }
    }

    public static void setDeliveryDate(String parameter, Map<String, String> errors, Consumer<LocalDate> consumer) {
        if (parameter == null || parameter.isEmpty()) {
            errors.put("deliveryDate", "Value is required");
        } else {
            try {
                LocalDate date = LocalDate.parse(parameter);
                if (LocalDate.now().isAfter(date)) {
                    errors.put("deliveryDate", "Delivery Date cannot be in the past");
                    return;
                }
                consumer.accept(date);
            } catch (DateTimeParseException e) {
                errors.put("deliveryDate", "Incorrect date format, valid example: " + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        }
    }

    public static void setPhoneNumber(String parameter, Map<String, String> errors, Consumer<String> consumer) {
        if (parameter == null || parameter.isEmpty()) {
            errors.put("phone", "Value is required");
        } else {
            String phonePattern = "^(\\+375|375|80)\\d{9}$";
            if (!parameter.matches(phonePattern)) {
                errors.put("phone", "Invalid phone number");
            } else {
                consumer.accept(parameter);
            }
        }
    }
}
