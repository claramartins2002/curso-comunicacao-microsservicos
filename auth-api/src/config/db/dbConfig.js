import Sequelize from "sequelize";
import { DB_NAME } from "../constants/secrets.js";
import { DB_PASSWORD } from "../constants/secrets.js";
import { DB_USER, DB_HOST, DB_PORT} from "../constants/secrets.js";
const sequelize = new Sequelize(DB_NAME, DB_USER, DB_PASSWORD,  {
host: DB_HOST,
port: DB_PORT,
dialect: "postgres",
quoteIdentifiers: false,
define: {
syncOnAssociation: true,
timestamps: false,
underscored: true,
underscoredAll: true,
freezeTableName: true
},
pool: {
    acquire: 180000
}
});

sequelize
.authenticate()
.then(() => {
console.info('Connection has been established!')
})
.catch(err => {
console.error("Enable to connect to the database because "+err.message);
});

export default sequelize;


