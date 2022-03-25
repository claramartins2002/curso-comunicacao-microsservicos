import Order from "../../modules/sales/models/Order.js";

export async function createInitialData() {

    await Order.collection.drop();

   await Order.create({
        products: [
            {
           productId: 100,
           quantity: 2 
        },
    {
        productId: 200,
        quantity: 1
    }],
    user: {
        id: '1',
        name: 'Clara Martins',
        email: 'clara@gmail.com'
    },
    status: 'APPROVED',
    createdAt: new Date(),
    updatedAt: new Date()
    });
    let initialData = await Order.find();

    console.info('Initial data was created: '+JSON.stringify(initialData, undefined, 4));

}