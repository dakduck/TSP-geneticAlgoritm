package com.company;

import java.lang.reflect.Array;
import java.util.*;

public class Barbie {

    // Кол-во мутаций и кросоверов, соответственно
    private int mut, cross;

    private int mutParam;

    // Матрица расстояний между городами
    private double[][] d;

    //кол-во векторов
    private int numOfVect;

    /**
     * Номер функции рандома
     * 0 - равномерный
     * 1 - линейный
     * 2 - экспоненциальный
     */
    private int randomNumber;

    // Список векторов
    private ArrayList<Route> vec;

    // Конструктор (READY)
    Barbie(double[][] d, int mut, int cross, int numOfVect, int randomNumber, int mutParam) {

        this.mut = mut;
        this.cross = cross;
        this.d = d.clone();
        this.numOfVect = numOfVect;
        generateVectors(numOfVect);
        this.randomNumber = randomNumber;
        this.mutParam = mutParam;

    }

    // Генерация начальных векторов (READY)
    private void generateVectors(int numOfVect) {

        this.vec = new ArrayList<>();
        ArrayList<Integer> numbers = new ArrayList<>();

        Random r = new Random();
        for (int j = 0; j < numOfVect; j++){
            numbers.clear();
            for (int i = 0; i < this.d.length; i++) {
                numbers.add(i);
            }
            StringBuilder answer = new StringBuilder();
            for (int i = 0; i < this.d.length-1; i++) {
                int randomPos = r.nextInt(numbers.size());
                answer.append(numbers.get(randomPos));
                answer.append('-');
                numbers.remove(randomPos);
            }
            answer.append(numbers.get(0));
            this.vec.add(new Route(answer.toString(), this.d));
        }
        Collections.sort(this.vec);

    }

    // Запуск одного цикла модели (READY)
    void runModel(){

        // Докидываем прошлых челиков
        // Тут строки передаютс по ссылке и их низя изменять***
        ArrayList<Route> answer = new ArrayList<>(this.vec);

        // Мутим мутантов и их тож в кастрюлю (муантв сразу многа)
        getMutants(answer);

        // Кросим кросоверов
        doCross(answer);

        // Сортировка
        Collections.sort(answer);

        // Дописываем самых топовых
        this.vec.clear();
        for (int i = 0; i < numOfVect; i++) {
            this.vec.add(answer.get(i));
        }
    }

    // Возвращает "int mut" мутантов в ArrayList (READY)
    private void getMutants(ArrayList<Route> answer){

        // ПРОВЕРКИ
        for (int i = 0; i < this.mut; i++) {

            Route r;
            if (this.mutParam == 2) {
                r = getMutant(answer);
            }else{
                r = getMutant(answer, this.mutParam);
            }

            if (!answer.contains(r)){
                answer.add(r);
            }
        }

    }

    private Route getMutant(ArrayList<Route> answer){

        StringBuilder mutant = new StringBuilder();
        String[] routeMas = answer.get(getRandom(answer.size())).route_splitted;

        Random r = new Random();
        int swipe1 = r.nextInt(routeMas.length);
        int swipe2 = r.nextInt(routeMas.length);
        while (swipe1 == swipe2) {
            swipe2 = r.nextInt(routeMas.length);
        }

        for (int i = 0; i < routeMas.length-1; i++) {
            if (i == swipe1){
                mutant.append(routeMas[swipe2]);
                mutant.append('-');
            }else if (i == swipe2){
                mutant.append(routeMas[swipe1]);
                mutant.append('-');
            }else{
                mutant.append(routeMas[i]);
                mutant.append('-');
            }
        }
        if (routeMas.length-1 == swipe1){
            mutant.append(routeMas[swipe2]);
        }else if (routeMas.length-1 == swipe2){
            mutant.append(routeMas[swipe1]);
        }else{
            mutant.append(routeMas[routeMas.length-1]);
        }

        return new Route(mutant.toString(), this.d);
    }

    private Route getMutant(ArrayList<Route> allCurRoutes, int numberOfSwipes){



        return null;
    }

    // Вариант мутации с выбором из текущей популяции, а не из всех текущих
    private Route getMutant_old(){

        StringBuilder answer = new StringBuilder();
        String[] routeMas = this.vec.get(getRandom(this.numOfVect)).route_splitted;

        Random r = new Random();
        int swipe1 = r.nextInt(routeMas.length);
        int swipe2 = r.nextInt(routeMas.length);

        for (int i = 0; i < routeMas.length-1; i++) {
            if (i == swipe1){
                answer.append(routeMas[swipe2]);
                answer.append('-');
            }else if (i == swipe2){
                answer.append(routeMas[swipe1]);
                answer.append('-');
            }else{
                answer.append(routeMas[i]);
                answer.append('-');
            }
        }
        if (routeMas.length-1 == swipe1){
            answer.append(routeMas[swipe2]);
        }else if (routeMas.length-1 == swipe2){
            answer.append(routeMas[swipe1]);
        }else{
            answer.append(routeMas[routeMas.length-1]);
        }

        return new Route(answer.toString(), this.d);
    }

    // Кроссоверит кроссоверы
    private void doCross(ArrayList<Route> answer){

        for (int i = 0; i < this.cross; i++) {
            doCrossOnce(answer);
        }

    }

    private void doCrossOnce(ArrayList<Route> answer){

        Random r =new Random();
        ArrayList<Route> copyOfVec = new ArrayList<>(this.vec);

        // Получение весторов для кроссовера
        int rNumber = r.nextInt(copyOfVec.size());
        Route route1 = copyOfVec.get(rNumber);
        copyOfVec.remove(rNumber);
        rNumber = r.nextInt(copyOfVec.size());
        Route route2 = copyOfVec.get(rNumber);

        // Получение точки разрыва кроссовера
        rNumber = r.nextInt(this.d.length);

        // Формирования строкового представления маршрутов
        ArrayList<String> cross12 = new ArrayList<>();
        ArrayList<String> cross21 = new ArrayList<>();

        for (int i = 0; i <= rNumber; i++) {
            cross12.add(route1.route_splitted[i]);
            cross21.add(route2.route_splitted[i]);
        }
        for (int i = 0; i < route2.route_splitted.length; i++) {
            if (!cross12.contains(route2.route_splitted[i])){
                cross12.add(route2.route_splitted[i]);
            }
            if (!cross21.contains(route1.route_splitted[i])){
                cross21.add(route1.route_splitted[i]);
            }
        }

        // Создание новых маршрутов из готовых строк
        Route resultRoute1 = new Route(cross12, this.d);
        Route resultRoute2 = new Route(cross21, this.d);

        if (!answer.contains(resultRoute1)){
            answer.add(resultRoute1);
        }
        if (!answer.contains(resultRoute2)){
            answer.add(resultRoute2);
        }
    }

    // Метод возвращает рандомную позицию в списке из mumOfIndividuals позиций,
    // используюя функцию выбора, указанную при создании модели
    private int getRandom(int numOfIndividuals) {
        if (this.randomNumber == 0) { // Равновероятностный выбор
            return new Random().nextInt(numOfIndividuals);
        } else if (this.randomNumber == 1) { // Линейный выбор
            return getRandomLiner(numOfIndividuals);
        } else{ // Экспоненциальная вер-ть выбора
            return getRandomExp(numOfIndividuals);
        }
    }

    // Метод, выполняющий выборку according to linear function
    private int getRandomLiner(int numOfIndividuals) {
        double tmp;
        double [] mas = new double [numOfIndividuals];
        double sum = 0;
        for (int i = 0; i < numOfIndividuals; i++) {
            tmp = -(i - numOfIndividuals);
            sum = sum + tmp;
            if (i==0) {
                mas[i] = 0+tmp;
            } else {
                mas[i] = mas[i-1]+tmp;
            }

        }
        double r =Math.random()*mas[mas.length-1];
        for (int i = 0; i < mas.length; i++) {
            if (r < mas[i]) {
                return i;
            }
        }
        return -1;
    }

    // Метод, выполняющий выборку according to exponential function
    private int getRandomExp(int numberOfIndividuals) {
        // Функция:   y= 1/e^(x - numberOfIndividuals)
        double [] masY = new double [numberOfIndividuals];
        double sum = 0; // Сумма значений функции во всех точках
        for (int i = 0; i < masY.length; i++) {
            masY[i] = 1/Math.pow(Math.E, i - numberOfIndividuals);
            sum += masY[i];
        }
        double randomNumber = Math.random() * masY[0]; // Получаем рандомное число от 0 до masY[0], т.е. [0, masY[0])
        for (int i = masY.length-1; i >= 0; i--) { // Смотрим, в каком диапазоне randomNumber
            if (randomNumber < masY[i]){
                return i;
            }
        }

        return -1; // Ошибку словим если что то пошло не так
    }

    // Надо подумать как переработать так, чтобы не возвращалась ссылка на строку, а только копия
    Route peak(){
        return this.vec.get(0);
    }

    ArrayList<Route> getVec() {
        return vec;
    }

    static ArrayList<Integer> getRandomPermutation(ArrayList<Integer> list){

        ArrayList<Integer> answer = new ArrayList<>(list);
        Random r = new Random();
        for (int i = 0; i < answer.size() - 1; i++) {
            int n = r.nextInt(answer.size() - 1);
            answer.add(answer.get(n));
            answer.remove(n);
        }

        return answer;
    }
}
