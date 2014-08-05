import java.io.IOException;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.AMQP;
import java.util.*;
import java.lang.Thread;



public class Producer{
    private static final String EXCHANGE_NAME_ONE = "logs_one";
 
   // private final static int max_mess = 100000;
    
    public static void main(String[] argv)
    throws java.io.IOException {
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        channel.exchangeDeclare(EXCHANGE_NAME_ONE, "fanout");
        
        int numofmessages;
        int max_mess;
        long  start,end;
        
        for(max_mess = 1;max_mess<=100000;max_mess*=10){
                start = new Date().getTime();
            
                for(numofmessages = 1;numofmessages<=max_mess;numofmessages++){
                    String message = "messages";
                    
                    channel.basicPublish(EXCHANGE_NAME_ONE, "",
                                         new AMQP.BasicProperties.Builder()
                                         .timestamp(new Date())
                                         .build(),
                                         message.getBytes());
                }
                
                end = new Date().getTime();
                
                System.out.println("Time Taken to produce "+max_mess+" messages in millsecs = "+(end-start));
                /*try {
                    Thread.sleep(1000-end+start);
                }
                catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }*/
        }
    }
}