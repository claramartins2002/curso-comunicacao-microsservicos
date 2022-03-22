import {Router} from "express";
import userController from "../controller/userController.js";
import checkToken from "../../../config/auth/checkToken.js";


const router = new Router();

router.post('/api/user/auth', userController.getAcessToken);
router.use(checkToken);
router.get('/api/user/email/:email', userController.findByEmail);

export default router;