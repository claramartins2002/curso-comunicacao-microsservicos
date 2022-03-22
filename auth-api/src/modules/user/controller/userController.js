import userService from "../service/userService.js";

class userController {

async getAcessToken(req, res){
let acessToken = await userService.getAcessToken(req);
return res.status(acessToken.status).json(acessToken);
}
async findByEmail(req, res){
let user = await userService.findByEmail(req);
return res.status(user.status).json(user);
}

}

export default new userController();