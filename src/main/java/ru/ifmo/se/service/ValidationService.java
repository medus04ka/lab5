package ru.ifmo.se.service;

import jakarta.validation.*;
import ru.ifmo.se.model.HumanBeing;

import java.util.Collection;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class ValidationService {

    public boolean hasDuplicatesId(PriorityQueue<HumanBeing> humans) {
        Set<Long> uniqueIds = new HashSet<>();
        boolean hasDuplicates = humans.stream()
                .map(HumanBeing::getId)
                .anyMatch(id -> !uniqueIds.add(id));
        return hasDuplicates;
    }

    public void validateConstraints(Collection<HumanBeing> humans) throws ValidationException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> violationsHumanBeing = new HashSet<>();
        Set<ConstraintViolation<Object>> violationsCoordinates = new HashSet<>();
        for (HumanBeing human : humans) {
            violationsHumanBeing.addAll(validator.validate(human));
            violationsCoordinates.addAll(validator.validate(human.getCoordinates()));
        }
        if (!violationsHumanBeing.isEmpty()) {
            throw new ValidationException(getViolationErrorMessage(violationsHumanBeing) + getViolationErrorMessage(violationsCoordinates));
        }
    }

    private String getViolationErrorMessage(Set<ConstraintViolation<Object>> constraintViolations) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : constraintViolations) {
            errorMessageBuilder.append(System.lineSeparator()).append("- Объект типа: ").append(violation.getRootBeanClass().getSimpleName());
            errorMessageBuilder.append(", Поле: ").append(violation.getPropertyPath());
            errorMessageBuilder.append(", Значение: ").append(violation.getInvalidValue());
            errorMessageBuilder.append(", Сообщение: ").append(violation.getMessage());
        }
        errorMessageBuilder.append(System.lineSeparator());
        return errorMessageBuilder.toString();
    }
}
