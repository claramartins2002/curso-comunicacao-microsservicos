import jwt from "jsonwebtoken";
import {promisify} from "util";
import * as secrets from "../secrets/secrets.js";
import * as httpStatus from "../httpStatus.js";

import accessTokenException from './accessTokenException.js';

const emptySpace = " ";
const empty = "";

export default async (req, res, next) => {
try {
const { authorization } = req.headers;
if(!authorization){
throw new accessTokenException(
httpStatus.UNAUTHORIZED,
"Access token must be informed!!!!!"
);
}

let AccessToken = authorization;
if(AccessToken.includes(emptySpace)){
AccessToken = AccessToken.split(emptySpace)[1];
}else {
AccessToken = authorization;
}
const decoded = await promisify(jwt.verify)(AccessToken, secrets.API_SECRET);
req.authUser = decoded.authUser;
return next();
}
catch(err){
const status = err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR;
return res.status(status).json({
status,
message: err.message
});
}
}

