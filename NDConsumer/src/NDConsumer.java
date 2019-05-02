import org.apache.activemq.ActiveMQConnectionFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.jms.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class NDConsumer extends JPanel {

    public TimeSeries randomNum;

    public NDConsumer(String title, String timeAxisLabel, String valueAxisLabel) {
        super(new BorderLayout());
        randomNum = new TimeSeries("Random Number", Millisecond.class);

        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection(randomNum);

        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(title, timeAxisLabel, valueAxisLabel,
                timeSeriesCollection, true,true,false);

        XYPlot xyPlot = jfreechart.getXYPlot();
        ValueAxis valueAxis = xyPlot.getDomainAxis();
        valueAxis.setAutoRange(true);

        valueAxis = xyPlot.getRangeAxis();

        ChartPanel chartpanel = new ChartPanel(jfreechart, true);
        chartpanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createEmptyBorder(10, 10, 10, 10), BorderFactory
                .createLineBorder(Color.black)));

        add(chartpanel);
    }

    public static void main(String[] args) throws JMSException {
        String MyURL = "tcp://127.0.0.1:61616";
        ConnectionFactory factory = null;
        Connection connection = null;
        Session session = null;
        Topic topic = null;
        MessageConsumer consumer = null;
        MyListener listener = null;

        try {
            NDConsumer ndConsumer = new NDConsumer("Normal Distribution", "Time", "Random Number");
            factory = new ActiveMQConnectionFactory(MyURL);
            connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            topic = session.createTopic("ND");
            consumer = session.createConsumer(topic);
            listener = new MyListener(ndConsumer);
            consumer.setMessageListener(listener);
            connection.start();

            JFrame jframe = new JFrame("Normal Distribution");
            jframe.getContentPane().add(ndConsumer,"Center");
            jframe.setBounds(200, 120, 1000, 500);
            jframe.setVisible(true);

            jframe.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent windowevent) {
                    System.exit(0);
                }
            });

            System.out.println("Press Any Key To Exit");
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
