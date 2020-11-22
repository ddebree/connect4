package io.github.ddebree.connect4;

public class Solver {

    // counter of explored nodes.
    private long nodeCount = 0;

    public int solve(final Position postion) {
        nodeCount = 0;
        return negamax(postion);
    }

    /*
     * Recursively solve a connect 4 position using negamax variant of min-max algorithm.
     * @return the score of a position:
     *  - 0 for a draw game
     *  - positive score if you can win whatever your opponent is playing. Your score is
     *    the number of moves before the end you can win (the faster you win, the higher your score)
     *  - negative score if your opponent can force you to lose. Your score is the opposite of
     *    the number of moves before the end you will lose (the faster you lose, the lower your score).
     */
    private int negamax(final Position position) {
        nodeCount++; // increment counter of explored nodes

        if (position.nbMoves() == Position.WIDTH * Position.HEIGHT) {
            // check for draw game
            return 0;
        }

        for (int x = 0; x < Position.WIDTH; x++) {
            // check if current player can win next move
            if (position.canPlay(x) && position.isWinningMove(x)) {
                return (Position.WIDTH * Position.HEIGHT + 1 - position.nbMoves()) / 2;
            }
        }

        int bestScore = -Position.WIDTH * Position.HEIGHT; // init the best possible score with a lower bound of score.

        for (int x = 0; x < Position.WIDTH; x++) {
            // compute the score of all possible next move and keep the best one
            if (position.canPlay(x)) {
                final Position newPosition = position.nextPostion(x);
                int score = -negamax(newPosition); // If current player plays col x, his score will be the opposite of opponent's score after playing col x
                if (score > bestScore) {
                    bestScore = score; // keep track of best possible score so far.
                }
            }
        }

        return bestScore;
    }

    public long getNodeCount() {
        return nodeCount;
    }

}
