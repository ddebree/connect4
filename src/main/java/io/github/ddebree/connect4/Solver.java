package io.github.ddebree.connect4;

public class Solver {

    private final int[] columnOrder = new int[Position.WIDTH];

    public Solver() {
        for (int i = 0; i < Position.WIDTH; i++) {
            // initialize the column exploration order, starting with center columns
            columnOrder[i] = Position.WIDTH / 2 + (1 - 2 * (i % 2)) * (i + 1) / 2;
        }
    }

    // counter of explored nodes.
    private long nodeCount = 0;

    public int solve(final Position postion) {
        nodeCount = 0;
        return negamax(postion,
                -Position.WIDTH * Position.HEIGHT / 2,
                Position.WIDTH * Position.HEIGHT / 2);
    }

    /**
     * Reccursively score connect 4 position using negamax variant of alpha-beta algorithm.
     * @param: alpha < beta, a score window within which we are evaluating the position.
     *
     * @return the exact score, an upper or lower bound score depending of the case:
     * - if true score of position <= alpha then true score <= return value <= alpha
     * - if true score of position >= beta then beta <= return value <= true score
     * - if alpha <= true score <= beta then return value = true score
     */
    private int negamax(final Position position, int alpha, int beta) {
        assert(alpha < beta);
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

        int max = (Position.WIDTH * Position.HEIGHT-1 - position.nbMoves()) / 2;	// upper bound of our score as we cannot win immediately
        if (beta > max) {
            beta = max;                     // there is no need to keep beta above our max possible score.
            if (alpha >= beta) return beta;  // prune the exploration if the [alpha;beta] window is empty.
        }

        for (int x = 0; x < Position.WIDTH; x++) {
            // compute the score of all possible next move and keep the best one
            final int col = columnOrder[x];
            if (position.canPlay(col)) {
                final Position newPosition = position.nextPostion(col);
                int score = -negamax(newPosition, -beta, -alpha); // explore opponent's score within [-beta;-alpha] windows:
                // no need to have good precision for score better than beta (opponent's score worse than -beta)
                // no need to check for score worse than alpha (opponent's score worse better than -alpha)

                if (score >= beta) return score;  // prune the exploration if we find a possible move better than what we were looking for.
                if (score > alpha) alpha = score; // reduce the [alpha;beta] window for next exploration, as we only
                // need to search for a position that is better than the best so far.
            }
        }

        return alpha;
    }

    public long getNodeCount() {
        return nodeCount;
    }

}
