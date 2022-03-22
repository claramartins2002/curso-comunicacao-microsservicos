import userRepository from "../repository/userRepository.js";
import * as httpStatus from "../../../config/constants/httpStatus.js";
import userException from "../exception/userException.js";
import jwt from "jsonwebtoken";
import bcrypt from "bcrypt";
import * as secrets from "../../../config/constants/secrets.js";
class userService{

async findByEmail(req){
try {
const {email} = req.params;
const {authUser} = req;
this.validateRequestData(email);
let user = await userRepository.findByEmail(email);
this.validateUserNotFound(user);
this.validateAuthenticatedUser(user, authUser);
return {
status : httpStatus.SUCCESS,
user: {
id: user.id,
name: user.name,
email: user.email
}
};
}

catch(err){
return {
status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
message: err.message
};
}
}

validateRequestData(email){
if(!email){
throw new userException(
httpStatus.BAD_REQUEST,
'User email was not informed!!!');
}
}

validateUserNotFound(user){
if(!user){
throw new Error(
httpStatus.BAD_REQUEST, "User was not found!!!");
}}


async  getAcessToken(req){
try{
const {email, password} = req.body;
this.validateAcessTokenData(email, password);
let user = await userRepository.findByEmail(email);
this.validateUserNotFound(user);
await this.validatePassword(password, user.password);
const authUser = {id: user.id, name: user.name, email: user.email};
const acessToken = jwt.sign({authUser},secrets.API_SECRET,{expiresIn: "1d"});
return {
status: httpStatus.SUCCESS,
acessToken}

}
catch(err){
return {
status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
message: err.message
};
}
}
validateAcessTokenData(email, password){
if(!email || !password){
throw new userException(
httpStatus.UNAUTHORIZED,
"Email and password must be informed!!!!");
}}

validateAuthenticatedUser(user, authUser){
if(!authUser || user.id !== authUser.id){
throw new userException(httpStatus.FORBIDDEN, "You cannot access this data!!!")
}
}

async validatePassword(password, hashPassword){
if(!await bcrypt.compare(password, hashPassword)){
throw new userException(httpStatus.UNAUTHORIZED, "Password does not match!!!!!!");
}
}
}

export default new userService();
