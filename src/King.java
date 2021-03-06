public class King {
    static long pattern[] = new long[]{
        770L, 
        1797L, 
        3594L, 
        7188L, 
        14376L, 
        28752L, 
        57504L, 
        49216L, 
        197123L, 
        460039L, 
        920078L, 
        1840156L, 
        3680312L, 
        7360624L, 
        14721248L, 
        12599488L, 
        50463488L, 
        117769984L, 
        235539968L, 
        471079936L, 
        942159872L, 
        1884319744L, 
        3768639488L, 
        3225468928L, 
        12918652928L, 
        30149115904L, 
        60298231808L, 
        120596463616L, 
        241192927232L, 
        482385854464L, 
        964771708928L, 
        825720045568L, 
        3307175149568L, 
        7718173671424L, 
        15436347342848L, 
        30872694685696L, 
        61745389371392L, 
        123490778742784L, 
        246981557485568L, 
        211384331665408L, 
        846636838289408L, 
        1975852459884544L, 
        3951704919769088L, 
        7903409839538176L, 
        15806819679076352L, 
        31613639358152704L, 
        63227278716305408L, 
        54114388906344448L, 
        216739030602088448L, 
        505818229730443264L, 
        1011636459460886528L, 
        2023272918921773056L, 
        4046545837843546112L, 
        8093091675687092224L, 
        -2260560722335367168L, 
        -4593460513685372928L, 
        144959613005987840L, 
        362258295026614272L, 
        724516590053228544L, 
        1449033180106457088L, 
        2898066360212914176L, 
        5796132720425828352L, 
        -6854478632857894912L, 
        4665729213955833856L, 
    };
    public static long getMoves(int pos, int castling, long safeSquares, long empty){
        long moves = pattern[pos];
        
        if(castling != 0){
            long emp = 1l << (pos - 1) | 1l << (pos - 2) | 1l << (pos - 3);
            if((castling & 2) != 0)
            if(bitboard.countBits(emp & empty) == 3)
            if((safeSquares & (1l << (pos - 1))) != 0)    
                moves |= 1L << pos - 2;
            
            emp = 1l << (pos + 1) | 1l << (pos + 2);

            if((castling & 4) != 0)
            if(bitboard.countBits(emp & empty) == 2)
            if((safeSquares & (1l << (pos + 1))) != 0)
                moves |= 1L << pos + 2;
        }

        return moves & safeSquares;
    }
    public static long pawnAttack(int turn, int pos){
        turn = (turn == 0)? -1: 1;
        long pawnAt = King.pattern[pos] & ~R.ranks[pos / 8] & ~F.files[pos % 8];
        if(((pos / 8 ) + turn) > 7 || ((pos / 8) + turn) < 0);
        else pawnAt &= ~R.ranks[(pos / 8) + turn];
        return pawnAt;
    }
    // public static long[] AttackPattern(){
    //     long position = 1L;
    //     long fileA = F.files[0];
    //     long fileB = F.files[1];
    //     long fileG = F.files[6];
    //     long fileH = F.files[7];
    //     long fileAB = fileA | fileB;
    //     long fileGH = fileG | fileH;
    //     long rank1 = R.ranks[0];
    //     long rank2 = R.ranks[1];
    //     long rank7 = R.ranks[6];
    //     long rank8 = R.ranks[7];
    //     long rank12 = rank1 | rank2;
    //     long rank78 = rank7 | rank8;
    //     for(int i = 0; i < 64; i++){
    //         if((position & ~fileA & ~rank8) != 0) 
    //             pattern[i] |= position << 7;
    //         if((position & ~rank8) != 0) 
    //             pattern[i] |= position << 8;
    //         if((position & ~fileH & ~rank8) != 0) 
    //             pattern[i] |= position << 9;
    //         if((position & ~fileA) != 0) 
    //             pattern[i] |= position >>> 1 ;

    //         if((position & ~fileH) != 0) 
    //             pattern[i] |= position << 1;
    //         if((position & ~fileA & ~rank1) != 0) 
    //             pattern[i] |= position >>> 9;                    
    //         if((position & ~rank1) != 0) 
    //             pattern[i] |= position >>> 8;
    //         if((position & ~fileH & ~rank1) != 0) 
    //             pattern[i] |= position >>> 7;  
    //         position <<= 1;  
    //     }
    //     return pattern;
    // }
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
    //     System.out.println(count);
    //     count++;
    // }
    // public static void main(String args[]){
    //     //AttackPattern();
    //     for(long i: Knight.pattern){
    //            display(i);
    //     }
    //     //display(F.files[6]);
    // }
}
