package com.ipiecoles.java.eval.th330.controller;

import com.ipiecoles.java.eval.th330.model.Album;
import com.ipiecoles.java.eval.th330.model.Artist;
import com.ipiecoles.java.eval.th330.service.AlbumService;
import com.ipiecoles.java.eval.th330.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityExistsException;
import java.util.List;

@Controller
@RequestMapping(value = "/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private AlbumService albumService;


    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}"
    )
    public ModelAndView getSingleArtistById(
            @PathVariable Long id
    ) {
        ModelAndView modelAndView = new ModelAndView("detail");
        modelAndView.addObject("artist", artistService.findById(id));
        return modelAndView;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "",
            params = "name"
    )
    public ModelAndView findAllArtistsByContainingName(
            @RequestParam String name
    ) {
        ModelAndView modelAndView = new ModelAndView("liste");
        modelAndView.addObject("artists", artistService.findByNameLikeIgnoreCase(name, 0, artistService.countAllArtists().intValue(), "name", "ASC"));
        return modelAndView;
    }


    @RequestMapping(
            method = RequestMethod.GET,
            value = ""
    )
    public ModelAndView findAllArtists(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sortProperty,
            @RequestParam String sortDirection
    ) {
        Integer artistsCount = (page * size) + 1;
        ModelAndView modelAndView = new ModelAndView("liste");
        modelAndView.addObject("artistsCount", artistsCount);
        modelAndView.addObject("artists", artistService.findAllArtists(page, size, sortProperty, sortDirection));
        return modelAndView;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/new"
    )
    public ModelAndView getFormNewArtist() {
        ModelAndView modelAndView = new ModelAndView("detail");
        modelAndView.addObject("artist", new Artist());
        return modelAndView;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public RedirectView addOrUpdateArtist(
            Artist artist,
            RedirectAttributes redirectAttributes
    ){
        if (artistService.existsByName(artist.getName().trim())) {
            throw new EntityExistsException("L'artiste " + artist.getName() + " existe déjà dans la base !");
        }

        if(artist.getId() == null){
            artistService.creerArtiste(artist);
            redirectAttributes.addFlashAttribute("flashMessage", "Création de l'artiste " + artist.getName() + " réalisée avec succès !");
        }
        else
        {

            if (artistService.existsById(artist.getId())) {
                artistService.updateArtiste(artist.getId(), artist);
                redirectAttributes.addFlashAttribute("flashMessage", "Modification de l'artiste " + artist.getName() +  " réalisée avec succès !");
            }
        }
        return new RedirectView("/artists/" + artist.getId());
    }


    @RequestMapping(
            method = RequestMethod.GET,
            value = "/delete/{id}"
    )
    public RedirectView deleteArtist(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        List<Album> albums = albumService.getAllAlbumsByArtistId(id);
        if (!albums.isEmpty()) {
            albums.forEach(album -> albumService.deleteAlbum(album.getId()));
        }
        artistService.deleteArtist(id);
        redirectAttributes.addFlashAttribute("flashMessage", "Suppresion effectuée avec succès !");
        return new RedirectView("/artists?page=0&size=10&sortProperty=name&sortDirection=ASC");
    }
}