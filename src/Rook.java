public class Rook {
    public static long pattern[][] = new long[8][255];
    public static long fpattern[][] = new long[8][255];
    public static void INIT(){
        long position = 1L;
        long positionF = 1L;
        for(int i = 0; i < 8; i++){
            for(int j = 1; j < 256; j++){
                //right
                long pos = position << 1;
                long posF = positionF << 8;
                while(pos < 256){
                    if((pos & j) == 0){
                        pattern[i][j - 1] |= pos;
                        fpattern[i][j - 1] |= posF;
                    }
                    else{
                        pattern[i][(int)j - 1] |= pos;
                        fpattern[i][(int)j - 1] |= posF;
                        break;
                    }
                    pos <<= 1;
                    posF <<= 8;
                }
                //left
                pos = position >>> 1;
                posF = positionF >>> 8;
                while(pos > 0){
                    if((pos & j ) == 0){
                        pattern[i][j - 1] |= pos;
                        fpattern[i][j- 1] |= posF;
                    }
                    else{
                        pattern[i][j - 1] |= pos;
                        fpattern[i][j- 1] |= posF;
                        break;
                    }
                    pos >>>= 1;   
                    posF >>>= 8;   
                }
            }
            position <<= 1;
            positionF <<= 8;
        }

        // position = 1L;
        // long positionF = 1L;
        // for(int i = 0; i < 8; i++){
        //     for(int j = 1; j < 256; j++){
        //         //right
        //         long pos = position << 1;
        //         long posl = positionF << 8;
        //         while(pos < 256){
        //             if((pos & j ) == 0){
        //                 fpattern[i][j - 1] |= posl;
        //             }
        //             else{
        //                 fpattern[i][(int)j - 1] |= posl;
        //                 break;
        //             }
        //             pos <<= 1;
        //             posl <<= 8;
        //         }
        //         //left
        //         pos = position >>> 1;
        //         posl = positionF >>> 8;
        //         while(pos > 0){
        //             if((pos & j ) == 0)pattern[i][(int)j - 1] |= pos;
        //             else{
        //                 pattern[i][(int)j - 1] |= pos;
        //                 break;
        //             }
        //             pos >>>= 1;   
        //             pos >>>= 8;   
        //         }
        //     }
        //     position <<= 1;
        //     position <<= 8;
        
     }

    public static long getMoves(int pos, long hState, int vState){
        return pattern[pos % 8][(int)(hState & 255) - 1 ] << (8 * (pos / 8)) 
        | fpattern[pos / 8][vState - 1] << (pos % 8);
    }
    // static int count = 0;
    // public static void display(long val){
    //     long position;
    //     for(int Y = 7; Y > -1; Y--){
    //         position = 1L << 8 * Y;
    //         for(int X = 0; X < 8; X++){
    //             if((position & val) != 0) System.out.print("*, ");
    //             //else if (X == count)System.out.print('N,');
    //             else System.out.print("_, ");
    //             position <<= 1;
    //         }
    //         System.out.println();
    //     }
    //     System.out.println();
    //     //System.out.println(count);
    //     count++;
    // }
    // public static void main(String args[]){
    //     INIT();
    //         for(int j = 0; j < 255; j++){
    //             System.out.println("___________________");
    //             display(fpattern[2][j]);
    //             display(j + 1);
    //             System.out.println(j + 1);
    //             System.out.println("+++++++++++++++++");
    //         }
    // }
    public static long[][] getPattern(){
        INIT();
        return pattern;
    }
}
