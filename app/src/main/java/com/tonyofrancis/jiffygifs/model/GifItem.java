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
        private Original fixed_width;

        public Original getOriginal() {
            return original;
        }

        public void setOriginal(Original original) {
            this.original = original;
        }

        public Original getFixed_width() {
            return fixed_width;
        }

        public void setFixed_width(Original fixed_width) {
            this.fixed_width = fixed_width;
        }
    }

    /**Class use to hold the original information for the gif*/
    public static class Original {

        private String url;
        private int width;
        private int height;
        private String webp;

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

        public String getWebp() {
            return webp;
        }

        public void setWebp(String webp) {
            this.webp = webp;
        }
    }

}
