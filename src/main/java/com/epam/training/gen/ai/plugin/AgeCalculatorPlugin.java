package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AgeCalculatorPlugin {

    /**
     * Calculate age from a given date of birth in the format "yyyy-MM-dd".
     *
     * @param dateOfBirthStr The date of birth as a String (expected format: yyyy-MM-dd).
     * @return A String representing the calculated age in years.
     */
    @DefineKernelFunction(description = "Calculate age based on the given date of birth (format: yyyy-MM-dd).")
    public String calculateAge(
            @KernelFunctionParameter(name = "dateOfBirth", description = "Date of birth in yyyy-MM-dd format") String dateOfBirthStr) {

        try {
            // Parse the date of birth
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr, formatter);

            // Get the current date
            LocalDate currentDate = LocalDate.now();

            // Check if the date of birth is in the future
            if(dateOfBirth.isAfter(currentDate)) {
                return "Error: Date of birth cannot be in the future.";
            }

            // Calculate the age as a period
            Period age = Period.between(dateOfBirth, currentDate);

            // Return the age in years
            return String.valueOf(age.getYears());
        } catch (DateTimeParseException e) {
            // If input format is invalid, log and return an error message
            return "Error: Invalid date format.";
        }
    }
}
