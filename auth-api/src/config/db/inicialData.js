import bcrypt from 'bcrypt';
import User from '../../modules/user/model/User.js';

export async function createInitialData() {
await User.sync({force: true});
let pass = await bcrypt.hash('123456', 10);
let firstUser = await User.create({
name: 'Clara Martins',
email: 'clara@gmail.com',
password: pass
});
let secondUser = await User.create({
name: 'Maria Rios',
email: 'maria@gmail.com',
password: pass
});
}
