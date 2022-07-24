public class BoardState {
    protected long pieces[] = new long[13];
    protected long W;
    protected long B;
    protected long enpassant;
    protected int Castling;
    protected int checks;
    protected long LineofAttack;
    protected long positionOfAttack;
    protected int IdOfAttackP;
    protected int moves = 0;
}
