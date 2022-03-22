import express from "express";
import * as db from "../auth-api/src/config/db/inicialData.js";
import userRoutes from "./src/modules/user/routes/userRoutes.js";
import checkToken from "./src/config/auth/checkToken.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

db.createInitialData();

app.get('/api/status', (req, res) => {
return res.json({
service: "Auth-API",
status: "up",
httpStatus: 200
});
});

app.use(express.json());
app.use(userRoutes);


app.listen(PORT, () => {
console.info('Server started sucessfully at port ${PORT}');
});