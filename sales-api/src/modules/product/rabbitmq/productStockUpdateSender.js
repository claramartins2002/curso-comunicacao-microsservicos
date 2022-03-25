import amqp from "amqplib/callback_api.js";
import { RABBIT_MQ_URL } from "../../../config/secrets/secrets.js";
import { PRODUCT_STOCK_UPDATE_ROUTING_KEY, PRODUCT_TOPIC } from "../../../config/rabbitmq/Queue.js";


export function sendProductStockUpdateQueue(message) {
    amqp.connect(RABBIT_MQ_URL, (error, connection) => {
        if(error){
            throw error;
        }
      connection.createChannel((error, channel)=> {
        if(error){
            throw error;
        }
        let jsonStringMessage = JSON.stringify(message);

        console.info("Sending message to product update stock: "+jsonStringMessage);
        channel.publish(PRODUCT_TOPIC, PRODUCT_STOCK_UPDATE_ROUTING_KEY,
              Buffer.from(jsonStringMessage)
              );
              console.info("Message was sent successfully!!!!!!");
      });
    });

}