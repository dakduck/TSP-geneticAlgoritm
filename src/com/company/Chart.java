package com.company;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.xy.*;

import javax.swing.*;
import java.awt.*;

class Chart extends JFrame {

    private XYDataset data; // Координаты точек графика (графиков)
    private String title, // Заголовок диаграммы
                    xAxisLabel,
                    yAxisLabel;

    Chart(String title, String xAxisLabel, String yAxisLabel) throws HeadlessException {
        this.title = title;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
    }

    void draw(){
        this.setVisible(true);

        JFreeChart chart = ChartFactory.createXYLineChart(this.title,
                this.xAxisLabel,
                this.yAxisLabel,
                this.data,
                PlotOrientation.VERTICAL,
                true,
                true,
                true);
        XYPlot plot = chart.getXYPlot();

//        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        VectorRenderer renderer = new VectorRenderer();
//        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void setData(VectorSeries series){

        VectorSeriesCollection seriesCollection = new VectorSeriesCollection();
        seriesCollection.addSeries(series);
        this.data = seriesCollection;

    }
}
