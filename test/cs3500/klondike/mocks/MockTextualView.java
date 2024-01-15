package cs3500.klondike.mocks;

import java.io.IOException;

import cs3500.klondike.view.TextualView;

/**
 * A mock class that implements TextualView and is used for testing.
 * Renders itself as "mock view".
 */
public class MockTextualView implements TextualView {
  Appendable out;

  public MockTextualView(Appendable out) {
    this.out = out;
  }

  @Override
  public void render() throws IOException {
    this.out.append(this.toString()).append("\n");
  }

  @Override
  public String toString() {
    return "Mock view";
  }
}
