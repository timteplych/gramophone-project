import {User} from './user.model';
import {Track} from './track.model';

export class CommentModel {
  constructor(
    public id: number,
    public content: string,
    public likes: User[],
    public user: User,
    public track: Track,
  ) {
  }
}
