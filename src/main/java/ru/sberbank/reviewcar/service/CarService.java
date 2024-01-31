package ru.sberbank.reviewcar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sberbank.reviewcar.exception.NotFoundException;
import ru.sberbank.reviewcar.model.*;
import ru.sberbank.reviewcar.validator.CarValidator;
import ru.sberbank.reviewcar.dao.DirectorStorage;
import ru.sberbank.reviewcar.dao.CarStorage;
import ru.sberbank.reviewcar.dao.UserStorage;
import ru.sberbank.reviewcar.dao.eventEnum.EventOperation;
import ru.sberbank.reviewcar.dao.eventEnum.EventType;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CarService {

    private final CarStorage carStorageDb;
    private final UserStorage userStorageDb;
    private final DirectorStorage directorRepository;
    private final EventService eventService;

    public Car getCar(int id) {
        Car car;
        try {
            car = carStorageDb.getCar(List.of(id)).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException("Машина с id=" + id + " не найдена");
        }
        return car;
    }

    public Collection<Car> getAllCars() {
        return carStorageDb.findAllCars();
    }

    public Car addCar(Car car) {
        CarValidator.validateCar(car);
        Klass klass = carStorageDb.checkKlass(car);
        List<Type> types = carStorageDb.checkType(car);
        Car car1 = carStorageDb.addCar(car);
        car1.setKlass(klass);
        car1.setTypes(types);
        carStorageDb.insertCarTypes(car1);

        if (car.getDirectors() != null) {
            Collection<Director> directors = car.getDirectors();
            for (Director director : directors) {
                int idDir = director.getId();
                carStorageDb.addDirectorsByCarId(car.getId(), idDir);
            }
        }

        return car1;
    }

    public Car updateCar(Car enteredCar) {
        CarValidator.validateCar(enteredCar);
        getCar(enteredCar.getId());
        Klass klass = carStorageDb.checkKlass(enteredCar);
        List<Type> types = carStorageDb.checkType(enteredCar);
        Car carForResult = carStorageDb.updateCar(enteredCar);
        carForResult.setKlass(klass);
        carForResult.setTypes(types);
        carStorageDb.deleteCarTypes(carForResult);
        carStorageDb.insertCarTypes(carForResult);

        carStorageDb.deleteDirectorsByCarId(enteredCar.getId());
        if (enteredCar.getDirectors() != null) {
            Collection<Director> directors = enteredCar.getDirectors();
            for (Director director : directors) {
                int idDir = director.getId();
                carStorageDb.addDirectorsByCarId(enteredCar.getId(), idDir);
            }
        }
        return carForResult;
    }

    public Collection<Car> getCarsByDirector(Integer directorId) {
        Director director = directorRepository.findById(directorId).orElseThrow(() -> new NotFoundException("404"));
        List<Director> director2 = directorRepository.getDirectorsByCarId(directorId);
        List<Car> cars = new ArrayList<>(carStorageDb.findAllCars());

       /* Comparator<Car> comparator = null;
        if (sortBy.equals("director")) {
            comparator = Comparator.comparing(Car::getId);
        } else if (sortBy.equals("likes")) {
            comparator = Comparator.comparingInt(Car::getRate).reversed();
        } else {
            String message = String.format("Сортировка по типу %s отсутствует", sortBy);
            throw new IllegalStateException(message);
        }*/

        return cars.stream()
                .filter(car -> !car.getDirectors().isEmpty())
                .collect(Collectors.toList());
    }


    public Collection<Car> getSearchCars(String query, String by) {
        List<String> searchFields = Arrays.asList(by.split(","));

        List<Car> cars = new ArrayList<>(this.getAllCars());
        List<Car> result = new ArrayList<>();

        final boolean isDirectorProvided = (searchFields.contains("director"));
        final boolean isTitleProvided = searchFields.contains("title");

        for (Car car : cars) {
            boolean isCarAdded = false;

            if (isTitleProvided && car.getName().toLowerCase().contains(query.toLowerCase())) {
                result.add(car);
                isCarAdded = true;
            }

            if (isDirectorProvided && !isCarAdded && car.getDirectors().stream()
                .anyMatch(director -> director.getName().toLowerCase().contains(query.toLowerCase()))) {
                result.add(car);
            }
        }
        result.sort(Comparator.comparing(Car::getId).reversed());
        return result;
    }

    public void makeLike(int idCar, int idUser) {
        carStorageDb.makeLike(idCar, idUser);
        eventService.createEvent(idUser, EventType.LIKE, EventOperation.ADD, idCar);
    }


    public void deleteLike(int idCar, int idUser) {
        User user = userStorageDb.getUser(idUser);
        if (user == null) {
            throw new NotFoundException("User с id " + idUser + " не найден.");
        }
        carStorageDb.deleteLike(idCar, idUser);
        eventService.createEvent(idUser, EventType.LIKE, EventOperation.REMOVE, idCar);
    }

    public Collection<Car> getPopularCars(int count, Integer typeId, Integer year) {
        if (count < 1) {
            log.info("count не может быть отрицательным.");
            throw new NotFoundException("count не может быть отрицательным.");
        }
        if (typeId != null || year != null) {
            return carStorageDb.getPopularCarsByTypeAndYear(count, typeId, year);
        }
        return new HashSet<>(carStorageDb.getPopularCars(count));
    }

    public Collection<Car> getLikesCars(int userId) {
        return carStorageDb.getLikesCars(userId);
    }

    public String deleteCar(Integer id) {
        return carStorageDb.deleteCar(id);
    }

    public Collection<Car> getPopularCarsByTypeAndYear(int count, Integer typeId, Integer year) {
        return carStorageDb.getPopularCarsByTypeAndYear(count, typeId, year);
    }

    public List<Car> getRecommendations(int id) {
        User targetUser;
        try {
            targetUser = userStorageDb.getUser(id);
        } catch (RuntimeException e) {
            throw new NotFoundException("пользователя с таким id не существует");
        }
        return carStorageDb.getCar(userStorageDb.getRecommendations(id, targetUser));
    }
}
