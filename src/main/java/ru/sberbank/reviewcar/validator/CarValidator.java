package ru.sberbank.reviewcar.validator;

import lombok.extern.slf4j.Slf4j;
import ru.sberbank.reviewcar.exception.ValidationException;
import ru.sberbank.reviewcar.model.Car;
import java.time.LocalDate;

@Slf4j
public class CarValidator {

    public static void validateCar(Car car) {
        try {
            if (car.getName().isBlank()) {
                log.info("Название машины не может быть пустым.");
                throw new ValidationException("Название машины не может быть пустым.");
            }
            if (car.getDescription().length() > 2000) {
                log.info("Длина описания не может превышать больше 2000 символов.");
                throw new ValidationException("Длина описания не может превышать больше 2000 символов.");
            }
            if (car.getPrice() <= 0) {
                log.info("Стоимость машины должна быть положительной.");
                throw new ValidationException("Стоимость машины должна быть положительной.");
            }
        } finally {

        }
    }
}