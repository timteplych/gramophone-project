package com.geekbrains.gramophone.rest;


import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.services.TrackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/tracksearch")
@Api(tags = "Track search")
public class TrackSearchRestController {

    private TrackService trackService;

    @Autowired
    public void setTrackService(TrackService trackService) {
        this.trackService = trackService;
    }

    @ApiOperation(value = "Search tracks by title",
            notes = "Поиск треков по названию",
            response = ResponseEntity.class)
    @GetMapping("/by_title/{title}")
    public ResponseEntity<?> searchTracksByTitle(
            @ApiParam(value = "Title substring",
                    example = "Wind of change")
            @PathVariable String title){
        List<Track> trackList = trackService.findByTitle(title);
        return new ResponseEntity<>(trackList,HttpStatus.OK);
    }

    @ApiOperation(value = "Search tracks by author",
            notes = "Поиск треков по автору",
            response = ResponseEntity.class)
    @GetMapping("/by_author/{author}")
    public ResponseEntity<?> searchTracksByAuthor(
            @ApiParam(value = "Author name substring",
                    example = "Scorpions")
            @PathVariable String author){
        List<Track> trackList = trackService.findByAuthor(author);
        return new ResponseEntity<>(trackList, HttpStatus.OK);
    }

    @ApiOperation(value = "Search tracks by genre title",
            notes = "Поиск треков по названию жанра",
            response = ResponseEntity.class)
    @GetMapping("/by_genre_title/{title}")
    public ResponseEntity<?> searchTracksByGenreTitle(
            @ApiParam(value = "Genre title",
                    example = "Hard rock")
            @PathVariable String title){
        List<Track> trackList = trackService.findByGenreTitle(title);
        return new ResponseEntity<>(trackList, HttpStatus.OK);
    }

    @ApiOperation(value = "Search tracks by genre id",
            notes = "Поиск по идентификатору жанра",
            response = ResponseEntity.class)
    @GetMapping("/by_genre_id/{id}")
    public ResponseEntity<?> searchTracksByGenreTitle(
            @ApiParam(value = "Genre id",
                    example = "2")
            @PathVariable long id){
        List<Track> trackList = trackService.findByGenreId(id);
        return new ResponseEntity<>(trackList, HttpStatus.OK);
    }
}
