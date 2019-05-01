import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Random;

public class NDPublisher {
    private static String MyURL = "tcp://localhost:61616";
    private static ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private Topic topic;

    public NDPublisher(String topicName) throws JMSException {
        factory = new ActiveMQConnectionFactory(MyURL);
        connection = factory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        topic = session.createTopic(topicName);
        producer = session.createProducer(topic);

        connection.start();
    }

    public void close() throws JMSException {
        if (connection != null)
            connection.close();
    }

    public static void main(String[] args) throws JMSException,InterruptedException{
        NDPublisher publisher = new NDPublisher("ND");
        publisher.sendMessage();
        publisher.close();
    }

    public void sendMessage() throws JMSException,InterruptedException {
        Random random = new Random(System.currentTimeMillis());
        System.out.println("产生消息中。。。。。。");
        while (true) {
            double randNum = random.nextGaussian();
            Message message = session.createTextMessage(String.valueOf(randNum));
            producer.send(message);
            Thread.sleep(500);
        }
    }
}
