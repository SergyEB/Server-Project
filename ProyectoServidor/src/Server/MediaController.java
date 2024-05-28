/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.util.List;
import java.io.File;
import java.util.ArrayList;

/**
 * This class handles the multimedia logic.
 *
 * @author sergi
 */
class MediaController {

    final static String multimediaPath = "multimedia";

    /**
     * Get the video list in the server.
     *
     * @return List of videos.
     */
    public static List<File> getVideos() {

        List<File> videoList = new ArrayList<>();
        File videoFolder = new File(multimediaPath, "videos");

        if (videoFolder.exists() && videoFolder.isDirectory()) {
            File[] videos = videoFolder.listFiles();
            if (videos != null) {
                for (File video : videos) {
                    System.out.println("Video encontrado: " + video.getName());
                    if ((video.isFile()) && (isVideo(video.getName()))) {
                        videoList.add(video);
                        System.out.println("Video añadido: " + video.getName());
                    }

                }
            }

        }
        return videoList;
    }

    /**
     * Get the songs list in the server.
     *
     * @return List of songs.
     */
    static List<File> getSongs() {
        List<File> songList = new ArrayList<>();
        File songFolder = new File(multimediaPath, "canciones");

        if (songFolder.exists() && songFolder.isDirectory()) {
            File[] songs = songFolder.listFiles();
            if (songs != null) {
                for (File song : songs) {
                    System.out.println("Cancion encontrada: " + song.getName());
                    if ((song.isFile()) && (isSong(song.getName()))) {
                        songList.add(song);
                        System.out.println("Video añadido: " + song.getName());
                    }
                }

            }
        }
        return songList;
    }

    /**
     * Get the docs list in the server.
     *
     * @return List of docs.
     */
    static List<File> getDocs() {
        List<File> docList = new ArrayList<>();
        File docFolder = new File(multimediaPath, "documentos");

        if (docFolder.exists() && docFolder.isDirectory()) {
            File[] docs = docFolder.listFiles();
            if (docs != null) {
                for (File doc : docs) {
                    System.out.println("Documento encontrado: " + doc.getName());
                    if (doc.isFile()) {
                        docList.add(doc);
                        System.out.println("Documento añadido: " + doc.getName());
                    }

                }
            }

        }
        return docList;

    }

    /**
     * Get the admin's video list in the server.
     *
     * @return List of videos.
     */
    static List<File> getVideosAdmin() {
        List<File> videoAdminList = new ArrayList<>();
        File videoAdminFolder = new File(multimediaPath, "videos_admin");

        if (videoAdminFolder.exists() && videoAdminFolder.isDirectory()) {
            File[] videos = videoAdminFolder.listFiles();
            if (videos != null) {
                for (File video : videos) {
                    System.out.println("Video encontrado: " + video.getName());
                    if ((video.isFile()) && (isVideo(video.getName()))) {
                        videoAdminList.add(video);
                        System.out.println("Video añadido: " + video.getName());
                    }

                }
            }

        }
        return videoAdminList;
    }

    /**
     * Get the admin's songs list in the server.
     *
     * @return List of songs.
     */
    static List<File> getSongsAdmin() {
        List<File> songAdminList = new ArrayList<>();
        File songAdminFolder = new File(multimediaPath, "canciones_admin");

        if (songAdminFolder.exists() && songAdminFolder.isDirectory()) {
            File[] songs = songAdminFolder.listFiles();
            if (songs != null) {
                for (File song : songs) {
                    System.out.println("Cancion encontrada: " + song.getName());
                    if ((song.isFile()) && (isSong(song.getName()))) {
                        songAdminList.add(song);
                        System.out.println("Video añadido: " + song.getName());
                    }
                }

            }
        }
        return songAdminList;

    }

    /**
     * Get the admin's docs list in the server.
     *
     * @return List of docs.
     */
    static List<File> getDocAdmin() {
        List<File> docAdminList = new ArrayList<>();
        File docAdminFolder = new File(multimediaPath, "docs_admin");

        if (docAdminFolder.exists() && docAdminFolder.isDirectory()) {
            File[] docs = docAdminFolder.listFiles();
            if (docs != null) {
                for (File doc : docs) {
                    System.out.println("Documento encontrado: " + doc.getName());
                    if (doc.isFile()) {
                        docAdminList.add(doc);
                        System.out.println("Documento añadido: "
                                + doc.getName());
                    }

                }
            }

        }
        return docAdminList;

    }

    /**
     * Check if the file is a video.
     *
     * @param videoName
     * @return true if is a video.
     */
    public static boolean isVideo(String videoName) {
        String[] videoExtensions = {".mp4", ".avi", ".mkv", ".mov", ".webm"};

        for (String videoExtension : videoExtensions) {

            if (videoName.endsWith(videoExtension)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the file is a song.
     *
     * @param videoName
     * @return true if is a song.
     */
    private static boolean isSong(String songName) {
        String[] songExtensions = {"aac", "aiff", "flac", "mid", "midi", "mp3",
            "ogg", "wav"};
        for (String songExtension : songExtensions) {

            if (songName.endsWith(songExtension)) {
                return true;
            }
        }
        return false;
    }

}
