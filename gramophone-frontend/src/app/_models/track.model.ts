import {Genre} from './genre.model';
import {User} from './user.model';
import {CommentModel} from './comment.model';

export class Track {
  constructor(
    public id: number,
    public title: string,
    public wordAuthor: string,
    public musicAuthor: string,
    public locationOnServer: string,
    public downloadUrl: string,
    public genre: Genre,
    public listeningAmount: number,
    public performer: User,
    public cover: string,
    public likes: User[],
    public dislikes: User[],
    public comments: CommentModel[],
    public createAt?: Date,
  ) {}
}
