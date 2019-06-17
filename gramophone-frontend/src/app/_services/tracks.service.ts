import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Genre, Track} from '../_models';
import {Observable} from 'rxjs';
import {API_URL} from '../../environments/environment';

@Injectable({providedIn: 'root'})
export class TracksService {
  constructor(private http: HttpClient) {
  }


  getTrackList(): Observable<Track[]> {
    return this.http.get<Track[]>(`${API_URL}/tracks`);
  }

  getTrackById(id: number): Observable<Track> {
    return this.http.get<Track>(`${API_URL}/tracks/${id}`);
  }

  getGenreList(): Observable<Genre[]> {
    return this.http.get<Genre[]>(`${API_URL}/tracks/genres`);
  }
}
