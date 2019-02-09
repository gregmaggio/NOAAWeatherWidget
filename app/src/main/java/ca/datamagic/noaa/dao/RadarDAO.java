package ca.datamagic.noaa.dao;

import java.util.Hashtable;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;

public class RadarDAO {
    private static Logger _logger = LogFactory.getLogger(RadarDAO.class);
    private static Hashtable<String, String> _wfoToRadar = null;

    private static synchronized Hashtable<String, String> getWfoToRadar() {
        if (_wfoToRadar == null) {
            Hashtable<String, String> wfoToRadar = new Hashtable<String, String>();
            wfoToRadar.put("AJK","ACG");
            wfoToRadar.put("BMX","BMX");
            wfoToRadar.put("MOB","MOB");
            wfoToRadar.put("HUN","HTX");
            wfoToRadar.put("TAE","TLH");
            wfoToRadar.put("JAN","DGX");
            wfoToRadar.put("LZK","LZK");
            wfoToRadar.put("TSA","INX");
            wfoToRadar.put("MEG","NQA");
            wfoToRadar.put("SHV","SHV");
            wfoToRadar.put("PUB","PUX");
            wfoToRadar.put("GLD","GLD");
            wfoToRadar.put("BOU","FTG");
            wfoToRadar.put("BOX","BOX");
            wfoToRadar.put("FFC","FFC");
            wfoToRadar.put("JAX","JAX");
            wfoToRadar.put("GSP","GSP");
            wfoToRadar.put("CAE","CAE");
            wfoToRadar.put("DMX","DMX");
            wfoToRadar.put("DVN","DVN");
            wfoToRadar.put("FSD","FSD");
            wfoToRadar.put("OAX","OAX");
            wfoToRadar.put("ARX","ARX");
            wfoToRadar.put("PAH","PAH");
            wfoToRadar.put("ILX","ILX");
            wfoToRadar.put("LSX","LSX");
            wfoToRadar.put("LOT","LOT");
            wfoToRadar.put("IND","IND");
            wfoToRadar.put("IWX","IWX");
            wfoToRadar.put("ILN","ILN");
            wfoToRadar.put("LMK","LVK");
            wfoToRadar.put("DDC","DDC");
            wfoToRadar.put("ICT","ICT");
            wfoToRadar.put("GID","UEX");
            wfoToRadar.put("TOP","TWX");
            wfoToRadar.put("EAX","EAX");
            wfoToRadar.put("SGF","SGF");
            wfoToRadar.put("JKL","JKL");
            wfoToRadar.put("RLX","RLX");
            wfoToRadar.put("PBZ","PBZ");
            wfoToRadar.put("GRR","GRR");
            wfoToRadar.put("MQT","MQT");
            wfoToRadar.put("ABR","ABR");
            wfoToRadar.put("TFX","TFX");
            wfoToRadar.put("BYZ","BYX");
            wfoToRadar.put("GGW","GGW");
            wfoToRadar.put("RAH","RAX");
            wfoToRadar.put("RNK","FCX");
            wfoToRadar.put("MRX","MRX");
            wfoToRadar.put("MHX","MHX");
            wfoToRadar.put("BIS","MBX");
            wfoToRadar.put("CYS","CYS");
            wfoToRadar.put("LBF","LNX");
            wfoToRadar.put("BTV","CXX");
            wfoToRadar.put("GYX","GYX");
            wfoToRadar.put("ABQ","ABX");
            wfoToRadar.put("LKN","LRX");
            wfoToRadar.put("BGM","BGM");
            wfoToRadar.put("BUF","BUF");
            wfoToRadar.put("ALY","ENX");
            wfoToRadar.put("CLE","CLE");
            wfoToRadar.put("OUN","TLX");
            wfoToRadar.put("AMA","AMA");
            wfoToRadar.put("BOI","CBX");
            wfoToRadar.put("CTP","CCX");
            wfoToRadar.put("UNR","UDX");
            wfoToRadar.put("OHX","OHX");
            wfoToRadar.put("SJT","SJT");
            wfoToRadar.put("LUB","LBB");
            wfoToRadar.put("FWD","GRK");
            wfoToRadar.put("MAF","MAF");
            wfoToRadar.put("EWX","DFX");
            wfoToRadar.put("HGX","HGX");
            wfoToRadar.put("LCH","LCH");
            wfoToRadar.put("CRP","CRP");
            wfoToRadar.put("SJU","JUA");
            wfoToRadar.put("MKX","MKX");
            wfoToRadar.put("GRB","GRB");
            wfoToRadar.put("SLC","MTX");
            wfoToRadar.put("MLB","MLB");
            wfoToRadar.put("TBW","TBW");
            wfoToRadar.put("KEY","BYX");
            wfoToRadar.put("GUM","GUA");
            wfoToRadar.put("AFG","APD");
            wfoToRadar.put("FGZ","FSX");
            wfoToRadar.put("PSR","IWA");
            wfoToRadar.put("TWC","EMX");
            wfoToRadar.put("VEF","ESX");
            wfoToRadar.put("MTR","MUX");
            wfoToRadar.put("SGX","SOX");
            wfoToRadar.put("LOX","VTX");
            wfoToRadar.put("HNX","HNX");
            wfoToRadar.put("REV","RGX");
            wfoToRadar.put("STO","DAX");
            wfoToRadar.put("GJT","GJX");
            wfoToRadar.put("HFO","HKM");
            wfoToRadar.put("MSO","MSX");
            wfoToRadar.put("OTX","OTX");
            wfoToRadar.put("PIH","SFX");
            wfoToRadar.put("CAR","CBW");
            wfoToRadar.put("PDT","PDT");
            wfoToRadar.put("RIW","RIW");
            wfoToRadar.put("OKX","OKX");
            wfoToRadar.put("ILM","LTX");
            wfoToRadar.put("EPZ","EPZ");
            wfoToRadar.put("CHS","CLX");
            wfoToRadar.put("BRO","BRO");
            wfoToRadar.put("AFC","ABC");
            wfoToRadar.put("SEW","ATX");
            wfoToRadar.put("AKQ","AKQ");
            wfoToRadar.put("LWX","LWX");
            wfoToRadar.put("PHI","DIX");
            wfoToRadar.put("APX","APX");
            wfoToRadar.put("FGF","MVX");
            wfoToRadar.put("PQR","RTX");
            wfoToRadar.put("EKA","BHX");
            wfoToRadar.put("DLH","DLH");
            wfoToRadar.put("MPX","MPX");
            wfoToRadar.put("MFL","AMX");
            wfoToRadar.put("MFR","MAX");
            wfoToRadar.put("LIX","LIX");
            wfoToRadar.put("DTX","DTX");
            _wfoToRadar = wfoToRadar;
        }
        return _wfoToRadar;
    }

    public String getRadar(String wfo) {
        Hashtable<String, String> wfoToRadar = getWfoToRadar();
        if ((wfo != null) && (wfo.length() > 0)) {
            wfo = wfo.toUpperCase();
            if (wfoToRadar.containsKey(wfo)) {
                return wfoToRadar.get(wfo);
            }
        }
        return null;
    }
}
