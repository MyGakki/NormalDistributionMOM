import org.apache.activemq.ActiveMQConnectionFactory;
import org.jfree.data.time.TimeSeries;

import javax.jms.*;
import javax.swing.*;


public class NDConsumer {

    private TimeSeries randomNum;

    public NDConsumer() {

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
            factory = new ActiveMQConnectionFactory(MyURL);
            connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            topic = session.createTopic("ND");
            consumer = session.createConsumer(topic);
            listener = new MyListener();
            consumer.setMessageListener(listener);
            connection.start();

            JFrame jframe = new JFrame("ND");


            System.out.println("Press Any Key To Exit");
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
