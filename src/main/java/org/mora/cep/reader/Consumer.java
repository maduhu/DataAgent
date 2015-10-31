package org.mora.cep.reader;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.BasicConfigurator;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ruveni on 30/10/15.
 */
public class Consumer extends TimerTask {
    // URL of the JMS server
    private static String url = "tcp://localhost:61616";
    // Name of the queue we will receive messages from
    private static String subject = "queueMap";
    MessageConsumer consumer = null;
    Connection connection = null;
    Session session = null;
    Timer timer=null;

    public Consumer(Timer timer) {
        this.timer=timer;
        try {
            BasicConfigurator.configure();
            // Getting JMS connection from the server
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            connection = connectionFactory.createConnection();
            connection.start();
            // Creating session for seding messages
            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);

            // Getting the queue
            Destination destination = session.createQueue(subject);
            // MessageConsumer is used for receiving (consuming) messages

            consumer = session.createConsumer(destination);
            // Here we receive the message.

            // By default this call is blocking, which means it will wait

            // for a message to arrive on the queue.
        } catch (Exception e) {
            e.printStackTrace();
            timer.cancel();
        }
    }

    @Override
    public void run() {
        try {
            Message message = consumer.receive();
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                System.out.println("Received message '" + textMessage.getText() + "'");
                System.out.println();
            }
        } catch (Exception e) {
            timer.cancel();
            e.printStackTrace();
            try{
                connection.close();
            }catch (Exception ex){
                //ex.printStackTrace();
            }
        }
    }
}
