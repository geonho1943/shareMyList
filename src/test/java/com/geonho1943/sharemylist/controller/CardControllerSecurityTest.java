package com.geonho1943.sharemylist.controller;

import com.geonho1943.sharemylist.dto.CardDto;
import com.geonho1943.sharemylist.dto.PlaylistDto;
import com.geonho1943.sharemylist.dto.UserDto;
import com.geonho1943.sharemylist.service.CardService;
import com.geonho1943.sharemylist.service.PlaylistService;
import com.geonho1943.sharemylist.service.RecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CardController.class)
@Import({SessionUserHelper.class, RequestSecurityHelper.class})
class CardControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @MockBean
    private PlaylistService playlistService;

    @MockBean
    private RecordService recordService;

    @Test
    void linkUploadAddsCsrfTokenToModel() throws Exception {
        MockHttpSession session = createLoggedInSession();
        when(playlistService.getPlaylistByUser(1)).thenReturn(List.of(createPlaylistDto(1, "My Playlist")));

        mockMvc.perform(get("/linkupload").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("playlist/linkupload"))
                .andExpect(model().attributeExists("csrfToken"))
                .andExpect(model().attributeExists("playlistByUser"));
    }

    @Test
    void submitYoutubeLinkAcceptsValidCsrfTokenAndSameOriginRequest() throws Exception {
        MockHttpSession session = createLoggedInSession();
        session.setAttribute("submitYoutubeLinkCsrfToken", "valid-token");

        CardDto cardDto = new CardDto();
        cardDto.setCardYoutId("video-id");
        when(cardService.parseYoutubeVideoId("https://youtu.be/example")).thenReturn("video-id");
        when(cardService.getMetadataByYoutubeApi("video-id")).thenReturn(cardDto);

        mockMvc.perform(post("/submitYoutubeLink")
                        .session(session)
                        .header("Origin", "http://localhost")
                        .param("youtubeLink", "https://youtu.be/example")
                        .param("playlistIdx", "1")
                        .param("csrfToken", "valid-token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/playlistInfo/1"));

        verify(cardService).saveCardToPlaylist(any(CardDto.class), eq(1), eq(1));
        verify(recordService).recordCreateCard(1);
    }

    @Test
    void submitYoutubeLinkRejectsInvalidCsrfToken() throws Exception {
        MockHttpSession session = createLoggedInSession();
        session.setAttribute("submitYoutubeLinkCsrfToken", "valid-token");
        when(playlistService.getPlaylistByUser(1)).thenReturn(List.of(createPlaylistDto(1, "My Playlist")));

        mockMvc.perform(post("/submitYoutubeLink")
                        .session(session)
                        .header("Origin", "http://localhost")
                        .param("youtubeLink", "https://youtu.be/example")
                        .param("playlistIdx", "1")
                        .param("csrfToken", "wrong-token"))
                .andExpect(status().isForbidden())
                .andExpect(view().name("playlist/linkupload"))
                .andExpect(model().attribute("error", "invalidCsrfToken"))
                .andExpect(model().attributeExists("csrfToken"));

        verify(cardService, never()).saveCardToPlaylist(any(CardDto.class), eq(1), eq(1));
    }

    @Test
    void submitYoutubeLinkRejectsCrossSiteRequest() throws Exception {
        MockHttpSession session = createLoggedInSession();
        session.setAttribute("submitYoutubeLinkCsrfToken", "valid-token");
        when(playlistService.getPlaylistByUser(1)).thenReturn(List.of(createPlaylistDto(1, "My Playlist")));

        mockMvc.perform(post("/submitYoutubeLink")
                        .session(session)
                        .header("Origin", "https://attacker.example")
                        .param("youtubeLink", "https://youtu.be/example")
                        .param("playlistIdx", "1")
                        .param("csrfToken", "valid-token"))
                .andExpect(status().isForbidden())
                .andExpect(view().name("playlist/linkupload"))
                .andExpect(model().attribute("error", "invalidSameSiteRequest"))
                .andExpect(model().attributeExists("csrfToken"));

        verify(cardService, never()).saveCardToPlaylist(any(CardDto.class), eq(1), eq(1));
    }

    @Test
    void submitYoutubeLinkReturnsInvalidRequestPageForUnauthenticatedCrossSiteRequest() throws Exception {
        mockMvc.perform(post("/submitYoutubeLink")
                        .header("Origin", "https://attacker.example")
                        .param("youtubeLink", "https://youtu.be/example")
                        .param("playlistIdx", "1"))
                .andExpect(status().isForbidden())
                .andExpect(view().name("error/invalidrequest"))
                .andExpect(model().attribute("error", "invalidSameSiteRequest"));
    }

    private MockHttpSession createLoggedInSession() {
        MockHttpSession session = new MockHttpSession();
        UserDto userDto = new UserDto();
        userDto.setUserIdx(1);
        userDto.setUserName("tester");
        session.setAttribute("checkedUserInfo", userDto);
        return session;
    }

    private PlaylistDto createPlaylistDto(int playlistIdx, String playlistName) {
        PlaylistDto playlistDto = new PlaylistDto();
        playlistDto.setPlaylistIdx(playlistIdx);
        playlistDto.setPlaylistName(playlistName);
        return playlistDto;
    }
}
