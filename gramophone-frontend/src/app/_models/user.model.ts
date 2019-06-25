import {Role} from './role.model';
import {Playlist} from './playlist.model';
import {InfoSinger} from './infoSinger.model';

export class User {
  constructor(
    public id: number,
    public username: string,
    public email: string,
    public password: string,
    public roles: Role[],
    public token: string,
    public avatar?: string,
    public activationCode?: string,
    public infoSinger?: InfoSinger,
    public playLists?: Playlist[],
    public subscribers?: User[],
    public subscription?: User[],
    public createdAt?: bigint,
    public updatedAt?: bigint
  ) {
  }
}
