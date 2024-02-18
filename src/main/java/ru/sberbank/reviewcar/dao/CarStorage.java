package ru.sberbank.reviewcar.dao;

import ru.sberbank.reviewcar.model.Car;
import ru.sberbank.reviewcar.model.Director;
import ru.sberbank.reviewcar.model.Type;
import ru.sberbank.reviewcar.model.Klass;

import java.util.Collection;
import java.util.List;

public interface CarStorage {

    List<Car> getCar(List<Integer> id);

    Collection<Car> getAllCars();

    Car addCar(Car car);

    Car updateCar(Car car);

    String deleteCar(int carId);

    void makeLike(int idCar, int idUser);

    void deleteLike(int idCar, int idUser);

    Collection<Car> getPopularCars(int count);

    List<Integer> getCarLikes(Integer id);

    Klass checkKlass(Car car);

    List<Type> getTypes(int idCar);

    List<Type> checkType(Car car);

    void insertCarTypes(Car car);

    void deleteCarTypes(Car car);

    void addDirectorsByCarId(int carId, int directorId);

    Collection<Director> findDirectorsByCarId(Integer carId);

    Collection<Car> getLikesCars(int userId);

    Collection<Car> getPopularCarsByTypeAndYear(int count, Integer typeId, Integer year);

    Collection<Car> getSearchCarsByDirector(String query);

    Collection<Car> getSearchCarsByTitle(String query);

    Collection<Car> findAllCars();

    void deleteDirectorsByCarId(Integer carId);

}
