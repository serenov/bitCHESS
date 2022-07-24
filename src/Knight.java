public class Knight {
    static long pattern[] = new long[]{
        132096L, 
        329728L, 
        659712L, 
        1319424L, 
        2638848L, 
        5277696L, 
        10489856L, 
        4202496L, 
        33816580L, 
        84410376L, 
        168886289L, 
        337772578L, 
        675545156L, 
        1351090312L, 
        2685403152L, 
        1075839008L, 
        8657044482L, 
        21609056261L, 
        43234889994L, 
        86469779988L, 
        172939559976L, 
        345879119952L, 
        687463207072L, 
        275414786112L, 
        2216203387392L, 
        5531918402816L, 
        11068131838464L, 
        22136263676928L, 
        44272527353856L, 
        88545054707712L, 
        175990581010432L, 
        70506185244672L, 
        567348067172352L, 
        1416171111120896L, 
        2833441750646784L, 
        5666883501293568L, 
        11333767002587136L, 
        22667534005174272L, 
        45053588738670592L, 
        18049583422636032L, 
        145241105196122112L, 
        362539804446949376L, 
        725361088165576704L, 
        1450722176331153408L, 
        2901444352662306816L, 
        5802888705324613632L, 
        -6913025356609880064L, 
        4620693356194824192L, 
        288234782788157440L, 
        576469569871282176L, 
        1224997833292120064L, 
        2449995666584240128L, 
        4899991333168480256L, 
        -8646761407372591104L, 
        1152939783987658752L, 
        2305878468463689728L, 
        1128098930098176L, 
        2257297371824128L, 
        4796069720358912L, 
        9592139440717824L, 
        19184278881435648L, 
        38368557762871296L, 
        4679521487814656L, 
        9077567998918656L, 
    };
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
    //         if(i == 63)
    //         i = 63;
    //         if((position & ~fileA & ~rank78) != 0) 
    //             pattern[i] |= position << 15;
    //         if((position & ~fileH & ~rank78) != 0) 
    //             pattern[i] |= position << 17;
    //         if((position & ~fileA & ~rank12) != 0) 
    //             pattern[i] |= position >>> 17;
    //         if((position & ~fileH & ~rank12) != 0) 
    //             pattern[i] |= position >> 15;

    //         if((position & ~fileAB & ~rank8) != 0) 
    //             pattern[i] |= position << 6;
    //         if((position & ~fileGH & ~rank8) != 0) 
    //             pattern[i] |= position << 10;                    
    //         if((position & ~fileAB & ~rank1) != 0) 
    //             pattern[i] |= position >>> 10;
    //         if((position & ~fileGH & ~rank1) != 0) 
    //             pattern[i] |= position >>> 6;  
    //         position <<= 1;  
    //     }
    //     return pattern;
    // }
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
        System.out.println(count);
        count++;
     }
    public static void main(String args[]){

            //System.out.println("\t\t" + i + "L, ");
             display(0xa003c180a63220L);
        
        //display(F.files[6]);
    }
}
