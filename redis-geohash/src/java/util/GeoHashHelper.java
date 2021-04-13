package java.util;

public class GeoHashHelper {

    public final double Max_Longitude = 90;
    public final double Min_Longitude =-90;

    public final double Max_Latitude = 180;
    public final double Min_Latitude = -180;

    private final int length = 20;

    private final double latUnit = (Max_Latitude-Min_Latitude)/(1<<20);
    private final double lngUnit = (Max_Longitude-Min_Longitude)/(1<<20);

    private final String[] base32Lookup=
            {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h",
                    "i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",};


    private void convert(double min, double max, double value, List<Character> list ){

        if(list.size()>(length-1)){
            return;
        }

        double mid=(max+min)/2;

        if(value<mid){
            list.add('0');
            convert(min,max,value,list);
        }else {
            list.add('1');
            convert(min,max,value,list);
        }

    }


    public String base32Encode(final String str){

        String unit="";
        StringBuilder sb=new StringBuilder();

        for(int start=0;start<str.length();start=start+5){
            unit=str.substring(start,start+5);
            sb.append(base32Lookup[convertoIndex(unit)]);
        }

        return sb.toString();
    }


    public int convertoIndex(String str){

        int length=str.length();
        int result=0;

        for(int index=0;index<length;index++){

            result += str.charAt(index) == '0'?0:1<<(length-1-index);
        }

        return result;

    }

    public String encode(double lat, double lng){

        List<Character> latList = new ArrayList<Character>();
        List<Character> lngList = new ArrayList<Character>();

        convert(Min_Latitude,Max_Latitude,lat,latList);
        convert(Min_Longitude,Max_Longitude,lng,lngList);

        StringBuilder sb=new StringBuilder();

        for(int index=0;index<latList.size();index++){

            sb.append(lngList.get(index)).append(latList.get(index));
        }

        return base32Encode(sb.toString());

    }


    public List<String> around(double lat, double lng){

        List<String> list=new ArrayList<String>();

        list.add(encode(lat,lng));
        list.add(encode(lat+latUnit,lng));
        list.add(encode(lat-latUnit,lng));
        list.add(encode(lat,lng+lngUnit));
        list.add(encode(lat,lng-lngUnit));
        list.add(encode(lat+latUnit,lng+lngUnit));
        list.add(encode(lat+latUnit,lng-lngUnit));
        list.add(encode(lat-latUnit,lng+lngUnit));
        list.add(encode(lat-latUnit,lng+lngUnit));

        return list;

    }




}
