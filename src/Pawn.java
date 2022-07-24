public class Pawn {
    public static long moves[][] = new long[8][];
    public static void INIT(){
        long A = F.files[0];
        long H = F.files[7];
        long pos = 1L;
        int n;
        int size;
        for(int i = 0; i < 8; i++){
            pos = 1L << i;
            boolean e = (pos &  A) != 0 || (pos & H) != 0;
            n = (e)? 2: 3;
            size = (int)Math.pow(2, n);
            moves[i] = new long[size];
            n = ((pos & A) != 0)? 1: 2;
            for(int j = 0; j < size; j++){

                if((n & j) != 0) moves[i][j] |= pos;
                if((pos & ~A) != 0) 

                if(((n >>> 1) & j) != 0) moves[i][j] |= (pos >>> 1);

                if((pos & ~H) != 0){ 
                    if(((n << 1) & j) != 0) moves[i][j] |= (pos << 1);
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
    public static long getMoves(int pos, bitboard b){
        int rank = pos / 8;
        int o = 0;
        if(b.isWhite()){
            if(rank == 7) return 0;
            if(rank == 1)o = 1;
            rank++;
        }
        else{
            if(rank == 0) return 0;
            if(rank == 6) o = -1;
            rank--;
        }
        long possible = moves[pos % 8][b.pawnState(pos)] << (rank * 8);

        if(o != 0){
            long d = 1L << ((rank + o) * 8 + (pos % 8));
            if(((1L << (rank * 8 + (pos % 8))) & possible) != 0)
            if(( d & ~b.getBoard()) != 0)
                possible |= d;
        }

        return possible;
    }
    public static long pawnAttack(long board, int turn, long ignorepos){
        board &= ~ignorepos;
        long Rattack = ~F.files[0] & board;
        Rattack = (turn == 0)? Rattack << 7: Rattack >>> 9;
        long Lattack = ~F.files[7] & board;
        Lattack = (turn == 0)? Lattack << 9: Lattack >>> 7;
        return Rattack | Lattack;
    }
    public static void main(String args[]){
        Pawn.INIT();
        for(int i = 0; i < 8; i++){
            // System.out.println("for position: "+ i);
            // for(int j = 0; j < moves[i].length; j++){
            //     System.out.println("---------------------");
            //     display(moves[i][j]);
            //     display(j);
            //     System.out.println("---------------------");
            // }
            display(0x6000000000000000L);
        } 
    }
}
