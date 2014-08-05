
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.*;
import java.io.PrintStream;
import java.lang.*;

public class Consumer{
    
    private static final String EXCHANGE_NAME_ONE = "logs_one";
  
    public static long diff = 0;
    private static final int max_mess = 100000;
    
    public static void main(String[] argv)
    throws java.io.IOException,
    java.lang.InterruptedException{
        ConnectionFactory  factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        
        channel.exchangeDeclare(EXCHANGE_NAME_ONE,"fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,EXCHANGE_NAME_ONE,"");
        
        boolean doit = true;
        int i = 0;
        int max_mess = 1;
        int numofmessages = 1;
        
        while(doit){
            GetResponse response = channel.basicGet(queueName,true);
            
            if(response == null){
            
            }
            else{
                AMQP.BasicProperties props = response.getProps();
                diff=Math.max((new Date().getTime()-props.getTimestamp().getTime()),diff);
                String message = new String(response.getBody());
                
                long deliveryTag = response.getEnvelope().getDeliveryTag();
                
                i++;
                if(i >= numofmessages && numofmessages>=max_mess){
                    System.out.println("The max time in seconds for " +numofmessages+ " messages is"+diff/1000);
                    doit = false;
                }
                else if(i >= numofmessages){
                    System.out.println("The max time in seconds for " +numofmessages+ " messages is"+diff/1000);
                    i = 0;
                    numofmessages*=10;
                }
            }
        }
        
    }
}
