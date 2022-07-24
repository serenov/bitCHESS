public class Perft {
    static String fen = "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1";
    static int limit = 4;
    static int count = 0;
    class mote{
        String move;
        int count;
        public mote(long selection, long destination){
            count = 0;
            move = stringify(bitboard.getIndex(selection), bitboard.getIndex(destination));
        }
        public String stringify(int sel, int des){
            char x = (char)('a' + (sel % 8));
            char y = (char)('1' + (sel / 8));
            char dx = (char)('a' + (des % 8));
            char dy = (char)('1' + (des / 8));

            return new String(new char[]{x, y, dx, dy});
        }
    }

    bitboard b;
    
   mote[] m;



    public Perft(bitboard b){
        this.b = b;
        b.bitboardsInit();
        b.INIT();
    }

    public void Traverse(int turn, int depth, int MI){
        if(depth > limit) return;
        if(!b.Checkmate()){
            long piece;
            long pmoves;
            long selection;
            long destination;
            int pos;
            int count;
            int promcnt = 0;
            for(int i = 0; i < 6; i++){
                piece = b.getp(6 * turn + i);
                for(int j = bitboard.countBits(piece); j > 0; j--){
                    pos = bitboard.getIndex(piece);
                    selection = 1l << pos;
                    pmoves = b.getMoves(6 * turn + i, pos % 8, pos / 8);
                    count = bitboard.countBits(pmoves);
                    for(int k = 0; k < count; k++){
                        destination = 1l << bitboard.getIndex(pmoves);

                        if(promcnt == 0)
                        if(b.isPromotion(turn * 6 + i, destination)){
                            promcnt = 4;
                            b.promoteToID = turn * 6 + 4;
                            k -= 3;
                        }
                        b.history(6 * turn + i, selection, destination);
                        b.move(6 * turn + i, selection, destination);
                        b.moves++;
                        

                        if(depth == 0) m[MI] = new mote(selection, destination);
                        if(depth == limit)m[MI].count++;
                        
                        Traverse((turn + 1) % 2, depth + 1, MI);
                        if(depth == 0){ 
                            MI++;
                            System.out.printf("\rCompletion: %d%c", (int)(100 * ((MI * 1.0)/ Perft.count)), '%');
                        }
                        b.undo(--b.moves);
                        if(promcnt != 0){
                            promcnt--;
                            b.promoteToID = turn * 6 + promcnt;
                        }
                        if(promcnt == 0)pmoves &= ~destination;
                    }
                    piece &= ~(1l << pos);
                }
            }
        }
    }
    final public static void countmoves(bitboard b){
        if(!b.Checkmate()){
            long piece;
            int pos;
            long pmoves;
            for(int i = 0; i < 6; i++){
                piece = b.getp(6 * b.moves + i);
                for(int j = bitboard.countBits(piece); j > 0; j--){
                    pos = bitboard.getIndex(piece);
                    pmoves = b.getMoves(6 * b.moves + i, pos % 8, pos / 8);
                    count += bitboard.countBits(pmoves);
                    if(b.isPromotion(i, pmoves)) count += 3 * bitboard.countBits(pmoves);
                    piece &= ~(1l << pos);
                }
            }
        }
    }
    public static void main(String args[]){
        Perft t;
        try{
            limit = Integer.parseInt(args[6]);
            //String k[] = new String[]{args[1], args[2], args[3], args[4]};
            t = new Perft(new bitboard(args));

        }
        catch(Exception e){
            System.err.println("taking default fen string and limit");
            t = new Perft(new bitboard());
            limit = 3;
        }
        //Perft t = new Perft(new bitboard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -".split(" ")));
        //Perft t = new Perft(new bitboard());
        countmoves(t.b);
        t.m = new mote[count];
        long telapsed = System.currentTimeMillis();
        t.Traverse(t.b.moves, 0, 0);
        telapsed = System.currentTimeMillis() - telapsed;
        long sum = 0;
        System.out.print("\n");
        for(int i = count - 1; i > -1; i--){
            System.out.println(t.m[i].move + ": " + (t.m[i].count));
            sum += t.m[i].count;
        }
        System.out.println("Nodes Searched: " + sum);
        System.out.println("Time elapsed: " + (telapsed / 1000.0) + "s");
        
    }
}
