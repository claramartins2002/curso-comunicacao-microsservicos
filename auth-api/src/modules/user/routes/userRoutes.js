import {Router} from "express";
import userController from "../controller/userController.js";
import checkToken from "../../../config/auth/checkToken.js";
import { createInitialData } from "../../../config/db/inicialData.js";

const router = new Router();
router.get("/api/initial-data", (req, res) => {
    createInitialData();
    return res.json({ message: "Data created." });
  });
router.post('/api/user/auth', userController.getAcessToken);
router.use(checkToken);
router.get('/api/user/email/:email', userController.findByEmail);

export default router;