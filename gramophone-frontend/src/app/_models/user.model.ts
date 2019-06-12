export class User {
  constructor(
    public email: string,
    public password: string,
    public role: string,
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
