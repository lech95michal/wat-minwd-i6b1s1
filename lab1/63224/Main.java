import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;

public class Main {

    public static void main(String[] args) {

        LinearProgram lp = new LinearProgram(new double[]{50.0,10.0});
        lp.addConstraint(new LinearSmallerThanEqualsConstraint(new double[]{12.0,4.0}, 480.0, "c1"));
        lp.addConstraint(new LinearSmallerThanEqualsConstraint(new double[]{8.0,8.0}, 640.0, "c2"));
        lp.addConstraint(new LinearBiggerThanEqualsConstraint(new double[]{1.0,0.0}, 0.0, "c3"));
        lp.addConstraint(new LinearBiggerThanEqualsConstraint(new double[]{0.0,1.0}, 0.0, "c4"));
        lp.addConstraint(new LinearSmallerThanEqualsConstraint(new double[]{1.0,-1.0}, 0.0, "c5"));
        lp.setMinProblem(false);
        LinearProgramSolver solver  = SolverFactory.newDefault();
        double[] sol = solver.solve(lp);

    }
} 