package com.company;

import org.jfree.data.xy.VectorSeries;
import org.jfree.data.xy.XYSeries;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        // Задание таблицы координат
        double[][] coordsArr = getCitiesCoords("coords.txt");

        // Задание таблицы D
        double[][] dTable = getDTableFromCoords(coordsArr);

        // Получение других данных для модели
        File params = new File("settings.txt");
        Scanner params_in = new Scanner(params);
        int mut = Integer.parseInt(params_in.nextLine().split(" ")[1]);
        int mutParam = Integer.parseInt(params_in.nextLine().split(" ")[1]);
        int cross = Integer.parseInt(params_in.nextLine().split(" ")[1]);
        int numOfVec = Integer.parseInt(params_in.nextLine().split(" ")[1]);
        int cycles = Integer.parseInt(params_in.nextLine().split(" ")[1]);
        int randomFunction = Integer.parseInt(params_in.nextLine().split(" ")[1]);

        // Дополнительная проверка для корректности введенного параметра мутации
        if (mutParam > dTable.length){
            System.out.println("Введент не корректный параметр мутации");
            return;
        }

        // Создание модели
        Barbie model = new Barbie(dTable, mut, cross, numOfVec, randomFunction, mutParam);

        // Создание файла out и запись в него первого поколения
        FileWriter out = new FileWriter(new File("out.txt"));
        printInFileCurrentGeneration(model, out);
        // Вывод первого наилучшего пути
        System.out.println(model.peak());

        // Создание серий, для графика
        XYSeries series = new XYSeries("RouteLength");

        // Исполнение модели n раз
        series.add(0, model.peak().getLength());
        for (int j = 0; j < cycles; j++) {
            model.runModel();
            series.add(j+1, model.peak().getLength());
        }
        Route finalRoute = model.peak();
        System.out.println(finalRoute);
        printInFileCurrentGeneration(model, out);

        // Вывод графика
//        Chart chart = new Chart("Коммивояжер", "Step", "Route range");
//        chart.setData(series);
//        chart.draw();

        // Вывод вторго графика для лучшего маршрута
        VectorSeries cSeries = new VectorSeries("RouteLength");
        fillSeriesFromRoute(cSeries, finalRoute, coordsArr);

        Chart chartCities = new Chart("Города", "X coord", "Y coord");
        chartCities.setData(cSeries);
        chartCities.draw();

        out.close();
    }

    static void printMas(double[][] mas){
        for (double[] ma : mas) {
            for (double i : ma) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }

    private static void printInFileCurrentGeneration(Barbie b, FileWriter out) throws IOException {

        ArrayList<Route> list = b.getVec();
        Iterator<Route> itr = list.iterator();

        out.write("| ");
        while (itr.hasNext()){
            out.write(itr.next().toString());
            out.write(" | ");
        }
        out.write("\n");
    }

    private static double[][] getDTableFromFile(String file) throws FileNotFoundException {
        File table = new File(file);
        Scanner tab_in = new Scanner(table);
        int size = tab_in.nextInt();
        tab_in.nextLine();
        double[][] mas = new double[size][];
        int i = 0;
        while (tab_in.hasNext()){
            mas[i] = new double[i+1];
            for (int j = 0; j < i+1; j++){
                mas[i][j] = tab_in.nextDouble();
            }
            if (tab_in.hasNext()){
                tab_in.nextLine();
            }
            i++;
        }
        return mas;
    }

    private static double[][] getCitiesCoords(String file) throws FileNotFoundException {

        Scanner in = new Scanner(new File(file));
        // Считывание кол-ва городов и создание соотв-го массива
        int size = in.nextInt();
        in.nextLine();
        double[][] arr = new double[size][2];
        for (int i = 0; i < size; i++) {
            arr[i][0] = in.nextDouble();
            arr[i][1] = in.nextDouble();
            if (in.hasNext()) {
                in.nextLine();
            }
        }

        return arr;
    }

    private static double[][] getDTableFromCoords(double[][] coords){

        double[][] d = new double[coords.length][];
        for (int i = 0; i < d.length; i++) {
            d[i] = new double[i+1];
            for (int j = 0; j < d[i].length; j++) {
                if (i == j){
                    d[i][j] = 0;
                }else {
                    d[i][j] = getDotsRange(coords[i], coords[j]);
                }
            }
        }

        return d;
    }

    private static double getDotsRange(double[] dot1, double[] dot2){
        return Math.sqrt(Math.pow(dot1[0] - dot2[0], 2) + Math.pow(dot1[1] - dot2[1], 2));
    }

    private static void fillSeriesFromRoute(VectorSeries series, Route route, double[][] coords){

        String[] routeArr = route.route_splitted;
        for (int i = 0; i < routeArr.length-1; i++) {
            int city1 = Integer.parseInt(routeArr[i]);
            int city2 = Integer.parseInt(routeArr[i+1]);
            series.add(coords[city1][0], coords[city1][1], coords[city2][0] - coords[city1][0], coords[city2][1] - coords[city1][1]);
        }
        int city1 = Integer.parseInt(routeArr[routeArr.length-1]);
        int city2 = Integer.parseInt(routeArr[0]);
        series.add(coords[city1][0], coords[city1][1], coords[city2][0] - coords[city1][0], coords[city2][1] - coords[city1][1]);
    }
}
