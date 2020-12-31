package ca.datamagic.noaa.dto;

public class ImageDTO {
    private Integer _width = null;
    private Integer _height = null;
    private Integer _imageType = null;
    private int[] _pixels = null;

    public Integer getWidth() {
        return _width;
    }

    public Integer getHeight() {
        return _height;
    }

    public Integer getImageType() {
        return _imageType;
    }

    public int[] getPixels() {
        return _pixels;
    }

    public void setWidth(Integer newVal) {
        _width = newVal;
    }

    public void setHeight(Integer newVal) {
        _height = newVal;
    }

    public void setImageType(Integer newVal) {
        _imageType = newVal;
    }

    public void setPixels(int[] newVal) {
        _pixels = newVal;
    }
}
