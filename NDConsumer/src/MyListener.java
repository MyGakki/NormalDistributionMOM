import org.jfree.data.time.Millisecond;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;

public class MyListener implements MessageListener {
    List<Double> randomList = new ArrayList<Double>();
    double average;
    double sum = 0;
    int N = 10;
    double exception = 2.0;
    NDConsumer consumer;

    public MyListener(NDConsumer consumer) {
        super();
        this.consumer = consumer;
    }

    @Override
    public void onMessage(Message message) {
        try{
            double temp = Double.valueOf(((TextMessage)message).getText());
            randomList.add(temp);
            sum += temp;
            consumer.randomNum.add(new Millisecond(), temp);
            if (temp > exception ||temp < -exception)
                System.out.println("Value:" + temp + " EXCEPTION");
            System.out.println("Average: " + sum/randomList.size());
            System.out.println("Latest 10 Variance:" + getVariance(randomList,N));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getAverage(List<Double> list, int N) {
        double tempSum = 0.0;
        int length = list.size();
        for (int i = length - N; i < length; i++) {
            tempSum += list.get(i);
        }
        return tempSum/N;
    }

    public double getVariance (List<Double> list, int N) {
        if (list.size() < N)
            return getVariance(list, list.size());
        else {
            int length = list.size();
            double tempSum = 0.0;
            double tempAverage = getAverage(list, N);
            for (int i = length - N; i < length; i++) {
                tempSum += (list.get(i) - tempAverage) * (list.get(i) - tempAverage);
            }
            return tempSum/N;
        }
    }
}
