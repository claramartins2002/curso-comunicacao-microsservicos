import amqp from "amqplib/callback_api.js";
import { PRODUCT_TOPIC, PRODUCT_STOCK_UPDATE_QUEUE,
     PRODUCT_STOCK_UPDATE_ROUTING_KEY, SALES_CONFIRMATION_QUEUE, 
     SALES_CONFIRMATION_ROUTING_KEY } from "./Queue.js"; 
import { RABBIT_MQ_URL } from "../secrets/secrets.js";
import { listenToConfirmationListener } from "../../modules/sales/rabbitmq/salesConfirmationListener.js";

const HALF_SECOND = 500;
const HALF_MINUTE = 30000;
const CONTAINER_WNV = "container";

export async function connectRabbitMq() {
const env = process.env.NODE_ENV;
if(CONTAINER_WNV ===env){
    console.info("Waiting for rabbitmq to start...");
    setInterval(() => {
        connectRabbitMqAndCreateQueues();

    }, HALF_MINUTE);
}


    }

    function connectRabbitMqAndCreateQueues() {
        amqp.connect(RABBIT_MQ_URL, {timeout: 180000}, (error, connection) => {
            if(error){
                throw error;
            }
            console.info("Starting RabbitMQ...");
            createQueue(connection, 
                PRODUCT_STOCK_UPDATE_QUEUE,
                 PRODUCT_STOCK_UPDATE_ROUTING_KEY, 
                 PRODUCT_TOPIC
                 );
            createQueue(connection,
                 SALES_CONFIRMATION_QUEUE,
                  SALES_CONFIRMATION_ROUTING_KEY, 
                  PRODUCT_TOPIC
                  );
    
            setTimeout(function () {
                connection.close();
                }, HALF_SECOND);                
        });

    }
        
    

    function createQueue(connection, queue, routingKey, topic){
        connection.createChannel((error, channel) => {
                channel.assertExchange(topic, 'topic', { durable: true});
                channel.assertQueue(queue, { durable: true});
                channel.bindQueue({queue, topic, routingKey});
            });
    }

