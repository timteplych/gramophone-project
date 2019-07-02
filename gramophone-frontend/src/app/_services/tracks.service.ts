import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Genre, Track} from '../_models';
import {BehaviorSubject, Observable} from 'rxjs';
import {API_URL} from '../../environments/environment';
import {map} from 'rxjs/operators';

@Injectable({providedIn: 'root'})
export class TracksService {
  public currentTrack: Observable<Track>;
  currentTrackSubj: BehaviorSubject<Track>;
  currentTrackListSubj: BehaviorSubject<Track[]>;
  public currentTrackList: Observable<Track[]>;

  constructor(private http: HttpClient) {
    this.currentTrackSubj = new BehaviorSubject<Track>(JSON.parse(localStorage.getItem('track')));
    this.currentTrack = this.currentTrackSubj.asObservable();
    this.currentTrackListSubj = new BehaviorSubject<Track[]>([]);
    this.currentTrackList = this.currentTrackListSubj.asObservable();
    this.getTrackList();
  }

  public get currentTrackValue(): Track {
    return this.currentTrackSubj.value;
  }


  getTrackList(page = 1): Observable<Track[]> {
    return this.http.get<Track[]>(`${API_URL}/tracks`, {params: new HttpParams().set('page', page.toString())})
      .pipe(map(tracks => {
        if (tracks) {
          this.currentTrackListSubj.next(tracks);
        }
        return tracks;
      }));
  }

  getTrackListWithSearchAndFilter(page = 1, searchTerm, genre): Observable<Track[]> {
    return this.http.get<Track[]>(`${API_URL}/tracks`,
      {params: new HttpParams().set('page', page.toString()).set('search', searchTerm).set('genre', genre)})
      .pipe(map(tracks => {
        if (tracks) {
          this.currentTrackListSubj.next(tracks);
        }
        return tracks;
      }));
  }

  getTrackById(id: number): Observable<Track> {
    return this.http.get<Track>(`${API_URL}/tracks/${id}`).pipe(map(track => {
      return track;
    }));
  }

  getGenreList(): Observable<Genre[]> {
    return this.http.get<Genre[]>(`${API_URL}/tracks/genres`);
  }

  createNewTrack(formData: FormData): Observable<any> {
    return this.http.post<any>(`${API_URL}/tracks`, formData);
  }


  updateTrackListeningAmount(id, amount): Observable<any> {
    return this.http.patch(`${API_URL}/tracks/${id}`, new HttpParams().set('listeningAmount', amount));
  }

  getUserTracks(id): Observable<Track[]> {
    return this.http.get<Track[]>(`${API_URL}/tracks/${id}/tracks`);
  }
}
