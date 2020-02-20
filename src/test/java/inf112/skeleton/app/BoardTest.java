package inf112.skeleton.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class BoardTest {

    @InjectMocks
    private Board board;

    @Mock
    private TmxMapLoader tmxMapLoader;

    private String mapPath;

    // Create mocks when @Mock is used
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        // Init mocks
        MockitoAnnotations.initMocks(this);
        this.mapPath  = "assets/kart.tmx";
        this.board = new Board(mapPath);
    }

    @Test
    public void test() {
        assert(true);
    }
}
