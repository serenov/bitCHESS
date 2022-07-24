import java.util.Scanner;

public class bitboard{ 
    
    private long pieces[] = new long[13];
    private long W;
    private long B;
    private long enpassant;
    private int Castling;
    protected int checks;
    private long LineofAttack;
    private long positionOfAttack;
    private int IdOfAttackP;
    protected int moves = 0;
    BoardState[] StateIndex = new BoardState[1048];
    private char FEN[];
    protected long pmoves = 0;
    protected int promoteToID = 4;

    public bitboard(String arr[]){
        this.FEN = arr[0].toCharArray();
        moves = (arr[1].charAt(0) == 'w')? 0: 1;
        InitCastling(arr[2].toCharArray());
        InitEnpassant(arr[3].toCharArray());
    }
    public bitboard(){
        String arr[] = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1".split(" ");
        this.FEN = arr[0].toCharArray();
        moves = (arr[1].charAt(0) == 'w')? 0: 1;
        InitCastling(arr[2].toCharArray());
        InitEnpassant(arr[3].toCharArray());
    }

    public boolean isWhite(){
        if(moves % 2 == 0) return true;
        return false;
    }

    public int FENMap(char c){
        switch(c){
            case 'p':
                return 11;
            case 'P':
                return 5;
            case 'R':
                return 2;
            case 'r':
                return 8;
            case 'B':
                return 3;
            case 'b':
                return 9;
            case 'N':
                return 4;
            case 'n':
                return 10;
            case 'K':
                return 0;
            case 'k':
                return 6;
            case 'Q':
                return 1;
            case 'q':
                return 7;
        }
        return 0;
    }

    public void InitCastling(char arr[]){
        Castling = 0;
        if(arr[0] == '-');
        else if( arr.length == 4) Castling = 63;
        else{
            for(char i: arr){
                if(i == 'Q') Castling |= 3;
                else if(i == 'K') Castling |= 6;
                else if(i == 'q') Castling |= 24;
                else if(i == 'k') Castling |= 48;
            }
        }
    }
    public void InitEnpassant(char[] arr){
        enpassant = 0;
        if(arr[0] == '-');
        else{
            enpassant = 1l << ((arr[1] - '1') * 8 + (arr[0] - 'a'));
        }
    }
    public void bitboardsInit(){

        long position;

        int y = 6;

        position = 1L << 56;

        for(char i: FEN){
            if(i > 'A'){ 
                    pieces[FENMap(i)] |= position;
                    position <<= 1;
            }
            else if( i > '0') position <<= i - '0';
            else position = 1L << (y-- * 8);
        }
        updateWB();
        updateEmpty();


    }
    
    public void updateEmpty(){
        pieces[12] = ~(W | B);
    }

    public void updateWB(){
        W = 0;
        B = 0;
        for(int i = 0; i < 6; i++){
            W |= pieces[i];
            B |= pieces[i + 6];
        }
    }

    public void bitDisplay(){

        char Sym[] = 
            new char[]{
                'K', 'Q', 'R', 'B', 'N', 'P',
                'k', 'q', 'r', 'b', 'n', 'p',
                ' '
            };
        long h = 1L;

        for(int Y = 7; Y > -1; Y--){

            h = 1L << (Y * 8);
            System.out.print((Y + 1) +" ");

            System.out.print("{");
            for(int X = 0; X < 8; X++){

                for(int j = 0; j < 13; j++){

                    if((pieces[j] & h) != 0){

                        System.out.print(Sym[j] + " ");

                    }

                }
                if((pmoves & h) != 0){
                    System.out.print("*,");
                }else System.out.print(" ,");

                h <<= 1;

            }

            System.out.print("}");
            System.out.println();

        }
        System.out.println("   a   b   c   d   e   f   g   h");
    }

    public int getPiece(long position){
        for(int i = 0; i < 6; i++){
            if((position & pieces[i]) != 0)return i;
            else if((position & pieces[i + 6]) != 0)return i + 6;
        }
        return 12;
    }
    public long getp(int i){
        return pieces[i];
    }

    public long getBoard(){
        return W | B;
    }
    public void history(int ID, long selection, long destination){
        logTheState();
        enpassant = 0;
        if(ID == 5 || ID == 11){
            if(isWhite()){
                if((selection & R.ranks[1]) != 0)
                if((destination & R.ranks[3]) != 0) enpassant |= selection << 8;
            }
            else {
                if((selection & R.ranks[6]) != 0)
                if((destination & R.ranks[4]) != 0) enpassant |= destination << 8;
            }
        }
        else if(ID == 0 || ID == 6){
            Castling = (isWhite())? Castling & 61: Castling & 47;
        }
        else if(ID == 2 || ID == 8){
            if(isWhite()){
                if((Castling & 2) != 0){
                    long i = 1;
                    long j = 1 << 7;
                    if((i & selection) != 0) Castling &= 62;
                    else if((j & selection) != 0) Castling &= 59;
                }
            }
            else{
                if((Castling & 16) != 0){
                    long i = 1l << 56;
                    long j = 1l << 63;
                    if((i & selection) != 0) Castling &= 55;
                    else if((j & selection) != 0) Castling &= 31;
                }
            }
        }

        if(destination == 1) Castling &= ~1;

        else if(destination == (1l << 7)) Castling &= ~4;

        else if(destination == (1l << 56)) Castling &= ~8;

        else if(destination == (1l << 63)) Castling &= ~32;

    }
    public int castling(){
        if(Castling == 0) return 0;

        int key = (isWhite())? 2: 16;
        long left = (isWhite())? 14: 14L << 7 * 8;
        long right = (isWhite())? 0x60: 0x60L << 7 * 8;
        int state = (isWhite())? 1: 0;

        if((Castling & key) == 0 || checks != 0) return 0;
        if((Castling & (key << 1)) != 0){
            if((right & pieces[12]) != 0){
                state += 4;
            }
        }
        if((Castling & (key >>> 1)) != 0){
            if((left & pieces[12]) != 0){
                state += 2;
            }
        }
        return state;
    }
    public boolean inBounds(char[] arr){
        if(arr[0] < 'a' || arr[2] < 'a' || arr[0] > 'h' || arr[2] > 'h' 
        || arr[1] < '1' || arr[3] < '1' || arr[1] > '8' || arr[3] > '8')
        return false;
        return true;
    }    
    public long getMoves(int i, int X, int Y){
        int position = Y * 8 + X;
        long Opiece = (moves % 2 == 0)? W: B;
        long resultant = (i != 0 && i != 6)? legalizer(i, position): 0;
        switch(i){
            case 0:
            case 6:
                resultant = King.getMoves(position, castling(), ~AllAttackedSqs(0), pieces[12]);
                return resultant & ~Opiece;
            case 1:
            case 7:
                resultant &= Rook.getMoves(8 * Y + X, (W | B) >>> (Y * 8), clw90(X)) | 
                Bishop.getMoves(Y * 8 + X, clw45(X, Y), clw135(X, Y));
                return  resultant & ~Opiece;
            case 2:
            case 8:
                resultant &= Rook.getMoves(8 * Y + X, (W | B) >>> (Y * 8), clw90(X));
                return  resultant & ~Opiece;
            case 3:
            case 9:
                resultant &= Bishop.getMoves(Y * 8 + X, clw45(X, Y), clw135(X, Y));
                return resultant & ~Opiece;
            case 4:
            case 10:
                return resultant & Knight.pattern[position] & ~Opiece;
            case 5:
            case 11:
                long pawnm = Pawn.getMoves(Y * 8 + X, this);
                if((pawnm & enpassant) != 0)
                if(IdOfAttackP == 5 || IdOfAttackP == 11){
                    resultant |= enpassant;
                }
                return resultant & pawnm;


        }
        return 0;

    }    
    public long pgetMoves(int i, int X, int Y, int turn){
        switch(i){
            case 1:
            case 7:
                return Rook.getMoves(8 * Y + X, (W | B) >>> (Y * 8), clw90(X)) | 
                Bishop.getMoves(Y * 8 + X, clw45(X, Y), clw135(X, Y));
            case 2:
            case 8:
                return Rook.getMoves(8 * Y + X, (W | B) >>> (Y * 8), clw90(X));
            case 3:
            case 9:
                return Bishop.getMoves(Y * 8 + X, clw45(X, Y), clw135(X, Y));
            case 4:
            case 10:
                return Knight.pattern[Y * 8 + X];
            default:
             return 0;
        }
    }    
    public long check(int turn, boolean set){
        int index = turn * 6;

        if((AllAttackedSqs(0) & pieces[index]) == 0){
            if(set) {LineofAttack = -1; checks = 0;}
            return -1;
        }
        
        int checks = 2;
        int pos = getIndex(pieces[index]);
        int X = pos % 8;
        int Y = pos / 8;

        int opp = (turn == 0)? 1: 0;

        long pkey;
        int oID = opp * 6;

        long LineofAttack = Rook.getMoves(pos, (W | B) >>> (Y * 8), clw90(X));
        long DiagonalofAttack = Bishop.getMoves(Y * 8 + X, clw45(X, Y), clw135(X, Y));
        long KnightAttack = Knight.pattern[pos];

        long AttackPattern = 0;

        if((pkey = (pieces[++oID] & LineofAttack)) != 0){
            int ind = getIndex(pkey);
            AttackPattern = (Rook.getMoves(getIndex(pkey), (W | B) >>> ((ind / 8)* 8), clw90((ind % 8))) & LineofAttack) | pkey;
        }
        else if((pkey = (pieces[oID] & DiagonalofAttack)) != 0){
            int ind = getIndex(pkey);
            AttackPattern = (Bishop.getMoves(ind, clw45(ind % 8, ind / 8), clw135(ind % 8, ind / 8)) & DiagonalofAttack) | pkey;
        }        
        else if((pkey = (pieces[++oID] & LineofAttack)) != 0){
            int ind = getIndex(pkey);
            AttackPattern = (Rook.getMoves(getIndex(pkey), (W | B) >>> ((ind / 8)* 8), clw90((ind % 8))) & LineofAttack) | pkey;
        }

        else checks--;

        if(set && checks == 2){
            positionOfAttack = pkey;
            IdOfAttackP = oID;
            this.checks++;
        }

        oID = 6 * opp + 2;
        if((pkey = (pieces[++oID] & DiagonalofAttack)) != 0){
            int ind = getIndex(pkey);
            AttackPattern = (Bishop.getMoves(ind, clw45(ind % 8, ind / 8), clw135(ind % 8, ind / 8)) & DiagonalofAttack) | pkey;
        }
        else if((pkey = (pieces[++oID] & KnightAttack)) != 0)
            AttackPattern |= pkey;

        else if((pkey = (pieces[++oID] & King.pawnAttack(turn, pos))) != 0)
                AttackPattern |= pkey;

        else  checks--;


        if(set){
            if(this.checks == 0){
                positionOfAttack = pkey;
                IdOfAttackP = oID;
            }
            this.LineofAttack = AttackPattern;
            this.checks = checks;
        }
        return AttackPattern;

    }
    public boolean Checkmate(){
        int turn = moves % 2;
        check(turn, true);
        if(checks == 0) return false;
        int pos;
        long board;
       for(int i = 0; i < 6; i++){
            board = pieces[turn * 6 + i];
            int count = countBits(board);
            for(int j = 0; j < count; j++){
                pos = getIndex(board);
                if(getMoves(turn * 6 + i, pos % 8, pos / 8) != 0) return false;
                board &= ~ (1l << pos);
            }
        }
        return true;
    }
    public long legalizer(int ID, int pos){
        int turn = (isWhite())? 0: 1;
        if(checks == 0){
            pieces[ID] &= ~(1L << pos);
            updateWB();
            long Attack = check(turn, false);
            pieces[ID] |= (1L << pos);
            updateWB();
            return Attack;
        }
        else if (checks == 1){
            pieces[ID] &= ~(1l << pos);
            updateWB();
            updateEmpty();
            long temp = AllAttackedSqs(positionOfAttack);
            pieces[ID] |= (1l << pos);
            updateWB();
            updateEmpty();
            if((temp & pieces[turn * 6]) != 0) return 0;
            return LineofAttack;
        }
        return 0;
    }
    public long AllAttackedSqs(long ignorepos){
        int opp = (isWhite())? 1: 0;
        long c = (opp == 1)? B: W;
        long AttackedSqs = 0;
        long temp = pieces[6 * ((opp + 1) % 2)];
        pieces[6 * ((opp + 1) % 2)] = 0;
        updateWB();

        for(int i = 1; i < 5; i++){
            long p = pieces[6 * opp + i];
            for(int j = countBits(p); j > 0; j--){
                int k = getIndex(p);
                if(k != getIndex(ignorepos))
                AttackedSqs |= pgetMoves(6 * opp + i, k % 8, k / 8, opp);
                p &= ~(1L << k);
            }
        }
        AttackedSqs |= King.pattern[getIndex(pieces[6 * opp])] & ~c;
        AttackedSqs |= Pawn.pawnAttack(pieces[6 * opp + 5], opp, ignorepos);
        pieces[6 * ((opp + 1) % 2)] = temp;
        updateWB();
        
        return AttackedSqs;
    }
    public void move(int ID, long selection, long destination){
        int j = getPiece(destination);
        pieces[ID] &= ~selection;
        pieces[ID] |= destination;
        if(ID == 5 || ID == 11){
            long off = selection << 9 | selection << 7 |
            selection >>> 9 | selection >>> 7;
            if((off & destination) != 0 && j == 12){
                destination = (isWhite())? destination >>> 8: destination << 8;
                j = getPiece(destination);
            }
            else if(isPromotion(ID, destination)){
                pieces[promoteToID] |= destination;
                pieces[ID] &= ~destination;
            }
        }
        else if(ID == 0 || ID == 6){
            long cfile = F.files[2];
            long gfile = F.files[6];
            if((selection & F.files[4]) != 0){
                if((destination & cfile) != 0)
                castleMove(destination, false);
                else if((destination & gfile) != 0)
                castleMove(destination, true);
            }
        }
        pieces[j] &= ~destination;
        updateWB();
        updateEmpty();
    }  
    public void castleMove(long destination, boolean isRight){
        int ID = (moves % 2) * 6 + 2;
        long s = (isRight)? destination << 1: destination  >>> 2;
        long d = (isRight)? destination >>> 1: destination << 1;
        move(ID, s, d);

    }
    public boolean isPromotion(int ID, long destination){
        if(((ID - 11) * (ID - 5) == 0) && (destination & (R.ranks[0]| R.ranks[7])) != 0) return true;
        return false;
    }


    public void input(char[] mov, Scanner s){
        if(mov[0] == 'u' && moves != 0) {undo(--moves); return;}
        if(mov.length == 4 && inBounds(mov));
        else{
            System.out.println("Invalid input");
            return;
        }
        final int selX = mov[0] - 'a';
        final int selY = mov[1] - '1';
        final int desX = mov[2] - 'a';
        final int desY = mov[3] - '1';
        final long selection = 1L << (8 * selY + selX);
        final long destination = 1L << (8 * desY + desX);
        final long piece = (moves % 2 == 0)? W: B;

        if((piece & selection) != 0 && (~piece & destination) != 0){
            final int i = getPiece(selection);
            pmoves = getMoves(i, selX, selY);
            bitDisplay();
            System.out.println();
            if((pmoves & destination) != 0){
                if(isPromotion(i, destination)){
                    System.out.println("Q - 1\nR - 2\nB - 3\nN - 4\n");
                    int t = s.nextInt();
                    promoteToID = (moves % 2) * 6 + t;
                    if(t*(t - 5) > 0) promoteToID = (moves % 2) * 6 + 1;
                }
                history(i, selection, destination);
                move(i, selection, destination);
                moves++;
            }
            else{
                System.out.println("\nSelect a marked Square!\n");
            }
            pmoves = 0;
        }
        else{
            System.out.println("Invalid");
        }
    }


    public int clw90(int X){
        int rank = 0;
        long position = 1L << X;
        long allpieces = W | B;
        for(int i = 0; i < 8; i++){
            if((position & allpieces) != 0) 
            rank |= 1 << i;
            position <<= 8;
        }
        return rank;
    }
    public int clw45(int X, int Y){
        long position = 1L << (8 * Y + X);
        int diagonalstate = 0;
        position = (X > Y)? position >>> (9 * Y): position >>> (9 * X);
        for(int i = 0; i < (8 - Math.abs(X - Y)); i++){
            if((position & (W | B)) != 0) diagonalstate |= 1 << i;
            position <<= 9;
        }
        return diagonalstate;
    }
    public int clw135(int X, int Y){
        long position = 1L << (8 * Y + X);
        int diagonalstate = 0;
        position = ((7 - X) > Y)? position >>> (7 * Y): position >>> (7 * (7 - X));
        for(int i = 0; i < (8 - Math.abs(7 - X - Y)); i++){
            if((position & (W | B)) != 0) diagonalstate |= 1 << i;
            position <<= 7;
        }
        return diagonalstate;
    }
    public int pawnState(int pos){
        long A = F.files[0];
        long H = F.files[7];
        long posk = 1L << pos;
        long Lpos = (isWhite())? posk << 7: posk >>> 9;
        long Mpos = (isWhite())? posk << 8: posk >>> 8;
        long Rpos  = (isWhite())? posk << 9: posk >>> 7;
        long pieces = (isWhite())? B: W;
        int state = 0;
        if(enpassant != 0){
            long temp1 = this.pieces[(moves % 2) * 6 + 5];
            long temp2 = this.pieces[((moves + 1) % 2) * 6 + 5];
            move((moves % 2) * 6 + 5, 1l << pos, enpassant);
            if((AllAttackedSqs(0) & this.pieces[(moves % 2) * 6]) == 0)
            pieces |= enpassant;
            this.pieces[(moves % 2) * 6 + 5] = temp1;
            this.pieces[((moves + 1) % 2) * 6 + 5] = temp2;
            updateWB();
            updateEmpty();
        }
        if((Mpos & ~(W | B)) != 0 ) state += 2;
        if((~A & posk) != 0) if((Lpos & pieces) != 0) state += 1;
        if((~H & posk) != 0) if((Rpos & pieces) != 0) state += 4;
        if((A & posk) != 0) state >>>= 1;

        return state;
    }
    public static int getIndex(long board){
        if(board == 0) return -1;
        if(board < 0) return 63;
        return (int)(Math.log(board) / (0.6931471));
    }
    public static int countBits(long board){
        int count = 0;
        for(long i = board; i != 0; i &= i - 1) count++;
        return count;
    }
    public void logTheState(){
        BoardState state = new BoardState();
        for(int i = 0; i < 13; i++){
            state.pieces[i] = pieces[i];
        }
        state.W = W;
        state.B = B;
        state.enpassant = enpassant;
        state.Castling = Castling;
        state.checks = checks;
        state.LineofAttack = LineofAttack;
        state.positionOfAttack = positionOfAttack;
        state.IdOfAttackP = IdOfAttackP;
        state.moves = moves;
        StateIndex[moves] = state;
    }
    public void undo(int moves){
        //if(moves == 0) return;
        for(int i = 0; i < 13; i++){
            pieces[i] = StateIndex[moves].pieces[i];
        }
        W = StateIndex[moves].W;
        B = StateIndex[moves].B;
        enpassant = StateIndex[moves].enpassant;
        Castling = StateIndex[moves].Castling;
        checks = StateIndex[moves].checks;
        LineofAttack = StateIndex[moves].LineofAttack;
        positionOfAttack = StateIndex[moves].positionOfAttack;
        IdOfAttackP = StateIndex[moves].IdOfAttackP;
        moves = StateIndex[moves].moves;
    }
    public void INIT(){
        Rook.INIT();
        Bishop.INIT();
        Pawn.INIT();
    }
    public static void main(String[] args){

        //bitboard b = new bitboard("8/5p1p/pk6/q5pN/7b/1pp1r2R/1B3P2/4Kn2".toCharArray());
        //bitboard b = new bitboard("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPKPP/RNBQ1BNR b KQkq e3 0 1".split(" "));
        //bitboard b = new bitboard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -".split(" "));
        //bitboard b = new bitboard();
        //bitboard b = new bitboard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -".split(" "));
        bitboard b = new bitboard("rnbq1k1r/pp1Pbppp/2p5/3Q4/2B5/8/PPP1NnPP/RNB1K2R b KQ - 2 8".split(" "));
        b.INIT();
        b.bitboardsInit();
        final String[] color = new String[]{"White", "Black"};
        //b.bitDisplay();
        try (Scanner s = new Scanner(System.in)) {
        
            while(!b.Checkmate()){
                if(b.checks != 0) System.out.println("Check!");
                b.bitDisplay();
                System.out.println("\n\t"+ color[b.moves % 2] + " To play");
                b.input(s.next().toCharArray(), s);
            }
            b.bitDisplay();
            System.out.println("Checkmate by " + color[(b.moves + 1) % 2]);

        }
    }
}