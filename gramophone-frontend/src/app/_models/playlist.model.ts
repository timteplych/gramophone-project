import {Track} from './track.model';
import {User} from './user.model';

export class Playlist {
  constructor(
    public id: number,
    public name: string,
    public tracks: Track[],
    public user: User
  ) {}
}
