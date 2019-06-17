import {Role} from './role.model';

export class User {
  constructor(
    public username: string,
    public email: string,
    public password: string,
    public roles: Role[],
    public token: string,
    public firstName?: string,
    public lastName?: string,
    public phone?: string,
    public createdAt?: bigint,
    public updatedAt?: bigint,
    public id?: string
  ) {
  }
}
