package grep;

/**
 * Created by Iskuskov on 26.03.2016.
 */
public class ParallelGrepTest extends GrepTestBase {

    private static final IGrep parallelGrep = new ParallelGrep();

    @Override
    protected IGrep grepObject() {
        return parallelGrep;
    }
}
