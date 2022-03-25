import express from "express";
import { createInitialData } from "./src/config/db/inicialData.js";
import userRoutes from "./src/modules/user/routes/userRoutes.js";
const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

app.use(express.json());
app.use(userRoutes);



app.get('/api/status', (req, res) => {
return res.json({
service: "Auth-API",
status: "up",
httpStatus: 200
});
});

app.listen(PORT, () => {
console.info('Server started sucessfully at port ${PORT}');
});