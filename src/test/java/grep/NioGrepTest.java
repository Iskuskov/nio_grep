package grep;

/**
 * Created by Iskuskov on 26.03.2016.
 */
public class NioGrepTest extends GrepTestBase {

    private static final IGrep nioGrep = new NioGrep();

    @Override
    protected IGrep grepObject() {
        return nioGrep;
    }
}
