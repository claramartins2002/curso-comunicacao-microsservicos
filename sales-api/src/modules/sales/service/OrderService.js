import OrderRepository from "../repository/OrderRepository.js";
import { sendProductStockUpdateQueue } from "../../product/rabbitmq/productStockUpdateSender.js";
import { ACCEPTED, REJECTED, PENDING } from "../status/OrderStatus.js";
import { INTERNAL_SERVER_ERROR } from "../../../config/httpStatus.js";
import OrderException from "../exception/OrderException.js";
import { BAD_REQUEST, SUCCESS } from "../../../config/httpStatus.js";
import ProductClient from "../../product/client/ProductClient.js";
class OrderService {
async createOrder(req){
    try {
        let orderData = req.body;
        //console.log(orderData);
        this.validateOrderData(orderData);
        const {authUser} = req;
        const {authorization} = req.headers;
        let order = { 
            status: PENDING,
            user: authUser,
            createdAt: new Date(),
            updatedAt: new Date(),
            products: orderData.products

        };
       await this.validateProductStock(order, authorization);
        let createdOrder = await OrderRepository.save(order);
        this.sendMessage(createdOrder);
        return {
            status:  SUCCESS,
            createdOrder,
        }
        
    } catch (err) {
        return {
            status: err.status ? err.status : INTERNAL_SERVER_ERROR,
        message: err.message
        };
    }
}
async updateOrder(orderMessage){
    try {
        const order = JSON.parse(orderMessage);
if(order.salesId && order.status){
    
        let existingOrder = await OrderRepository.findById(order.salesId);
        if(existingOrder && existingOrder.status !== order.status){
            existingOrder.status = order.status;
            existingOrder.updatedAt = new Date();
            await OrderRepository.save(existingOrder);
        }
    
    else {
        console.warn('The order message was not complete...');
    }
}
    } catch (error) {
        console.error("Could not parse order message from queue");
    }
}

validateOrderData(data){
if(!data || !data.products){
    throw new OrderException(BAD_REQUEST, 'The products must be informed!!!!!');
}
}
async validateProductStock(order, token) {
    let stockIsOut = await ProductClient.checkProductStock(order.products, token);
    if(stockIsOut){
        throw new OrderException(BAD_REQUEST, 'The stock is out for the products!!!!!!');
    }
}

sendMessage(order) {
    const message = {
        saleId: order.id,
        products: order.products
    };
sendProductStockUpdateQueue(message);

}

async findById(req){
    const {id} = req.params;
    try {
        this.validateInformedId(id);
        const existingOrder = await OrderRepository.findById(id);
        if(!existingOrder){
            throw new OrderException(BAD_REQUEST, "The order was not found!!!!!!");
        }
        return {
            status: SUCCESS, 
            existingOrder
        } ;

    } catch (err) {
        return {status: err.status ? err.status : INTERNAL_SERVER_ERROR,
        message: err.message
        }
    }
}

validateInformedId(id) {
if(!id){
    throw new OrderException(BAD_REQUEST, "The order Id must be informed!!!!!!!!");
}
}
}

export default new OrderService();