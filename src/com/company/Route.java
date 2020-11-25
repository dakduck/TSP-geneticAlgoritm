package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Route implements Comparable<Route> {

    String route;
    double[][] d;
    private double length;
    String[] route_splitted;

    Route(String route, double[][] d) {
        this.route = route;
        this.route_splitted = route.split("-");
        this.d = d;
        setLenght();
    }

    Route(ArrayList<String> list, double[][] d){

        StringBuilder answer = new StringBuilder();
        this.route_splitted = new String[list.size()];

        Iterator<String> itr = list.iterator();
        int i = 0;
        while (itr.hasNext()){
            String buf = itr.next();
            route_splitted[i] = buf;
            answer.append(buf).append('-');
            i++;
        }

        this.d = d;
        this.route = answer.replace(answer.length()-1, answer.length(), "").toString();
        setLenght();
    }

    private void setLenght() {
        this.length = 0;
        for (int i = 0; i < this.route_splitted.length - 1; i++) {
            int x1 = Integer.parseInt(this.route_splitted[i]),
                x2 = Integer.parseInt(this.route_splitted[i+1]);
            if (x1 > x2){
                this.length += this.d[x1][x2];
            }else {
                this.length += this.d[x2][x1];
            }
        }
        int x1 = Integer.parseInt(this.route_splitted[0]),
                x2 = Integer.parseInt(this.route_splitted[route_splitted.length-1]);
        if (x1 > x2){
            this.length += this.d[x1][x2];
        }else {
            this.length += this.d[x2][x1];
        }
    }

    @Override
    public String toString() {
        String answer = "Route{route=%s-%s, length=%.5f}";
        return String.format(answer,
                this.route,
                this.route_splitted[0],
                this.length);
//        return "Route{" +
//                "route='" + route + '\'' +
//                ", lenght=" + length +
//                '}';
    }

    @Override
    public int compareTo(Route route) {
        double eps = 0.00000000001;
        if (Math.abs(this.length - route.getLength()) < eps){
            return 0;
        }
        return this.length < route.length ?  -1 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route1 = (Route) o;
        return Objects.equals(route, route1.route);
    }

    @Override
    public int hashCode() {
        return Objects.hash(route);
    }

    public double getLength() {
        return length;
    }
}
