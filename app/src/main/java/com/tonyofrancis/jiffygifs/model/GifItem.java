package com.tonyofrancis.jiffygifs.model;

/**
 * Created by tonyofrancis on 5/20/16.
 * Model Class used to hold all information about the GIF
 */

public final class GifItem {

    private String id;
    private Images images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    /**Image class use to hold image information */
    public static class Images {

        private Original original;
        private Original original_still;

        public Original getOriginal() {
            return original;
        }

        public void setOriginal(Original original) {
            this.original = original;
        }

        public Original getOriginal_still() {
            return original_still;
        }

        public void setOriginal_still(Original original_stills) {
            this.original_still = original_stills;
        }
    }

    /**Class use to hold the original information for the gif*/
    public static class Original {

        private String url;
        private int width;
        private int height;
        private String mp4;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getMp4() {
            return mp4;
        }

        public void setMp4(String mp4) {
            this.mp4 = mp4;
        }
    }

}
