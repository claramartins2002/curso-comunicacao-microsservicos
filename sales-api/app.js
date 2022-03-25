import express from "express";
import { connect } from "./src/config/db/mongoDbConfig.js";
import { connectRabbitMq } from "./src/config/rabbitmq/RabbitConfig.js";
import { createInitialData } from "./src/config/db/initialData.js";
import checkToken from "./src/config/auth/checkToken.js";
import { sendProductStockUpdateQueue } from "./src/modules/product/rabbitmq/productStockUpdateSender.js";
import router from "./src/modules/sales/routes/OrderRoutes.js";
import { listenToConfirmationListener } from "./src/modules/sales/rabbitmq/salesConfirmationListener.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;
app.use(express.json());

connect();
app.use(checkToken);
app.use(router);

connectRabbitMq();
listenToConfirmationListener();

app.get("api/initial-data", async (req, res)=>{
    await createInitialData();
    return res.json({message: "Data created"});
});


app.get('/teste', (req, res) => {

    try{
        sendProductStockUpdateQueue([{
            productId: 100,
            quantity: 1
        }]);
        return res.status(200).json({status: 200});
    }
    catch(err){
        console.log(err);
        return res.status(500).json({error: trues});
    }
});

app.get("/api/status", async (req, res) => {
   
return res.status(200).json({
service: "Sales-API",
 status: "UP",
 httpStatus: 200
});
});

   
app.listen(PORT, () => {
console.info('Server started sucessfully at port '+PORT);
});

