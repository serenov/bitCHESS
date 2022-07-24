public class Bishop {
    // WASTE OF A LOT OF SPACE WITH REDUNDANT STATES 
    // DOESN'T MATTER BECAUSE IT IS SIGNIFICANTLY FASTER
    public static long Rdiagonal[][] = new long[64][];
    public static long Ldiagonal[][] = new long[64][];

    public static void INIT(){
        int Abs;
        int lim;
        long pos;
        long pos2;
        int k;
        for(int Y = 0; Y < 8; Y++){
            for(int X = 0; X < 8; X++){

                Abs = Math.abs(X - Y);
                lim = 8 - Abs;

                // if(X == 1)
                // X = 1;
                Rdiagonal[8 * Y + X] = new long[(int)Math.pow(2, lim)];

                for(int i = 1; i < Math.pow(2, lim); i++){
                    pos = 1L << ((Y + 1) * 8 + (X + 1));
                    pos2 = (X > Y)? 1L << (Y + 1): 1L << (X + 1);
                    k = (X > Y)? lim - Y: lim - X;
                    for(int j = 1; j < k; j++){
                        Rdiagonal[8 * Y + X][i] |= pos; 

                        if((pos2 & i) == 0){
                            pos <<= 9;
                            pos2 <<= 1;
                        }
                        else break;
                    } 
                    pos = 1L << ((Y - 1) * 8 + (X - 1));
                    pos2 = (X > Y)? 1L << (Y - 1): 1L << (X - 1);
                    k = (X > Y)?  Y + 1: X + 1;                  
                    for(int j = 1; j < k; j++){
                        Rdiagonal[8 * Y + X][i] |= pos; 

                        if((pos2 & i) == 0){
                            pos >>>= 9;
                            pos2 >>>= 1;
                        }
                        else break;
                    }

                }

                Abs = Math.abs(7 - X - Y);
                lim = 8 - Abs;
                Ldiagonal[8 * Y + X] = new long[(int)Math.pow(2, lim)];
                for(int i = 1; i < Math.pow(2, lim); i++){
                    pos = 1L << ((Y + 1) * 8 + (X - 1));
                    pos2 = ((7 - X) > Y)? 1L << (Y + 1): 1L << (8 - X);
                    k = ((7 - X) > Y)?  lim - Y: lim - (7 - X);
                    if(X == 1 && Y == 1 && i == 2)  
                    X = 1;
                    for(int j = 1; j < k; j++){
                        Ldiagonal[8 * Y + X][i] |= pos; 

                        if((pos2 & i) == 0){
                            pos <<= 7;
                            pos2 <<= 1;
                        }
                        else break;
                    } 
                    pos = 1L << ((Y - 1) * 8 + (X + 1));
                    pos2 = ((7 - X) > Y)? 1L << (Y - 1): 1L << (6 - X);
                    k = ((7 - X) > Y)?  Y + 1: 8 - X;                  
                    for(int j = 1; j < k; j++){
                        Ldiagonal[8 * Y + X][i] |= pos; 

                        if((pos2 & i) == 0){
                            pos >>>= 7;
                            pos2 >>>= 1;
                        }
                        else break;
                    }

                }
            }
        }
    }
    static int count = 0;
    public static void display(long val){
        long position;
        for(int Y = 7; Y > -1; Y--){
            position = 1L << 8 * Y;
            for(int X = 0; X < 8; X++){
                if((position & val) != 0) System.out.print("*, ");
                //else if (X == count)System.out.print('N,');
                else System.out.print("_, ");
                position <<= 1;
            }
            System.out.println();
        }
        System.out.println();
       // System.out.println(count);
        count++;
     }

    public static long getMoves(int pos, int Rd, int Ld){
        return Ldiagonal[pos][Ld] | Rdiagonal[pos][Rd];
    }
    public static void main(String args[]){
        INIT();
        // for(int i = 0; i < 128; i++){
        //     System.out.println("______________");
        // display(Ldiagonal[2][i]);
        // display(i);
        // System.out.println("+++++++++++++++++");
        // }
        display(Ldiagonal[59][8]);
        display(8);

    }
}
