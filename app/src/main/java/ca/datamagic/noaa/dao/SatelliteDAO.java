package ca.datamagic.noaa.dao;

public class SatelliteDAO {
    //PR: https://cdn.star.nesdis.noaa.gov/GOES16/ABI/SECTOR/pr/GEOCOLOR/
    //AK: https://cdn.star.nesdis.noaa.gov/GOES17/ABI/SECTOR/ak/GEOCOLOR/
    //US: https://cdn.star.nesdis.noaa.gov/GOES16/ABI/CONUS/GEOCOLOR/
    //HI: https://cdn.star.nesdis.noaa.gov/GOES17/ABI/SECTOR/hi/GEOCOLOR/
    public static String getSatelliteImage(String state) {
        if (state != null) {
            if (state.compareToIgnoreCase("PR") == 0) {
                return "https://cdn.star.nesdis.noaa.gov/GOES16/ABI/SECTOR/pr/GEOCOLOR/latest.jpg";
            } else  if (state.compareToIgnoreCase("HI") == 0) {
                return "https://cdn.star.nesdis.noaa.gov/GOES17/ABI/SECTOR/hi/GEOCOLOR/latest.jpg";
            } else  if (state.compareToIgnoreCase("AK") == 0) {
                return "https://cdn.star.nesdis.noaa.gov/GOES17/ABI/SECTOR/ak/GEOCOLOR/latest.jpg";
            }
        }
        return "https://cdn.star.nesdis.noaa.gov/GOES16/ABI/CONUS/GEOCOLOR/latest.jpg";
    }
}
